package com.progettomedusa.user_service.service;


//import com.progettomedusa.auth_service.model.request.AuthRequest;
import com.progettomedusa.user_service.config.MailServiceProperties;
import com.progettomedusa.user_service.model.converter.UserConverter;
import com.progettomedusa.user_service.model.dto.UserDTO;
import com.progettomedusa.user_service.model.request.CreateUserRequest;
import com.progettomedusa.user_service.model.request.ResetPasswordRequest;
import com.progettomedusa.user_service.model.response.CreateRequestResponse;
import com.progettomedusa.user_service.util.Tools;
import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progettomedusa.user_service.config.OkHttpClientCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.progettomedusa.user_service.model.response.ResetPasswordResponse;
//import com.progettomedusa.auth_service.model.response.LoginResponse;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalCallingService {

    private final MailServiceProperties mailServiceProperties;
    private final ObjectMapper objectMapper;
    private final OkHttpClientCustom okHttpClientCustom;
    private final Tools tools;
    private final UserConverter userConverter;


    public ResetPasswordResponse retrieveUserData(ResetPasswordRequest resetPasswordRequest, String appKeyHeader) throws IOException {
        String url = String.join("", mailServiceProperties.getUrl(), "/mail-service/reset-password");

        String json = objectMapper.writeValueAsString(resetPasswordRequest);
        RequestBody requestBody = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-APP-KEY", appKeyHeader)
                .post(requestBody)
                .build();

        Response response = okHttpClientCustom.okHttpClient().newCall(request).execute();
        ResetPasswordResponse resetPasswordResponse = new ResetPasswordResponse();
        if (response.isSuccessful()) {
            resetPasswordResponse.setMessage("email inviata con successo");
            resetPasswordResponse.setDomain("user-service");
            resetPasswordResponse.setTimestamp(tools.getInstant());
        }else{
            resetPasswordResponse.setMessage("ERRORE: email non recapitata");
            resetPasswordResponse.setDomain("user-service");
            resetPasswordResponse.setTimestamp(tools.getInstant());
        }
        return resetPasswordResponse;
    }

    public CreateRequestResponse createConfirmUser(UserDTO userDTO) throws IOException {
        String  url = String.join("", mailServiceProperties.getUrl(), "/mail-service/new-member-confirm");

        String json = objectMapper.writeValueAsString(userConverter.userDtoToUserRequestForm(userDTO));
        RequestBody requestBody = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-APP-KEY", userDTO.getApplicationId())
                .post(requestBody)
                .build();
        Response response = okHttpClientCustom.okHttpClient().newCall(request).execute();
        CreateRequestResponse createRequestResponse = new CreateRequestResponse();
        if (response.isSuccessful()) {
            createRequestResponse.setMessage("email inviata con successo");
            createRequestResponse.setDomain("user-service");
            createRequestResponse.setTimestamp(tools.getInstant());
        }else{
            createRequestResponse.setMessage("ERRORE: email non recapitata");
            createRequestResponse.setDomain("user-service");
            createRequestResponse.setTimestamp(tools.getInstant());
        }
        return createRequestResponse;
    }


}


