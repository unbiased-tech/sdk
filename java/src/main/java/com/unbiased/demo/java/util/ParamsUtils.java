/*
 * @(#) ParamsUtils.java 2021-01-14
 *
 * Copyright 2021 NetEase.com, Inc. All rights reserved.
 */

package com.unbiased.demo.java.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @version 2021-01-14
 */
public class ParamsUtils {
    public static String readJson(Object obj, List<String> tmpList) {
        //如果objJson为json数组
        if(obj instanceof JSONArray) {
            JSONArray objArray = (JSONArray)obj;
            for (int i = 0; i < objArray.size(); i++) {
                if (objArray.get(i) instanceof JSONObject) {
                    readJson(objArray.get(i), tmpList);
                } else if (objArray.get(i) instanceof String) {
                    tmpList.add(objArray.get(i).toString());
                }
            }
        } else if(obj instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject)obj;
            Iterator it = jsonObject.keySet().iterator();
            while(it.hasNext()) {
                String key = it.next().toString();
                Object value = jsonObject.get(key);
                if(value instanceof JSONArray) {
                    JSONArray objArray = (JSONArray)value;
                    readJson(objArray, tmpList);
                } else if(value instanceof JSONObject){
                    readJson((JSONObject)value, tmpList);
                } else { //如果value是基本类型
                    if (StringUtils.isNotBlank(value.toString())) {
                        String tmpStr = key + "=" + value.toString();
                        tmpList.add(tmpStr);
                    }
                }
            }
        } else { //objJson为基本类型
            System.out.println("基本类型的参数: {}" + obj.toString());
        }
        Collections.sort(tmpList);

        // 去重后组合成字符串
        return tmpList.stream().distinct().collect(Collectors.joining("&"));
    }


    public static String formatJson(String json_src) {
        try {
            json_src = StringEscapeUtils.unescapeJavaScript(json_src);
            json_src = json_src.replace("\"[", "[");
            json_src = json_src.replace("]\"", "]");
            json_src = json_src.replace("\"{", "{");
            json_src = json_src.replace("}\"", "}");
            return json_src;
        }catch (Exception e) {
            System.out.println("format json error," +  e);
            return json_src;
        }
    }


    public static String  handlerParams(Map<String,String> params) {
        JSONObject paramObj = JSON.parseObject(JSON.toJSONString(params));
        String postStr = formatJson(paramObj.toJSONString());
        // 调用递归，解析出所有json的k-v，拼接成字符串
        List<String> tmpList = new ArrayList<>();

        return readJson(JSON.parseObject(postStr), tmpList);
    }

}
