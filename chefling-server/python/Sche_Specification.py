'''
Scheduling Specification
opensource, for making sence of Scheduling Algorithm
'''


#TODO
#Event Interrupt
#Timer


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





#Input Json
# {
#     menu :
#         {
#             cooks :{ (cook은 position과 같은 의미로 봐도 무방할듯!)
#                 cook1(고유 key) :{
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
#                 food1(고유키) :{
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