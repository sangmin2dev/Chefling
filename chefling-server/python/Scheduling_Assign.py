'''
Scheduling Algorithm
Schedulig cooking order to improving process of kitchen & restaurant service
'''

from Scheduling_Setup import *
from Scheduling_Event import *
from Scheduling_Archi import *

#return [['stake', 10], ['pasta', 7], ['dessert', 5]]
#return ['1000', ['pasta', 'stake', 'dessert']]

def assign_ordered(s_ordered, menu):
    orderID, bills = orderPassing()

    sortedOrder = []
    priority = 1

    for element in bills :
        sortedOrder.append(menu[menu.index(element)])


    #event
    if sortedOrder[0].orderID > 1000 :
        sortedOrder.sort(key=lambda sortedOrder: sortedOrder[1], reverse=False)
        for uni_food in sortedOrder :
            temp = Food(orderID, uni_food)
            temp.priority = priority
            temp.waitable = sortedOrder[0][1] - uni_food[1]
            s_ordered.insert(0,Food(temp))
            priority += 1
    #normal
    else :
        sortedOrder.sort(key=lambda sortedOrder: sortedOrder[1], reverse=True)
        for uni_food in sortedOrder :
            temp = Food(orderID, uni_food)
            temp.priority = priority
            temp.waitable = sortedOrder[0][1] - uni_food[1]
            s_ordered.append(Food(temp))
            priority += 1

    return s_ordered


def assigning(food, s_ordered, s_cook) :
    for element in s_ordered :
        if food.orderID == element.orderID :
            element.priority -= 1
    s_ordered.remove(food)

    for uni_archi in s_cook :
        if uni_archi.position == element.name :
            uni_archi.charge.append(food)
            break

    return s_ordered, s_cook


#TODO
# cook queue에 할당
def assign_cook(s_ordered,s_cook, serverClock) :

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
    for element in s_ordered :
        if element.name in canAssign :
            if element.priority == 1 or \
                    element.waitable <= element.realwait:
                s_ordered, s_cook = assigning(element,s_ordered,s_cook)
        else :
            continue

    return s_ordered, s_cook