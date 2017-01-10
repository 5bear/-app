package com.springapp.classes;

import java.util.UUID;

/**
 * Created by 11369 on 2016/10/7.
 */
public class Guid {
    private static UUID uid;
    public static String getGuid(){
        uid = UUID.randomUUID();
        return uid.toString().replaceAll("-", "");
    }
}
