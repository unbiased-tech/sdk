package com.unbiased.demo.java.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @description 计算签名
 **/
public class SigUtils {

    /**
     * 编码方式
     */
    private static final String CONTENT_CHARSET = "UTF-8";

    /**
     * HMAC加密算法
     */
    private static final String HMAC_ALGORITHM = "HmacSHA1";


    /**
     * url 编码
     * @param input url
     * @return
     */
    private static String encodeUrl(String input) {
        try{
            return URLEncoder.encode(input, CONTENT_CHARSET).replace("+", "%20").replace("*", "%2A");
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 计算签名
     * @param method 方法类型
     * @param url_path url path
     * @param params 参数
     * @param secret 密钥
     * @return
     */
    private static String createSig(String method, String url_path, Map<String, String> params, String secret) {
        String sig = null;
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);

            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(CONTENT_CHARSET), mac.getAlgorithm());

            mac.init(secretKey);

            String mk = makeSource(method, url_path, params);

            byte[] hash = mac.doFinal(mk.getBytes(CONTENT_CHARSET));

            // base64
            sig = new String(Base64Coder.encode(hash));
        } catch(NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return sig;
    }


    /**
     * 组合计算签名所需的字符串
     * @param method 请求方法
     * @param url_path url path
     * @param params 请求参数
     * @return
     */
    private static String makeSource(String method, String url_path, Map<String, String> params) {
        Object[] keys = params.keySet().toArray();

        Arrays.sort(keys);

        StringBuilder buffer = new StringBuilder(128);

        buffer.append(method.toUpperCase()).append("&").append(encodeUrl(url_path)).append("&");

        StringBuilder buffer2= new StringBuilder();

        for(int i=0; i<keys.length; i++) {
            buffer2.append(keys[i]).append("=").append(params.get(keys[i]));

            if (i!=keys.length-1) {
                buffer2.append("&");
            }
        }

        buffer.append(encodeUrl(buffer2.toString()));

        return buffer.toString();
    }


    /**
     * 删除value为空的Map数据
     * @param param 请求参数
     * @return
     */
    private static Map<String, String> delValNullInMap(Map<String, String> param) {
        try {
            Iterator<String> it = param.keySet().iterator();
            while (it.hasNext()) {
                Object ele = it.next();
                if (param.get(ele).toString().trim().equals("")) {
                    it.remove();
                }
            }
            return param;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取url path
     * @param url url
     * @return
     */
    private static String getUrlPath(String url) {
        try {
            URL u = new URL(url);
            return u.getPath();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getUrlQueryParams(String url) {
        try {
            URL u = new URL(url);
            return u.getQuery();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取签名
     * @param method 请求方法
     * @param url 请求url
     * @param params 请求参数
     * @param accessKey 用户accessKey
     * @param secretKey 用户密钥
     * @param uDate 时间戳
     * @return
     */
    public static String getSignature(String method, String url, Map<String,String> params, String accessKey, String secretKey, String uDate) {

        String urlPath = getUrlPath(url);
        Map<String,String> fData = new HashMap<>();
        fData.put("uDate", uDate);
        fData.put("accessKey", accessKey);

        method = method.toUpperCase();
        if ("POST".equals(method)) {
            params = delValNullInMap(params);

            String formData = ParamsUtils.handlerParams(params);
            if (StringUtils.isNotBlank(formData)) {
                fData.put("formData", formData);
            }
        } else if ("GET".equals(method)) {
            fData.put("formData", getUrlQueryParams(url));
        }

        return createSig(method,urlPath, fData, secretKey+ "&");
    }




}
