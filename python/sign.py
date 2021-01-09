# -*- coding: utf-8 -*-
"""
-------------------------------------------------
   File Name:    sign
   Description :
   date:         2021/1/9
-------------------------------------------------
"""

import urllib
from urllib.parse import quote
import binascii
import hashlib
import hmac


def mk_soucrce(method, url_path, params):
    str_params = quote("&".join(k + "=" + str(params[k]) for k in sorted(params.keys())), '')

    source = '%s&%s&%s' % (
        method.upper(),
        quote(url_path, ''),
        str_params
    )

    return source


def hmac_sha1_sig(method, url_path, params, secret):
    source = mk_soucrce(method, url_path, params)
    print('source:%s' % source)
    secret_bytes = bytes(secret, 'latin-1')
    source_bytes = bytes(source, 'latin-1')
    hashed = hmac.new(secret_bytes, source_bytes, hashlib.sha1)
    return binascii.b2a_base64(hashed.digest())[:-1]


def main():
    method = 'GET'
    url_path = '/v1/blacklist'
    params = {
        'appKey': 'JIFHUS84J2K3O49080',
        'mobile': '19900992233',
    }

    secret = '9b7adbd2ee5206224135&'

    print(mk_soucrce(method, url_path, params))
    print(hmac_sha1_sig(method, url_path, params, secret))


if __name__ == '__main__':
    main()