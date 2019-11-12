'''
Scheduling Setup
Setting basic information by customizing
'''

from random import *
from sys import *
from json import *
from Scheduling_Archi import *

#
#TODO Json -> 정보
#
#Basic Setting
#Chef : 쿸수, 요리사별 Position, 포지션별 역량
#Food : 메뉴, 음식별 요리 소요시간
#
count = 0

#arg : 1 -> parameter

def loadJson(information) :
    information = loads(argv[1])
    return information

def loadOrdered(information) :
    ordered = information[4]
    for uniInfo in ordered :
        Food(uniInfo[0],uniInfo[1],uniInfo[2],uniInfo[3])
    #orderID, name, priority, waiting

def loadServed(information) :
    prelist = information[6]
    served = []
    for uniInfo in prelist:
        element = Food(uniInfo[0],uniInfo[1])
        served.append(element)
    return served



def loadCooks(information) :
    prelist = information[5]
    cooklist = []
    for uniInfo in prelist :
        element = Cook(uniInfo[0],uniInfo[1],uniInfo[2],uniInfo[3], uniInfo[4], uniInfo[5])
        #이름, 포지션, 어빌리티, 현재 요리중인 음식, 요리사 클락, 서버 클락
        cooklist.append(element)
    return cooklist




def loadChefinit(information) :
    cooks = information[1]
    return cooks


def loadFoodinit(information) :
    #시간이 오래 걸리는 순으로 정렬
    menu = information[0]
    menu.sort(key= lambda menu : menu[1],reverse =True)

    return menu

def orderPassing(information) :
    order = information[2]
    orderID = order[0]
    foods = order[1]

    return orderID, foods