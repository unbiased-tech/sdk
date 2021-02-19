# -*- coding: utf-8 -*-


import json
import time
import requests
from utils import get_signature


def req_black_list_api(accessKey, secretKey, formdata, url):
    # 10位秒级时间戳
    timestamp = str(int(time.time()))
    method = 'POST'

    sig = get_signature(accessKey, secretKey, timestamp,formdata, url, method)

    headers = {
        'signature': sig,
        'content-type': 'application/json;charset=UTF-8',
        'eventTime': timestamp,  # 10位时间戳
        'accessKey': accessKey,
    }

    resp = requests.post(url=url, headers=headers, data=json.dumps(formdata))

    print(resp.text)

if __name__=='__main__':
    url = 'http://ip:port/api/v1/attentionList'

    accessKey = 'ak'
    secretKey = 'sk'

    formdata = {
        'partnerName': '',
        'appName': '',
        'panCode': '',
        'aadhaar': '',
        'mobile': '',
        'event': '',
        'name': '',
        'eventTime': str(int(time.time())),
        'returnType': 'details'
    }
    req_black_list_api(accessKey, secretKey,formdata, url)
