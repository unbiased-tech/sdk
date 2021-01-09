# -*- coding: utf-8 -*-
"""
-------------------------------------------------
   File Name:    demo
   Description :
   date:         2021/1/9
-------------------------------------------------
"""


import json
import time
import requests
from utils import get_signature


def req_client(accessKey, secretKey, url, formdata, apiKey):
    # 10位秒级时间戳
    timestamp = str(int(time.time()))

    method = 'POST'

    # 获取签名
    sig = get_signature(accessKey, secretKey, timestamp, formdata, url, method)

    headers = {
        'signature': sig,
        'content-type': 'application/json;charset=UTF-8',
        'u-date': timestamp,
        'access-key': accessKey,
        'api-key': apiKey
    }

    resp = requests.post(url=url, headers=headers, data=json.dumps(formdata))

    print(resp.text)


if __name__=='__main__':

    url = 'http://localhost:8095/api/v1/blacklist'

    accessKey = 'your access key'
    secretKey = 'your secret key'
    apiKey = 'api key'

    formdata = {
        'event': '',
        'pancode': '',
        'aadhaar': '',
        'mobile': '7543862819',
        'name': '',
        'eventTime': int(time.time()),
        'returnType': 'details'
    }

    req_client(accessKey, secretKey, url, formdata, apiKey)


