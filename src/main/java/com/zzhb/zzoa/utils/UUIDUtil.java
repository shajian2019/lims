package com.zzhb.zzoa.utils;

import java.util.UUID;

public class UUIDUtil {

    public static String creatUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}