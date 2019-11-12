'''
Scheduling Event
Processing Event such as odering, unexpected issue and so on
'''
#TODO Json -> 정보

from time import *
from threading import *

from threading import *

class Ordering :
    def __init__(self, orderID,):
        self.orderID = orderID
        self.ordered_food = []

####
####jason -> 정보
####

#return [orderID, [pasta,stake, stake, cake]]
    def making_bills(self) :
        pass

    #order가 왔는지 checking
    #return bool
    def checking_order(self) :
        pass

#TODO
# 1. cook queue에 있는 객체를 served에 내보낸다
# 2. 빈칸이 생긴 cook큐에 assign_cook()
#return Food() (object)
def finish_cooking(cooks):
    pass