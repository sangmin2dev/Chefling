'''
Scheduling Setup
Setting basic information by customizing
'''

from random import *
from sys import *
from json import *
from Scheduling_Archi import *
from abc import *
# cooked
# 현재 쿡이 비어있는지 판단
# 비어있지 않다면 메뉴요리시간 - 현재 조리시간 반환

#ordered
# 레디 큐 순환하며 음식 서칭
# 음식별로 counting
# counting * 음식 조리 시간 / 큐 할당 수
# 단 다른 메뉴여도 같은 큐에 들어갈 수 있다는 점을 인식


count = 0

def loadJson() :
    information = loads(argv[1])

    return information


#TODO : loadFoodinit
def loadFoodinit(information) :
    #시간이 오래 걸리는 순으로 정렬
    menu = information[0]
    menu.sort(key= lambda menu : menu[1],reverse =True)

    #오래걸리는 순으로 정렬된 메뉴 리스트
    return menu



#TODO : loadOrdered
def loadOrdered(information) :
    prelist = information[4]
    ordered = []
    temp = []
    oneOrder = []

    if prelist ==  ["None"] :
        return ordered

    for uniInfo in prelist :
        #def __init__(self, orderID, cate, name, course):
        element = Food(int(uniInfo[0]), uniInfo[2], uniInfo[3], uniInfo[4])
        element.foodID = uniInfo[1]
        element.priority = int(uniInfo[5])
        element.waitable = int(uniInfo[6])
        element.realwait = int(uniInfo[7])
        element.emergency = int(uniInfo[8])
        element.andthen = int(uniInfo[9])


        temp.append(element)


    for uni_food in temp :
        if oneOrder == [] :
            oneOrder.append(uni_food)
            continue

        else :
            if uni_food.orderID == oneOrder[-1].orderID:
                oneOrder.append(uni_food)

            else:
                ordered.append(oneOrder)
                oneOrder = []
                oneOrder.append(uni_food)

    if oneOrder != []:
        ordered.append(oneOrder)

    return ordered

#TODO : loadCooks
def loadCooks(information) :
    prelist = information[1]
    cooklist = []
    for uniInfo in prelist :
        #cook_id, position,
        element = Cook(uniInfo[0],uniInfo[1], int(uniInfo[2]))

        if uniInfo[3] == ["None"] :
            element.charge = []
        else :
            element.charge = uniInfo[3]

        element.cookClock = uniInfo[4]

        if uniInfo[5] == "true":
            element.sema = True
        elif uniInfo[5] == "false":
            element.sema = False

        cooklist.append(element)
    return cooklist


#TODO : orderPassing
def orderPassing(information) :
    order = information[2][-1]
    orderID = int(order[0])
    foods = order[1]

    return orderID, foods

#TODO : output
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
                        uni_food.emergency, uni_food.andthen]
                op_ordered.append(temp)

    for element in s_cook :
        if element.charge == []:
            element.charge = ["None"]
        temp = [element.cook_id,element.position,element.ability,
                element.charge,element.cookClock, element.sema]
        op_cook.append(temp)

    fin_out = [op_ordered, op_cook]

    # print(op_ordered)
    # print(op_cook)
    print(dumps(fin_out))