#sudo
import queue
from Scheduling_Setup import *
from Scheduling_Event import *
from Scheduling_Archi import *
from Scheduling_Assign import *
from threading import *
from abc import *

#TODO
#   order 2개 이상부터
#   하나씩 들어가는 문제 해결


#initialize
#
def main() :

    #Json load
    #information = loadJson()

    information = [ [ [ '파스타', 15 ], ['리조또', 10], ['스테이크', 7] ],

     [ [ '박성호', '파스타', 2, 'None', "None", "None" ], [ '정성운', '리조또', 2, "None", "None", "None"], ['이상민', '스테이크', 2,"None", "None","None"] ],

     [ ['1', [['파스타',"hgsd___2222hlkfjasflk____2"], ['리조또', "ashgkjdsfh"],['스테이크',"kjsadhkgjas;k"]]]],

     0,

     [0],

     [ 'z' ] ]


    menu = loadFoodinit(information)


    #Queue setting
    s_ordered = loadOrdered(information)
    s_cook = loadCooks(information)



    serverClock = information[3]


    #Scheduling
    s_ordered = assign_ordered(s_ordered,menu, information)
    s_ordered, s_cook = assign_cook(s_ordered, s_cook, serverClock)

    #output
    output(s_ordered, s_cook)



if __name__ == "__main__":
    main()