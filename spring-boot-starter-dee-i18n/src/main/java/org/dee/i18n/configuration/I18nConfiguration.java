package org.dee.i18n.configuration;

import org.dee.i18n.properties.I18nConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.annotation.Resource;
import java.util.Locale;

@Configuration
public class I18nConfiguration {

    @Resource
    private I18nConfigurationProperties properties;

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:" + properties.getBasename());
        messageSource.setDefaultEncoding(properties.getEncoding());
        messageSource.setCacheSeconds(properties.getCacheDuration()); // 设置缓存刷新时间，根据需要调整
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE); // 设置默认 Locale
        return localeResolver;
    }

}
