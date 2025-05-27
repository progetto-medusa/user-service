package com.progettomedusa.user_service.model.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DomainMsg {

    USER_SERVICE_TECHNICAL("UserServiceTechnicals"),
    MICROSERVICE_FUNCTIONAL("MicroServiceFunctional");

    private final String name;
}

