'''
Scheduling Setup
Setting basic information by customizing
'''

from random import *
from sys import *
from json import *
from Scheduling_Archi import *
from abc import *

count = 0

def loadJson() :
    information = loads(argv[1])

    return information


#initialize
def loadFoodinit(information) :
    #시간이 오래 걸리는 순으로 정렬
    menu = information[0]
    menu.sort(key= lambda menu : menu[1],reverse =True)

    #오래걸리는 순으로 정렬된 메뉴 리스트
    return menu
############



#Queue Architecture
def loadOrdered(information) :
    prelist = information[4]
    ordered = []

    if prelist ==  ["None"] :
        return ordered

    for uniInfo in prelist :
        #def __init__(self, orderID, cate, name, course):
        element = Food(uniInfo[0], uniInfo[2], uniInfo[3], uniInfo[4])
        element.foodID = uniInfo[1]
        element.priority = uniInfo[5]
        element.waitable = uniInfo[6]
        element.realwait = uniInfo[7]
        element.emergency = uniInfo[8]

        ordered.append(element)

    return ordered


def loadCooks(information) :
    prelist = information[1]
    cooklist = []

    for uniInfo in prelist :
        #cook_id, position,
        element = Cook(uniInfo[0],uniInfo[1], uniInfo[2])
        if uniInfo[3] == ["None"] :
            element.charge = []
        else :
            element.charge = uniInfo[3]
        element.cookClock = uniInfo[4]
        element.sema = uniInfo[5]
        cooklist.append(element)
    return cooklist
#####################

#ordering
def orderPassing(information) :
    order = information[2][-1]
    orderID = order[0]
    foods = order[1]

    return orderID, foods


def output(s_ordered, s_cook) :
    op_ordered = []
    op_cook =[]

    if s_ordered == []:
        op_ordered = ["None"]
    else :
        # print(s_ordered)
        for oneOrder in s_ordered :
            for uni_food in oneOrder :
                temp = [uni_food.orderID,uni_food.foodID, uni_food.cate, uni_food.name,
                        uni_food.course, uni_food.priority, uni_food.waitable, uni_food.realwait,
                        uni_food.emergency]
                op_ordered.append(temp)

    for element in s_cook :
        if element.charge == []:
            element.charge = ["None"]
        temp = [element.cook_id,element.position,element.ability,
                element.charge,element.cookClock, element.sema]
        op_cook.append(temp)

    fin_out = [op_ordered, op_cook]

    print(dumps(fin_out))