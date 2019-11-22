#sudo
import queue
from Scheduling_Setup import *
from Scheduling_Archi import *
from Scheduling_Assign import *
from abc import *

#TODO
#   order 2개 이상부터
#   하나씩 들어가는 문제 해결


#initialize
#

#TODO
# app~main

# [카테고리, 이름, 소요시간, 타입]
# 카테고리, [이름, 소요시간], 타입

def main() :
    # a[0] - food: cate, name, 요리시간, 음식장르(app,mai,des)(초기화)
    # da[1] - cook별(요리사이름, 포지션, 역량, 현재 쿸리스트(None), 쿡시간(None), 블락유무(None)(쿡큐) 각 요리는[오더아이디, food_id, 카테고리,[ 음식이름, 소요시간], 타입]
    # a[2] - order: [order_id, food배열(name, ID)](오더)
    # a[3] - servertime
    # da[4] - food 단위의 ordered list(오더 아이디 / food_id / (cate, [음식이름, 소요시간], type....] / 우선순위(null) / 대기가능시간(null) / 대기시간(null) / 이벤트유무(null)) (오더큐)
    # 초기화 이후 파이썬 한번 거치고 난 결과값과 현재 order 리스트 와의 관계
    # da[5] - served(음식이름 / 담당한 요리사) (완료된요리; 로그)

    # Json load
    information = loadJson()

    # information = [ [ [ 'pasta', '파스타', 6, "app" ], [ 'r','리조또', 10, "app"], ['s','스테이크', 7, "mai"] ],
    #
    #  [ [ '박성호', 'pasta', 2, 'None', "None", "None" ], [ '정성운', 'r', 2, "None", "None", "None"], ['이상민', 's', 2,"None", "None","None"] ],
    #
    #  [ ['0', [['파스타',"0_0"], ['파스타', "0_1"],['스테이크',"0_2"]]]],
    #
    #  0,
    #
    #  [0],
    #
    #  [ 'z' ] ]


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