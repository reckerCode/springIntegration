package com.springIntegration.services.dto;


import com.springIntegration.services.enums.ErrorCodes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Error {

    ErrorCodes errorCode;
    String errorTitle;
    String errorDetails;
    String errorSourceServiceId;
    String errorSourceServiceName;

}
