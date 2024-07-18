package org.dee.common.service.impl;

import org.dee.rpc.RPCRequest;
import org.dee.rpc.RPCResult;
import org.dee.agent.utils.RpcResultUtil;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;

public class WebClientImpl {

    @Resource
    private WebClient.Builder webClientBuilder;

    protected <T> RPCResult<List<T>> doPostRequestReturnList(String baseUrl, String uri, RPCRequest param, Class<T> tClass) {
        RPCResult<List<LinkedHashMap>> rpcResult = webClientBuilder
                .baseUrl(baseUrl)
                .build()
                .method(HttpMethod.POST)
                .uri(uri)
                .headers(headers -> headers.setContentType(MediaType.APPLICATION_JSON))
                .bodyValue(param)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<RPCResult<List<LinkedHashMap>>>(){})
                .block();
        if (!RpcResultUtil.restRequestIsSuccess(rpcResult)) {
            throw new RuntimeException(rpcResult.getMessage());
        }
        RPCResult result = new RPCResult();
        result.setBody(rpcResult.getBody());
        return result;
    }

    protected <T> RPCResult<T> doPostRequestReturnEntity(String baseUrl, String uri, RPCRequest param, Class<T> target) {
        Mono<RPCResult<T>> rpcResult = webClientBuilder
                .baseUrl(baseUrl)
                .build()
                .method(HttpMethod.POST)
                .uri(uri)
                .headers(headers -> headers.setContentType(MediaType.APPLICATION_JSON))
                .bodyValue(param)
                .retrieve().bodyToMono(new ParameterizedTypeReference<RPCResult<T>>(){});
        return rpcResult.block();
    }

    protected void doGetRequest(String baseUrl, String uri, RPCRequest param) {

    }
}
