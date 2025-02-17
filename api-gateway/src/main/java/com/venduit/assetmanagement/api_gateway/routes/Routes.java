package com.venduit.assetmanagement.api_gateway.routes;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;

@Configuration
public class Routes {

    @Bean
    public RouterFunction<ServerResponse> userManagementServiceRoute(){
        return GatewayRouterFunctions.route("user_service")
                .route(RequestPredicates.path("/api/v1/user"), HandlerFunctions.http("http://localhost:8080"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userManagementServiceSwaggerRoute(){
        return GatewayRouterFunctions.route("user_service_swagger")
                .route(RequestPredicates.path("/aggregate/user-service/v3/api-docs"), HandlerFunctions.http("http://localhost:8080"))
                .filter(setPath("/v3/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> transactionServiceRoute(){
        return GatewayRouterFunctions.route("transaction_service")
                .route(RequestPredicates.path("/api/v1/transaction"), HandlerFunctions.http("http://localhost:8083"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> transactionServiceSwaggerRoute(){
        return GatewayRouterFunctions.route("transaction_service_swagger")
                .route(RequestPredicates.path("/aggregate/transaction-service/v3/api-docs"), HandlerFunctions.http("http://localhost:8083"))
                .filter(setPath("/v3/api-docs"))
                .build();
    }


    @Bean
    public RouterFunction<ServerResponse> savingServiceRoute(){
        return GatewayRouterFunctions.route("saving_service")
                .route(RequestPredicates.path("/api/v1/saving"), HandlerFunctions.http("http://localhost:8082"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> savingServiceSwaggerRoute(){
        return GatewayRouterFunctions.route("saving_service_swagger")
                .route(RequestPredicates.path("/aggregate/saving-service/v3/api-docs"), HandlerFunctions.http("http://localhost:8082"))
                .filter(setPath("/v3/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> investmentServiceRoute(){
        return GatewayRouterFunctions.route("investment_service")
                .route(RequestPredicates.path("/api/v1/investment"), HandlerFunctions.http("http://localhost:8081"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> investmentServiceSwaggerRoute(){
        return GatewayRouterFunctions.route("investment_service_swagger")
                .route(RequestPredicates.path("/aggregate/investment-service/v3/api-docs"), HandlerFunctions.http("http://localhost:8081"))
                .filter(setPath("/v3/api-docs"))
                .build();
    }
}
