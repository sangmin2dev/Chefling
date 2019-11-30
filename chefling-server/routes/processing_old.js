// a[0] - food: cate, name, 요리시간, 음식장르(app,mai,des)(초기화)
// a[1] - cook별(요리사이름, 포지션, 역량, 현재 쿸리스트(None), 쿡시간(None), 블락유무(None)(쿡큐) 
// 각 요리는[오더아이디, food_id, 카테고리,[ 음식이름, 소요시간], 타입]
// a[2] - order:[ [order_id, food배열(name, ID)] ,[  ], [None]  ](오더)
// a[3] - servertime
// a[4] - food 단위의 ordered list(오더 아이디 / food_id / cate / [음식이름, 소요시간] / type / 우선순위(null) / 대기가능시간(null) / 대기시간(null) / 이벤트유무(null) / andthen) (오더큐)
// 초기화 이후 파이썬 한번 거치고 난 결과값과 현재 order 리스트 와의 관계
// a[5] - time per menu  [...](메뉴이름 / 시간)
// a[6] - time per food [...] (음식이름 / 음식아이디 / 시간)