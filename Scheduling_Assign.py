'''
Scheduling Algorithm
Schedulig cooking order to improving process of kitchen & restaurant service
'''

from Scheduling_Setup import *
from Scheduling_Event import *
from Scheduling_Archi import *

#Packing Each Order (unisizing)

#return [['stake', 10], ['pasta', 7], ['dessert', 5]]
#return ['1000', ['pasta', 'stake', 'dessert']]

def assign_ordered(ordered,foodInfo):
    orderlist = orderPassing()
    #요리시간(오래걸리는순)으로 정렬된 orderlist
    sortedOrder = []
    priority = 1

    for menu in foodInfo:
        for uni_food in orderlist[1] :
            if menu[0] == uni_food :
                sortedOrder.append([uni_food,menu[1]])

    for uni_food in sortedOrder :
        waiting = sortedOrder[0][1] - uni_food[1]
        temp = Food(orderlist[0], uni_food, priority, waiting)
        ordered.append(Food(temp))
        priority += 1

    return ordered



#emergency situation
def assing_emergency(ordered) :
    pass

#TODO
# cook queue에 할당
#Cooks(id,position,ability,charge,sema)
def assign_cook(ordered,Cooks, served) :

    for uni_cook in Cooks :
        if uni_cook.charge == uni_cook.ability :
            pass
        else :
            pass
            #waiting < cookingtime : 할당

    return ordered, Cooks, served


#TODO
# served queue에 할당
# cook 정보 추가해서 내보내기
def assign_served(Cooks, served) :
#
#   cook -> served
#
    pass