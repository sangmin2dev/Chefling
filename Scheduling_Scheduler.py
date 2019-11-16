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
    information = loadJson()

    menu = loadFoodinit(information)
    kitchen = loadChefinit(information)

#TODO
    #처음에 큐 이니셜라이징??

    #Queue setting
    s_ordered = loadOrdered(information,menu)
    s_cook = loadCooks(information)
    s_served = loadServed(information)


    serverClock = information[3]


    #Scheduling
    s_ordered = assign_ordered(s_ordered,menu)
    s_ordered, s_cook = assign_cook(s_ordered, s_cook, serverClock)

    #output
    pass



if __name__ == "__main__":
    main()