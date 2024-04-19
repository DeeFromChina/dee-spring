package org.dee.i18n.controller;

import lombok.extern.slf4j.Slf4j;
import org.dee.i18n.utils.I18nMessageUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpSession;
import java.util.Locale;

@RestController
@Slf4j
public class HomeController {

    /**
     * 国际化使用例子
     * @return
     */
    @GetMapping("/123")
    public String home() {
        String welcomeMessage = I18nMessageUtil.getMessage("welcome");
        log.info(welcomeMessage);
        return welcomeMessage;
    }

    /**
     * 变更国际化语言
     * @param lang
     * @param session
     * @return
     */
    @GetMapping("/change-locale")
    public String changeLocale(@RequestParam("lang") String lang, HttpSession session) {
        Locale locale = new Locale(lang);
        session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
        return "redirect:/"; // 重定向回主页或其他页面
    }

}
