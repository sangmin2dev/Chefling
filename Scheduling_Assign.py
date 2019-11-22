'''
Scheduling Algorithm
Schedulig cooking order to improving process of kitchen & restaurant service
'''

from Scheduling_Setup import *
from Scheduling_Event import *
from Scheduling_Archi import *
from copy import *
from abc import *

#return [['stake', 10], ['pasta', 7], ['dessert', 5]]
#return ['1000', ['pasta', 'stake', 'dessert']]

def assign_ordered(s_ordered, menu,information):
    orderID, bills = orderPassing(information)

    sortedOrder = []
    priority = 1
    standard = []

    refinedBills=[]
    for uni_food in bills :
        refinedBills.append(uni_food[0])

    for menu_ele in menu :
        standard.append(menu_ele[0])

    for element in refinedBills :
        sortedOrder.append(menu[standard.index(element)])

    #event
    if int(orderID) > 1000 :
        sortedOrder.sort(key=lambda sortedOrder: sortedOrder[1], reverse=False)
        for uni_food in sortedOrder :
            temp = Food(orderID, uni_food)
            temp.priority = priority
            temp.waitable = sortedOrder[0][1] - uni_food[1]
            s_ordered.insert(0,temp)
            priority += 1
    #normal
    else :
        sortedOrder.sort(key=lambda sortedOrder: sortedOrder[1], reverse=True)
        for uni_food in sortedOrder :
            temp = Food(orderID, uni_food)
            temp.priority = priority
            temp.waitable = sortedOrder[0][1] - uni_food[1]
            s_ordered.append(temp)
            priority += 1

    return s_ordered


def assigning(food, s_ordered, s_cook) :
    temp = []
    for uni_archi in s_cook :
        if uni_archi.position == food.name[0] :
            temp.append(food.orderID)
            temp.append(food.foodID,)
            temp.append(food.name)

            uni_archi.charge.append(temp)
            break

    for element in s_ordered:
        if food.orderID == element.orderID:
            element.priority -= 1


    s_ordered.remove(food)

    return s_ordered, s_cook


def assign_cook(s_ordered, s_cook, serverClock) :


    # calc wait~ in ordered queue
    for element in s_ordered:
        element.realwait += serverClock

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
        if element.name[0] in canAssign :
            if element.priority == 1 or \
                    element.waitable <= element.realwait:
                s_ordered, s_cook = assigning(element,s_ordered,s_cook)
        else :
            continue


    # standard = s_ordered
    # print("101",len(standard))
    # for element in standard :
    #     print(element)
    #     s_ordered, s_cook = assigning(element, s_ordered, s_cook)

    return s_ordered, s_cook