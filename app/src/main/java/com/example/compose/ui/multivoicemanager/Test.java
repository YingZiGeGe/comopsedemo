package com.example.compose.ui.multivoicemanager;

/**
 * @author : YingYing Zhang
 * @e-mail : 540108843@qq.com
 * @time : 2023-02-07
 * @desc :
 */
public class Test {

    public static class IEventCenter {
        public Object sendEvent(int event, Object... args) {
            return args;
        }
    }
}


interface Receiver {
    void onEvent(String event, Object... args);
}


