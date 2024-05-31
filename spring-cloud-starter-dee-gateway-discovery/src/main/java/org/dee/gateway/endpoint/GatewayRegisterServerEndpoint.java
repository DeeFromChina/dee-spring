package org.dee.gateway.endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayRegisterServerEndpoint {

    @GetMapping("/testConnection")
    public String testConnection() {
        return "success";
    }

}
