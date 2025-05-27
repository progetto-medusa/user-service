package com.progettomedusa.user_service.model.exception;

import com.progettomedusa.user_service.model.response.RestResponse;
import lombok.*;
import com.progettomedusa.user_service.model.response.Error;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommonErrorResponse extends RestResponse {
    private Error error;
}
