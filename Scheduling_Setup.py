'''
Scheduling Setup
Setting basic information by customizing
'''

from random import *
from sys import *
from json import *
from Scheduling_Archi import *

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

def loadChefinit(information) :
    cooks = information[1]
    return cooks
############



#Queue Architecture
def loadOrdered(information) :
    prelist = information[4]
    ordered = []
    for uniInfo in prelist :
        # orderID, name, priority, waiting, realwait,emergency
        element = Food(uniInfo[0],uniInfo[1])
        element.priority = uniInfo[2]
        element.waitable = uniInfo[3]
        element.realwait = uniInfo[4]
        element.emergency = uniInfo[5]
        ordered.append(element)
    return ordered

def loadCooks(information) :
    prelist = information[5]
    cooklist = []
    for uniInfo in prelist :
        #cook_id, position,
        element = Cook(uniInfo[0],uniInfo[1], uniInfo[2])
        element.charge = uniInfo[3]
        element.cookClock = uniInfo[4]
        element.sema = uniInfo[5]
        cooklist.append(element)
    return cooklist

def loadServed(information) :
    prelist = information[6]
    served = []
    for uniInfo in prelist:
        #orderID, name, cook
        element = Food(uniInfo[0],uniInfo[1])
        element.cook = uniInfo[2]
        served.append(element)
    return served
#####################


#ordering
def orderPassing(information) :
    order = information[2]
    orderID = order[0]
    foods = order[1]

    return orderID, foods


def output(s_ordered, s_cook) :
    op_ordered = []
    op_cook =[]
    for element in s_ordered :
        temp = [element.orderID, element.name,
                element.priority, element.waitable, element.realwait,
                element.emergency]
        op_ordered.append(temp)

    for element in s_cook :
        temp = [element.cook_id,element.position,element.ability,
                element.charge,element.cookClock, element.sema]
        op_cook.append(temp)

    print(op_ordered)
    print(op_cook)