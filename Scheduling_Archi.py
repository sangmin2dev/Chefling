'''
Scheduler Architecture
Struct Scheduler Architecture
'''

from Scheduling_Setup import *
from Scheduling_Event import *

total_Food = 0

#fod 별로 쪼개서 넘어온다
#order별 요리음식 (Data)
#개별 음식 객체
class Food :
    def __init__(self,orderID, name, priority, waiting):
        self.orderID = orderID
        self.name = name

        self.priority = priority
        self.waiting = waiting
        self.realwait = 0

        self.emergency = 0

        self.cook = ""
        self.gang = 0
        self.other_info = ""


#TODO oderID : Object -> 큐에 넣기
#
#TODO Food information
#   1. general Setting (ex. cooking time)
#   2. 음식별 정보 (ex. name, oderID, Deadline(my))



#주문음식 ordered Queue에 할당
#name은 음식 리스트
#데드라인 설정하기

#TODO
# chef Queue
#   Blocking
#   셰프큐는 여러개!!
#   기본 세팅에서 처리되어야 함
class Cook :
    def __init__(self, cook_id, position, cook_info, serverClock):
        self.cook_id = cook_id
        self.position = position

        self.ability = cook_info[1]
        self.charge = []

        self.cookClock
        self.serverClock

        self.sema = 1

    def Block(self, event):
        if event == True :
            self.sema = 0


def making_Cooks() :
    Cooks = []
    cook_info = loadChef()

    for uni_cook in cook_info :
        Cooks.append(Cook(uni_cook[0], uni_cook[1], uni_cook[2]))


    return Cooks