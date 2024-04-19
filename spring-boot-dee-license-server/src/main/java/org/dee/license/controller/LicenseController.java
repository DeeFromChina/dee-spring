package org.dee.license.controller;

import org.dee.license.properties.LicenseConfigurationProperties;
import org.dee.license.entity.GenerateLicense;
import org.dee.license.entity.LicenseCreatorParam;
import org.dee.license.license.LicenseCreator;
import org.dee.framework.http.WebResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/license")
public class LicenseController {

    @Resource
    private LicenseConfigurationProperties configuration;

    /**
     * 生成证书
     * @param dto 生成证书需要的参数，如：{"subject":"ccx-models","privateAlias":"privateKey","keyPass":"5T7Zz5Y0dJFcqTxvzkH5LDGJJSGMzQ","storePass":"3538cef8e7","licensePath":"C:/Users/zifangsky/Desktop/license.lic","privateKeysStorePath":"C:/Users/zifangsky/Desktop/privateKeys.keystore","issuedTime":"2018-04-26 14:48:12","expiryTime":"2018-12-31 00:00:00","consumerType":"User","consumerAmount":1,"description":"这是证书描述信息","licenseCheckModel":{"ipAddress":["192.168.245.1","10.0.5.22"],"macAddress":["00-50-56-C0-00-01","50-7B-9D-F9-18-41"],"cpuSerial":"BFEBFBFF000406E3","mainBoardSerial":"L1HF65E00X9"}}
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @RequestMapping(value = "/generateLicense", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public WebResponse<Map<String,Object>> generateLicense(@RequestBody GenerateLicense dto) {

        LicenseCreatorParam param = new LicenseCreatorParam();
        putCreatorParamFromDTO(param, dto);
        putCreatorParamFromConfig(param);

        LicenseCreator licenseCreator = new LicenseCreator(param);
        boolean result = licenseCreator.generateLicense();

        Map<String,Object> resultMap = new HashMap<>(2);
        if(result){
            resultMap.put("result","ok");
            resultMap.put("msg",param);
        }else{
            resultMap.put("result","error");
            resultMap.put("msg","证书文件生成失败！");
        }
        return WebResponse.success(resultMap);
    }

    private void putCreatorParamFromDTO(LicenseCreatorParam param, GenerateLicense dto) {
        param.setSubject(dto.getSubject());
        param.setIssuedTime(dto.getIssuedTime());
        param.setExpiryTime(dto.getExpiryTime());
        param.setConsumerType(dto.getConsumerType());
        param.setConsumerAmount(dto.getConsumerAmount());
        param.setDescription(dto.getDescription());
        param.setLicenseCheckModel(dto.getLicenseCheckModel());
    }

    private void putCreatorParamFromConfig(LicenseCreatorParam param) {
        param.setPrivateAlias(configuration.getPrivateAlias());
        param.setStorePass(configuration.getStorePass());
        param.setPrivateKeysStorePath(configuration.getPrivateKeysStorePath());
        param.setKeyPass(configuration.getKeyPass());

        String licensePath = configuration.getLicensePath() + "certfile-" + param.getSubject() + ".cer";
        param.setLicensePath(licensePath);
    }

}
