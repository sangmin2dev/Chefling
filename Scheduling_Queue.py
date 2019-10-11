'''
Scheduling Algorithm
Schedulig cooking order to improving process of kitchen & restaurant service
'''

#sudo
import queue
from Scheduling_Setup import *
from Scheduling_Event import *

total_Food = 0

def making_OSqueues() :
    ordered = queue.Queue()
    served = queue.Queue()

    return ordered, served

#fod 별로 쪼개서 넘어온다
#order별 요리음식 (Data)
class Food :
    def __init__(self,orderID, name, Deadline):
        self.orderID = orderID
        self.name = name
        self.Deadline = Deadline
        self.cook = ""
        self.gang = 0
        self.other_info = ""

#FIXME
#Setting Deadling
    def set_deadline(self, foodnum) :
        global total_Food

        for i in range(0, len(self.name)):
            each_Food = total_Food + i + 1


#Packing Each Order
def packing():
    orderlist = Ordering.split_order()
    orderID = orderlist[0]
    gang_ordered = []

    for i in orderlist[1]:
        Deadline = Food.set_deadline(len(orderlist[1]))
        gang_ordered.append(Food(orderID, i, Deadline))

#gang_ordered : [Food(id, food_name, deadline)]
    return gang_ordered


#TODO oderID : Object -> 큐에 넣기
#
#TODO Food information
#   1. general Setting (ex. cooking time, soldOutBool...)
#   2. 음식별 정보 (ex. name, oderID, Deadl
#


#FIXME
# Should sorting
def ordered_assign(ordered, gang_ordered) :
    for i in gang_ordered :
        ordered.put(i)

    return ordered

#주문음식 ordered Queue에 할당
#name은 음식 리스트
#데드라인 설정하기

#TODO
# chef Queue
#   Blocking
#   셰프큐는 여러개!!
#   기본 세팅에서 처리되어야 함
class Cook :
    def __init__(self, cook_id, cook_info):
        self.cook_id = cook_id
        self.ability = cook_info[1]
        self.breaktime = cook_info[2]
        self.charge = queue.Queue(maxsize=cook_info[0])
        self.sema = 1

    def Block(self, event):
        if event == True :
            self.sema = 0



def making_Cooks() :
    Cooks = []
    cook_info = loadChef()

    for cook_id in cook_info :
        Cooks.append(Cook(cook_id, cook_info[cook_id]))

    return Cooks



#TODO
# cook queue에 할당
def assign_cook(ordered,Cooks, served) :
    temp= []
#
#   ordered -> temp
#

#
#   temp -> cooks
#
    return ordered, Cooks, served


#TODO
# served queue에 할당
# cook 정보 추가해서 내보내기
def assign_served(Cooks, served) :
#
#   cook -> served
#
    pass