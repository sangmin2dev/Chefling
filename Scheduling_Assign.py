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

def assign_ordered(s_ordered, menu, information):
    orderID, bills = orderPassing(information)

    if bills == [] :
        return s_ordered

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


    if appSorted == []:
        isapp = False

    if appSorted != []:
        isapp = True

    presorted.append(appSorted)
    presorted.append(maiSorted)
    presorted.append(desSorted)

    #event
    #factor = cate name time course id
    for i in range(0,3):
        sortedOrder = presorted[i]
        if sortedOrder == [] :
            continue
        for uni_food in sortedOrder :
            temp = Food(orderID, uni_food[0],[uni_food[1], uni_food[2]],uni_food[3])
            temp.priority = prior
            temp.foodID = uni_food[4]
            temp.waitable = sortedOrder[0][2] - uni_food[2]
            temp.realwait = 0
            if sortedOrder == appSorted :
                temp.andthen = 0
            elif isapp == False :
                temp.andthen = 0
            else :
                temp.andthen = 1
            prior += 1

            oneOrder.append(temp)

    if int(orderID) > 1000 :
        s_ordered.insert(0,oneOrder)
    else :
        s_ordered.append(oneOrder)


    return s_ordered


#TODO : orderpart
def orderpart(ID, s_ordered):
    for eachOrder in s_ordered :
        for unifood in eachOrder:
            if unifood.foodID == ID:
                eachOrder.remove(unifood)

    return s_ordered, 0

#TODO : cookpart
def cookpart(food, s_cook) :
    temp = [food.orderID, food.foodID, food.cate, food.name, food.course]
    index = food.foodID
    precook = None

    for uni_cook in s_cook:
        if uni_cook.position == food.cate:
            if precook == None :
                precook = uni_cook
            else :
                if len(precook.charge) <= len(uni_cook.charge):
                    continue
                else :
                    precook = uni_cook
        else :
            continue

        precook.cookClock = food.name[1]
        if precook.charge == ["None"]:
            precook.charge = []
        precook.charge.append(temp)

    return s_cook


#TODO : modPriority
def modPriority(oneOrder) :
    for element in oneOrder:
        if element.priority >= 2 :
            element.priority -= 1

    return oneOrder


#TODO : finishApp
def finishApp(oneOrder, uni_food):
    index = ""

    for element in oneOrder :
        if element.priority == 0:
            continue
        else :
            index = element.course
            break

    if index == "":
        return oneOrder
    elif index == "app":
        return oneOrder
    else :
        for uni in oneOrder :
            uni.andthen = 0

    return oneOrder

#TODO : assignable
def assignable(s_cook):
    #Check scheduler can assign s_cook
    isFull = 0
    canAssign = []

    for cook in s_cook :
        if cook.sema == True :
            isFull += 1
        else :
            if cook.charge == ["None"] :
                canAssign.append(cook.position)
            else :
                if len(cook.charge) == cook.ability :
                    isFull +=1
                else :
                    canAssign.append(cook.position)

    if isFull == len(s_cook) :
        canAssign = []

    return canAssign

#FIXME
#TODO : estimating
def estimating():
    pass


#TODO : assign_cook
def assign_cook(s_ordered, s_cook, serverClock, menu) :
    # calc wait~ in ordered queue
    if serverClock != 0:
        for eachOrder in s_ordered:
            for element in eachOrder:
                element.realwait += serverClock


    canAssign = assignable(s_cook)
    if canAssign == []:
        return s_ordered, s_cook


    #assign cook queue
    nonchain = deepcopy(s_ordered)



#빈 리스트 예외처리
    for f_eachOrder in nonchain :
        for n_unifood in f_eachOrder :
            if (int(n_unifood.priority) <= 1 or int(n_unifood.waitable) <= int(n_unifood.realwait)) \
                    and (int(n_unifood.andthen) == 0) and (n_unifood.cate in canAssign) :

                s_ordered, n_unifood.priority = orderpart(n_unifood.foodID, s_ordered)
                s_cook = cookpart(n_unifood, s_cook)
                f_eachOrder = copy(modPriority(f_eachOrder))
                f_eachOrder = copy(finishApp(f_eachOrder,n_unifood))


                canAssign = assignable(s_cook)

            else :
                continue

# #second step
    sec_canAssign = assignable(s_cook)
    if sec_canAssign == []:
        return s_ordered, s_cook

    #assign cook queue
    sec_nonchain = copy(s_ordered)
    for s_eachOrder in sec_nonchain :
        if s_eachOrder == [] :
            continue
        for unifood in s_eachOrder :
            if unifood.cate in canAssign :
                if  (unifood.andthen == 0) and (n_unifood.cate in canAssign):

                    s_ordered, n_unifood.priority = orderpart(n_unifood.foodID, s_ordered)
                    s_cook = cookpart(n_unifood, s_cook)
                    f_eachOrder = copy(modPriority(f_eachOrder))
                    f_eachOrder = copy(finishApp(f_eachOrder, n_unifood))

                    canAssign = assignable(s_cook)

                else :
                    continue


#현재 요리 상황 고려한 시간 출력
#메뉴 예상 요리시간
    t_menu = []
    t_food = []
    #food : time
    menulist = {}

    cookfortime = {}
    cookforlen = {}
    cookforcomp = {}
    cookingavg = {}

    foodwait = {}

    cate = 0
    sum = 0
    divide = 0

    for uni in menu :
        menulist[uni[0]] = 0
        t_menu.append([uni[1], uni[2]])

        if cate == 0 :
            cate = uni[0]
            sum += uni[2]
            divide +=1

        elif uni == menu[-1]:
            if cate != uni[0]:
                cookingavg[cate] = sum / divide
                sum = 0
                divide = 0
            sum += uni[2]
            divide+=1
            cookingavg[uni[0]] = sum / divide

        elif cate != uni[0]:
            cookingavg[cate] = sum/divide
            cate = uni[0]
            sum = uni[2]
            divide = 1

        else :
            sum += uni[2]
            divide += 1


    for uni in menu:
        foodwait[uni[0]] = 0


    for uni in s_cook :
        if not (uni.position in cookfortime):
            cookfortime[uni.position] = int(uni.cookClock)
        else :
            if cookfortime[uni.position] > int(uni.cookClock):
                continue
            else :
                cookfortime[uni.position] = int(uni.cookClock)

    for uni in s_cook:
        if not (uni.position in cookforlen):
            cookforlen[uni.position] = 1
            cookforcomp[uni.position] = 0
        else :
            cookforlen[uni.position] += 1

#incook
    for unicook in s_cook :
        for unifood in unicook.charge:
            t_food.append([unifood[3], unifood[1], 0])

    for oneOrder in s_ordered :
        for unifood in oneOrder :
            if cookforcomp[unifood.cate] == cookforlen[unifood.cate]:
                foodwait[unifood.cate] += 1
                cookforcomp[unifood.cate] = 0
                t_food.append([unifood.name,unifood.foodID,foodwait[unifood.cate], unifood.time])

            else :
                cookforcomp[unifood.cate] += 1
                menulist[unifood.cate] += unifood.name[1]
                foodwait[unifood.cate] += 1
                unifood.time = menulist[unifood.cate] + cookingavg[unifood.cate]
                t_food.append([unifood.name, unifood.foodID,foodwait[unifood.cate], unifood.time])

    return s_ordered, s_cook, t_menu, t_food