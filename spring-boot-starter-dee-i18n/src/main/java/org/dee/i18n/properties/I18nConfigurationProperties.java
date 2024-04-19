package org.dee.i18n.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "org.dee.i18n")
public class I18nConfigurationProperties {

    private String basename = "i18n/messages";

    private Integer cacheDuration = 60 * 60 * 3;

    private String encoding = "UTF-8";

}
