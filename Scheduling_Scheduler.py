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

#TODO
# app~main

def main() :
    # a[0] - food: 음식이름, 요리시간, 음식장르(app,mai,des)(초기화)
    # a[1] - cook별(요리사이름, 포지션, 역량, 현재 쿸리스트(None), 쿡시간(None), 블락유무(None)(쿡큐) 각 요리는[오더아이디, food_id, (음식이름, 소요시간)]
    # a[2] - order: order_id, food배열(이름, ID)(오더)
    # a[3] - servertime
    # a[4] - food 단위의 ordered list(오더 아이디 / food_id / (음식이름, 소요시간) / 우선순위(null) / 대기가능시간(null) / 대기시간(null) / 이벤트유무(null)) (오더큐)
    # 초기화 이후 파이썬 한번 거치고 난 결과값과 현재 order 리스트 와의 관계
    # a[5] - served(음식이름 / 담당한 요리사) (완료된요리; 로그)

    # Json load
    # information = loadJson()

    information = [ [ [ '파스타', 6, "app" ], ['리조또', 10, "app"], ['스테이s크', 7, "mai"] ],

     [ [ '박성호', '파스타', 2, 'None', "None", "None" ], [ '정성운', '리조또', 2, "None", "None", "None"], ['이상민', '스테이크', 2,"None", "None","None"] ],

     [ ['1', [['파스타',"0"], ['파스타', "1"],['스테이크',"2"]]]],

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