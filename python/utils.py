# -*- coding: utf-8 -*-

import json
from sign import hmac_sha1_sig
from urllib.parse import urlparse


# 删除参数为空的值
def del_dict_value_is_none(data):
    if not data:
        return data
    for key in list(data.keys()):
        if not data.get(key) or len(data.get(key)) == 0:
            del data[key]
        if isinstance(data.get(key), list):
            data[key] = json.loads(json.dumps(data.get(key)).replace(' ', ''))

    return data


def params_sorted(formdata):
    if not formdata:
        return None
    p = {k: formdata[k] for k in sorted(formdata)}  # 对键值对排序（顺序）
    p = json.dumps(p, separators=(',', ':'), ensure_ascii=False)  # 转JSON字符串。不允许,和:后有空格

    return p


def item_generator(json_input):
    if isinstance(json_input, dict):
        for k, v in json_input.items():
            if isinstance(v, str):
                yield '%s=%s' % (k, v)
            else:
                yield from item_generator(v)
    elif isinstance(json_input, list):
        for item in json_input:
            if isinstance(item, dict):
                yield from item_generator(item)
            else:
                yield item


def get_signature(accessKey, secretKey, timestamp, formdata = None, url = None, method='POST'):

    url_path = urlparse(url).path

    secretKey = '%s&' % secretKey

    params = {
        'accessKey': accessKey,
        'eventTime': timestamp
    }

    return hmac_sha1_sig(method, url_path, params, secretKey)
