'''
Scheduler Architecture
Struct Scheduler Architecture
'''

from Scheduling_Setup import *
from abc import *

total_Food = 0

#fod 별로 쪼개서 넘어온다
#order별 요리음식 (Data)
#개별 음식 객체
class Food(metaclass = ABCMeta) :
    def __init__(self,orderID, cate, name, course):
        self.orderID = orderID
        self.cate = cate
        self.name = name
        self.course = course

        self.priority = 1
        self.waitable = 0
        self.realwait = 0
        self.andthen = 0

        self.emergency = 0

        self.foodID = 0

        self.cook = ""

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
class Cook(metaclass = ABCMeta) :
    def __init__(self, cook_id, position, ability):
        self.cook_id = cook_id
        self.position = position
        self.ability = ability

        self.charge = []

        self.cookClock = 0

        self.sema = False

    def Block(self, event):
        if event == True :
            self.sema = 0
