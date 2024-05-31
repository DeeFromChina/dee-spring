package org.dee.i18n.utils;

import org.dee.context.SpringContext;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class I18nMessageUtil {

    public static ThreadLocal<String> t = new ThreadLocal<>();

    public static void setLangage(String langage) {
        t.set(langage);
    }

    public static String getLangage() {
        return t.get();
    }

    public static Locale getLocale() {
        return t.get() == null ? Locale.SIMPLIFIED_CHINESE : new Locale(t.get());
    }

    public static String getMessage(String code) {
        MessageSource messageSource = SpringContext.getBean(MessageSource.class);
        return messageSource.getMessage(code, null, I18nMessageUtil.getLocale());
    }

}
