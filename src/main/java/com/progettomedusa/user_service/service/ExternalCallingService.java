package com.progettomedusa.user_service.service;


import com.progettomedusa.user_service.config.MailServiceProperties;
import com.progettomedusa.user_service.model.converter.PMUserConverter;
import com.progettomedusa.user_service.model.dto.UserDTO;
import com.progettomedusa.user_service.model.request.NewPasswordRequest;
import com.progettomedusa.user_service.model.request.ResetPasswordEmailRequest;
import com.progettomedusa.user_service.model.response.CreatePMUserResponse;
import com.progettomedusa.user_service.model.response.NewPasswordResponse;
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


@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalCallingService {

    private final MailServiceProperties mailServiceProperties;
    private final ObjectMapper objectMapper;
    private final OkHttpClientCustom okHttpClientCustom;
    private final Tools tools;
    private final PMUserConverter pmUserConverter;

    public CreatePMUserResponse createConfirmUser(UserDTO userDTO) throws IOException {
        String  url = String.join("", mailServiceProperties.getUrl(), "/mail-service/new-member-confirm");

        String json = objectMapper.writeValueAsString(pmUserConverter.userDtoToUserRequestForm(userDTO));
        RequestBody requestBody = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-APP-KEY", userDTO.getApplicationId())
                .post(requestBody)
                .build();
        Response response = okHttpClientCustom.okHttpClient().newCall(request).execute();
        CreatePMUserResponse createPMUserResponse = new CreatePMUserResponse();
        if (response.isSuccessful()) {
            createPMUserResponse.setMessage("email inviata con successo");
            createPMUserResponse.setDomain("user-service");
            createPMUserResponse.setTimestamp(tools.getInstant());
        }else{
            createPMUserResponse.setMessage("ERRORE: email non recapitata");
            createPMUserResponse.setDomain("user-service");
            createPMUserResponse.setTimestamp(tools.getInstant());
        }
        return createPMUserResponse;
    }

    public NewPasswordResponse retrieveUserData(ResetPasswordEmailRequest resetPasswordEmailRequest, String appKeyHeader) throws IOException {
        String url = String.join("", mailServiceProperties.getUrl(), "/mail-service/reset-password");

        String json = objectMapper.writeValueAsString(resetPasswordEmailRequest);
        RequestBody requestBody = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-APP-KEY", appKeyHeader)
                .post(requestBody)
                .build();

        Response response = okHttpClientCustom.okHttpClient().newCall(request).execute();
        NewPasswordResponse newPasswordResponse = new NewPasswordResponse();
        if (response.isSuccessful()) {
            newPasswordResponse.setMessage("email inviata con successo");
            newPasswordResponse.setDomain("user-service");
            newPasswordResponse.setTimestamp(tools.getInstant());
        }else{
            newPasswordResponse.setMessage("ERRORE: email non recapitata");
            newPasswordResponse.setDomain("user-service");
            newPasswordResponse.setTimestamp(tools.getInstant());
        }
        return newPasswordResponse;
    }


}


