package com.unbiased.demo.java;

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
        String accessKey = "xxx";
        String secretKey = "xxx";


        String url = "http://localhost:8095/api/v1/attentionList";


        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);

        Map<String, String> params = new HashMap<>();
        params.put("partnewName","");
        params.put("appName","");
        params.put("event", "");
        params.put("panCode", "");
        params.put("aadhaar","");
        params.put("mobile","7543862819");
        params.put("name","");
        params.put("eventTime",timestamp);
        params.put("returnType", "details");


        // 计算签名
        String sig = SigUtils.getSignature("POST", url, accessKey, secretKey, timestamp);

        // 请求接口
        RestTemplate template = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("signature", sig);
        headers.add("content-type", "application/json;charset=UTF-8");
        headers.add("eventTime", timestamp);
        headers.add("accessKey", accessKey);


        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = template.postForEntity(url, httpEntity, String.class);
        System.out.println("result--->" + response.getBody());


    }

}
