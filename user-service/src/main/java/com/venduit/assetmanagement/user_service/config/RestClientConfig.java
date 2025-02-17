package com.venduit.assetmanagement.user_service.config;


import com.venduit.assetmanagement.user_service.client.GatewayClient;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    @Value("${gatewayclient-url}")
    private String userClientUrl;

    private final ObservationRegistry observationRegistry;

    @Bean
    public GatewayClient gatewayClient() {
        RestClient restClient = RestClient.builder()
                .observationRegistry(observationRegistry)
                .baseUrl(userClientUrl)
                .build();

        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(GatewayClient.class);
    }

}
