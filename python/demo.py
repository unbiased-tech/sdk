# -*- coding: utf-8 -*-
'''
@date: 2021/1/4 11:34
@desc: 
'''


import json
import time
import requests
from utils import get_signature


def req_black_list_api(accessKey, secretKey, formdata, url, apiKey):
    # 10位秒级时间戳
    timestamp = str(int(time.time()))
    method = 'POST'

    sig = get_signature(accessKey, secretKey, timestamp,formdata, url, method)

    headers = {
        'signature': sig,
        'content-type': 'application/json;charset=UTF-8',
        'u-date': timestamp,  # 10位时间戳
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
        'parCode': '',
        'aadhaar': '',
        'mobile': '',
        'event': '',
        'name': '',
        'eventTime': str(int(time.time())),
        'returnType': 'details'
    }
    req_black_list_api(accessKey, secretKey,formdata, url, apiKey)
