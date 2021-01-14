package com.unbiased.demo.java;

import com.alibaba.fastjson.JSONObject;
import com.unbiased.demo.java.util.SigUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;



public class JavaApplication {


    /**
     * 请求示例
     * @param args
     */
    public static void main(String[] args) {
        String accessKey = "your access key";
        String secretKey = "your secret key";
        String apiKey = "api key";


        String url = "http://localhost:8095/api/v1/blacklist";


        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);

        Map<String, String> params = new HashMap<>();
        params.put("event", "");
        params.put("panCode", "");
        params.put("aadhaar","");
        params.put("mobile","123456");
        params.put("name","");
        params.put("eventTime",timestamp);
        params.put("returnType", "details");


        // 计算签名
        String sig = SigUtils.getSignature("POST", url, params, accessKey, secretKey, timestamp);

        // 请求接口
        RestTemplate template = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("signature", sig);
        headers.add("content-type", "application/json;charset=UTF-8");
        headers.add("u-date", timestamp);
        headers.add("access-key", accessKey);
        headers.add("api-key",apiKey);


        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = template.postForEntity(url, httpEntity, String.class);
        System.out.println("result--->" + response.getBody());


    }

}
