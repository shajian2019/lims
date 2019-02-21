package com.zzhb.utils;

import com.github.bingoohuang.patchca.color.RandomColorFactory;
import com.github.bingoohuang.patchca.custom.ConfigurableCaptchaService;
import com.github.bingoohuang.patchca.word.RandomWordFactory;

public class CaptchaFactory {

    private static ConfigurableCaptchaService cs = new ConfigurableCaptchaService();

    static {
        cs.setColorFactory(new RandomColorFactory());
        RandomWordFactory wf = new RandomWordFactory();
        wf.setCharacters("123456789ABCDEFGHKLMNPQRSVWXYZ");
        wf.setMaxLength(4);
        wf.setMinLength(4);
        cs.setWordFactory(wf);
    }

    public static ConfigurableCaptchaService getInstance() {
        return cs;
    }
}