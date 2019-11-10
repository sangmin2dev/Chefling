#!/usr/bin/env python
# coding=utf8
#-*- coding:utf-8 -*-


import sys
import json


a = json.loads(sys.argv[1])
for i in a:
    print(i)

