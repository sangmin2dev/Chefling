#sudo
import queue
from Scheduling_Setup import *
from Scheduling_Event import *
from Scheduling_Archi import *
from Scheduling_Assign import *
from threading import *


#TODO
# Input Output 설정 (Pasing X Packet O)
# order -> cook


#initialize
#
def main() :

    #Json load
#    information = loadJson()

    information = [ [ [ '파스타', 15 ], ['스파게티', 10], ['스테이크', 7] ],

   [ [ '박성호', '포지션1', 2 ], [ '정성운', '포지션2', 2 ] ],

   [ [ '1', ['파스타', '스파게티','스테이크'] ],],

   0,

   [],

   [],

   [ 'None' ] ]


    menu = loadFoodinit(information)
    kitchen = loadChefinit(information)

#TODO
    #처음에 큐 이니셜라이징??

    #Queue setting
    s_ordered = loadOrdered(information)
    s_cook = loadCooks(information)
    s_served = loadServed(information)


    serverClock = information[3]


    #Scheduling
    s_ordered = assign_ordered(s_ordered,menu, information)
    s_ordered, s_cook = assign_cook(s_ordered, s_cook, serverClock)

    #output
    output(s_ordered, s_cook)



if __name__ == "__main__":
    main()