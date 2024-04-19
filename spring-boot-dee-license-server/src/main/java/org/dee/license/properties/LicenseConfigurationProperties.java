package org.dee.license.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Data
@Configuration
@ConfigurationProperties(prefix = "license")
public class LicenseConfigurationProperties implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 密钥别称
     */
    private String privateAlias;

    /**
     * 访问秘钥库的密码
     */
    private String storePass;

    /**
     * 密钥库存储路径
     */
    private String privateKeysStorePath;

    /**
     * 密钥密码（需要妥善保管，不能让使用者知道）
     */
    private String keyPass;

    /**
     * 证书生成路径
     */
    private String licensePath;

}
