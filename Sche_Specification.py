#!!!!!!!!논의사항!!!!!!!!!
#1. 넘어오는 데이터 형
#       음식
#       셰프등 기본정보
#       오더 아이디 정의해야함
#       이벤트 처리


'''
Scheduling Specification
opensource, for making sence of Scheduling Algorithm
'''


#Information Setting

#   Chef
#       position
#       number of food at a time

#   Food
#       Cooking Time
#       Food Category
#       Soldout

#   Oder
#       Oder ID
#       order food



#Scheduling System

#   Odered Queue
#       FIFO by OderID
#       If Issue : Assign Queue's Head
#       Set Deadling(Current Queue, OderID)

#   Chef Queue
#       Assign Considering Chef Information
#       If Issue : Block Relevant Queue
#       Gang Scheduling

#   Served Queue
#       Adding cook Information

#TODO
#Input Json
# {
#     menu :
#         {
#             cooks :{ (cook은 position과 같은 의미로 봐도 무방할듯!)
#                 cook1 :{
#                     cook_id :
#                     position :
#                     ability : 한번에 처리할 수 있는 요리의 갯수
#                     breaktime :
#                 }
#                 cook2 :{
#                     동일
#                 }
#                 ...
#
#             }
#             foods :{
#                 food1 :{
#                     name :
#                     image :
#                     category : 카테고리는 포지션에 해당하는 카테고리를 의미함 (같은 포지션에서 처리할 수 있는 요리!!)
#                     description :
#                     price :
#                     cooking_time :
#                     sold_out :
#                 }
#                 food2 : {
#                     동일
#                 }
#                 ...
#
#             }
#             restaurant_info :{
#                 name :
#                 logo :
#                 image :
#             }
#         }
#
#     order :
#         {
#             order_id :
#             order_food : 'A', 'B', 'C'...
#             order_request :
#
#         }
# }