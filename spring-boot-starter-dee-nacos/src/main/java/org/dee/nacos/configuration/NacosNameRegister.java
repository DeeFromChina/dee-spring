package org.dee.nacos.configuration;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.util.Properties;

@Configuration
public class NacosNameRegister {

    @Value("${server.port}")
    private Integer port;

    @Value("${spring.cloud.nacos.config.server-addr}")
    private String nacosPath;

    @Resource
    private NacosProxyConfiguration nacosProxyConfiguration;

    @Bean
    public void nacosProxyNameRegister() {
        try{
            if(nacosProxyConfiguration == null || !nacosProxyConfiguration.getEnabled()) {
                return;
            }

            InetAddress inet = InetAddress.getLocalHost();
            for(NacosProxyConfiguration.Config config : nacosProxyConfiguration.getConfigs()){
                // 创建 Nacos 命名服务实例
                Properties properties = new Properties();
                properties.put("serverAddr", nacosPath);
                NamingService naming = NacosFactory.createNamingService(properties);

                Instance instance = new Instance();
                instance.setIp(inet.getHostAddress());
                instance.setPort(port);

                // 注册服务实例
                naming.registerInstance(config.getServerName(), config.getGroup(), instance);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
