package com.gdou.www.gdouaqualib.utils;

import java.util.Map;

/**
 * Created by Veyron on 2017/1/20.
 * Function：发送消息类
 */
public class MessageEvent {
    public Map<String,Object> mMap;

    public MessageEvent(Map<String,Object> Map) {
        this.mMap = Map;
    }
}