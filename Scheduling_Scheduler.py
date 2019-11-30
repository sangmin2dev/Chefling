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


#TODO : main
def main() :
    # a[0] - food: cate, name, 요리시간, 음식장르(app,mai,des)(초기화)
    # a[1] - cook별(요리사이름, 포지션, 역량, 현재 쿸리스트(None), 쿡시간(None), 블락유무(None)(쿡큐) // 각 요리는[오더아이디, food_id, 카테고리,[ 음식이름, 소요시간], 타입]
    # a[2] - order: [order_id, food배열(name, ID)](오더)
    # a[3] - servertime
    # a[4] - food 단위의 ordered list(오더 아이디 / food_id / cate / [음식이름, 소요시간] / type / 우선순위(null) / 대기가능시간(null) / 대기시간(null) / 이벤트유무(null) / andthen) (오더큐)
    # 초기화 이후 파이썬 한번 거치고 난 결과값과 현재 order 리스트 와의 관계
    # a[5] - time per menu  [...](메뉴이름 / 시간)
    # a[6] - time per food [...] (음식이름 / 음식아이디 / 남은 음식 수 / 시간)

    # output - 대기시간
    # [갈릭 파스타 ]

    # Json load
#    information = loadJson()

    information = [ [ [ 'bread', '갈릭 브레드', 6, "app" ], [ 'pasta','갈릭 까르보나라', 12, "mai"], ['pizza','부처스 피자', 14, "mai"], ['dessert','아포카토', 5 ,'des'] ],

                     [ [ '박성호', 'bread', 2, [["1","1_1","bread",["갈릭 브레드",12],"app"],["1","1_1","bread",["갈릭 브레드",12],"app"]], "None", "None" ], [ '정성운', 'pasta', 2, [["1","1_1","pasta",["갈릭 까르보나라",12],"app"]], "None", "None"], ['이상민', 'pizza', 2,[["1","1_2","pizza",["부처스 피자",12],"mai"]], "None","None"], ['백종원', 'dessert', 1, ["None"],"None","None"] ],

                     [['1', [['갈릭 브레드',"0_0"]]], ['2', [['갈릭 브레드',"1_1"]]],['2', [['갈릭 브레드',"1_1"]]]],

                     2,

                     ["None"] ,

                    ["None"],

                    ["None"]]

#['2', [['갈릭 브레드',"0_0"], ['갈릭 까르보나라', "0_1"],['부처스 피자',"0_2"]]],

#["1","1_1","bread",["갈릭 브레드",12],"app","1","1","0","1", 0 ],["1","1_2","pasta",["갈릭 까르보나라",12],"mai","2","1","0","1", 1 ]

    menu = loadFoodinit(information)

    #Queue setting
    s_ordered = loadOrdered(information)
    s_cook = loadCooks(information, menu)

    serverClock = int(information[3])

    #Scheduling
    s_ordered = assign_ordered(s_ordered,menu, information)

    s_ordered, s_cook, t_menu, t_food = assign_cook(s_ordered, s_cook, serverClock, menu)


    #output
    output(s_ordered, s_cook, t_menu, t_food)



if __name__ == "__main__":
    main()