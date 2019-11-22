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
    priority = 1

    refinedBills=[]
    appSorted = []
    maiSorted = []
    desSorted = []
    presorted = []

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

    #event
    #factor = cate name time course id
    for i in range(0,3):
        sortedOrder = presorted[i]
        if sortedOrder == [] :
            break
        if int(orderID) > 1000 :
            for uni_food in sortedOrder :
                temp = Food(orderID, uni_food[0],[uni_food[1], uni_food[2]],uni_food[3])
                temp.priority = priority
                temp.foodID = uni_food[4]
                temp.waitable = sortedOrder[0][2] - uni_food[2]
                s_ordered.insert(0,temp)
                priority += 1
            #normal

        else :
            for uni_food in sortedOrder :
                temp = Food(orderID, uni_food[0],[uni_food[1], uni_food[2]],uni_food[3])
                temp.priority = priority
                temp.foodID = uni_food[4]
                temp.waitable = sortedOrder[0][2] - uni_food[2]

                s_ordered.append(temp)
                priority += 1

    return s_ordered


def assigning(food, s_ordered, s_cook) :
    temp = []
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

    for element in s_ordered:
        if food.orderID == element.orderID:
            element.priority -= 1

    s_ordered.remove(food)

    return s_ordered, s_cook


def assign_cook(s_ordered, s_cook, serverClock) :
    # calc wait~ in ordered queue
    if serverClock != 0:
        for element in s_ordered:
            element.realwait += serverClock

#first step
    #Check scheduler can assign s_cook
    isFull = 0
    canAssign = []
    for cook in s_cook :
        if len(cook.charge) == cook.ability :
            isFull +=1
        else :
            canAssign.append(cook.position)

    if isFull == len(s_cook) :
        return s_ordered, s_cook


    #assign cook queue
    standard = copy(s_ordered)
    for element in standard :
        if element.cate in canAssign :
            if element.priority == 1 or \
                    element.waitable <= element.realwait:
                s_ordered, s_cook = assigning(element,s_ordered,s_cook)
            else :
                continue

#second step

    return s_ordered, s_cook