'''
Scheduling Algorithm
Schedulig cooking order to improving process of kitchen & restaurant service
'''

from Scheduling_Setup import *
from Scheduling_Archi import *
from copy import *
from abc import *

#return [['stake', 10], ['pasta', 7], ['dessert', 5]]
#return ['1000', ['pasta', 'stake', 'dessert']]

def assign_ordered(s_ordered, menu,information):
    orderID, bills = orderPassing(information)

    sortedOrder = []
    prior = 1

    refinedBills=[]
    appSorted = []
    maiSorted = []
    desSorted = []
    presorted = []
    oneOrder = []

    temp = []

    for uni_bill in bills :
        for uni_menu in menu :
            if uni_menu[1] == uni_bill[0]:
                temp = [uni_menu[0],uni_menu[1],uni_menu[2],uni_menu[3],uni_bill[1]]
                refinedBills.append(temp)


    #메뉴 Menu
    for element in refinedBills :
        if element[3] == "app" :
            appSorted.append(element)
        if element[3] == "mai":
            maiSorted.append(element)
        if element[3] == "des":
            desSorted.append(element)

    appSorted.sort(key=lambda appSorted: appSorted[2], reverse=True)
    maiSorted.sort(key=lambda maiSorted: maiSorted[2], reverse=True)
    desSorted.sort(key=lambda desSorted: desSorted[2], reverse=True)

    presorted.append(appSorted)
    presorted.append(maiSorted)
    presorted.append(desSorted)

    if appSorted == []:
        isapp = False

    if appSorted != []:
        isapp = True

    #event
    #factor = cate name time course id
    for i in range(0,3):
        sortedOrder = presorted[i]
        if sortedOrder == [] :
            break
        for uni_food in sortedOrder :
            temp = Food(orderID, uni_food[0],[uni_food[1], uni_food[2]],uni_food[3])
            temp.priority = prior
            temp.foodID = uni_food[4]
            temp.waitable = sortedOrder[0][2] - uni_food[2]
            if (isapp == True) and (uni_food[3] == "mai"):
                temp.andthen = 1
            else :
                temp.andthen = 0
            prior += 1

            oneOrder.append(temp)

    if int(orderID) > 1000 :
        s_ordered.insert(0,oneOrder)
    else :
        s_ordered.append(oneOrder)

    return s_ordered

def modPriority(oneOrder, s_ordered) :
    for element in oneOrder:
        if element.priority >= 2 :
            element.priority -= 1

    return oneOrder


def orderpart(ID, s_ordered):
    for eachOrder in s_ordered :
        for unifood in eachOrder:
            if unifood.foodID == ID:
                eachOrder.remove(unifood)

    for i in s_ordered:
        for j in i :
            print(j.name)

    return s_ordered

def cookpart(food, s_cook) :
    temp = []
    index = food.foodID

    for uni_archi in s_cook :
        if uni_archi.position == food.cate :
            temp.append(food.orderID)
            temp.append(food.foodID,)
            temp.append(food.cate)
            temp.append(food.name)
            temp.append(food.course)
            if uni_archi.charge == ["None"]:
                uni_archi.charge = []
            uni_archi.charge.append(temp)
            break

    return s_cook


#oneOrder : 변화주체
#s_orered : 비교주체
#orderID : 인덱스
def finishApp(oneOrder, s_ordered):
    index = oneOrder[0].orderID

    if s_ordered != []:
        for each in s_ordered :
            if len(each) == 0:
                return oneOrder
            if each[0].orderID == index:
                comp = each

        if comp[0].course == "app":
            return oneOrder

        else :
            for uni_food in oneOrder :
                uni_food.andthen = 0

    return oneOrder

def assignable(s_cook):
    #Check scheduler can assign s_cook
    isFull = 0
    canAssign = []
    for cook in s_cook :
        if len(cook.charge) == cook.ability :
            isFull +=1
        else :
            canAssign.append(cook.position)

    if isFull == len(s_cook) :
        canAssign = []

    return canAssign

def findandfetch(eachorder, chain):
    for comp in chain:
        if eachorder is comp:
            return(chain.index(comp))


def assign_cook(s_ordered, s_cook, serverClock) :
    # calc wait~ in ordered queue
    if serverClock != 0:
        for element in s_ordered:
            element.realwait += serverClock

    canAssign = assignable(s_cook)
    if canAssign == []:
        return s_ordered, s_cook


    print("in",s_ordered)
    #assign cook queue
    chain = copy(s_ordered)
    nonchain = deepcopy(s_ordered)

    flag = 0

#빈 리스트 예외처리
    for n_eachOrder in nonchain :

        for n_unifood in n_eachOrder :
            if (n_unifood.priority <= 1 or n_unifood.waitable <= n_unifood.realwait) \
                    and (n_unifood.andthen == 0) and (n_unifood.cate in canAssign):

                s_ordered = orderpart(n_unifood.foodID, chain)
                s_cook = cookpart(n_unifood, s_cook)


                n_eachOrder = copy(modPriority(n_eachOrder, s_ordered))
                n_eachOrder = copy(finishApp(n_eachOrder,s_ordered))

            else :
                continue


    for i in s_ordered:
        for j in i:
            print("out",j.name)
            print("out",j.priority)
            print("out",j.andthen)

    for i in s_cook:
        print("out",i.cook_id)
        print("out",i.position)
        print("out",i.charge)


# #second step
    sec_canAssign = assignable(s_cook)
    if sec_canAssign == []:
        return s_ordered, s_cook


    #assign cook queue
    sec_nonchain = copy(s_ordered)
    for eachOrder in sec_nonchain :
        if eachOrder == [] :
            continue
        eachOrder = finishApp(eachOrder)
        for unifood in eachOrder :
            if unifood.cate in canAssign :
                if  unifood.andthen == 0 :
                    s_ordered = orderpart(unifood.foodID, eachOrder, s_ordered)
                    s_cook = cookpart(unifood, s_cook)
                    s_ordered = modPriority(eachOrder, s_ordered)
                else :
                    continue

    return s_ordered, s_cook