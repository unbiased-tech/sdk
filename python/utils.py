# -*- coding: utf-8 -*-
"""
-------------------------------------------------
   File Name:    utils
   Description :
   date:         2021/1/9
-------------------------------------------------
"""
from sign import hmac_sha1_sig
from urllib.parse import urlparse


# 删除参数为空的值
def del_dict_value_is_none(data):
    for key in list(data.keys()):
        if not data.get(key):
            del data[key]
    return data


def get_signature(accessKey, secretKey, timestamp, formdata, url, method='POST'):

    url_path = urlparse(url).path

    secretKey = '%s&' % secretKey

    params = {
        'accessKey': accessKey,
        'uDate': timestamp
    }

    params.update(formdata)

    params = del_dict_value_is_none(params)

    return hmac_sha1_sig(method, url_path, params, secretKey)