package com.springIntegration.services.exception;

import com.springIntegration.services.dto.Error;

import java.util.List;

public class GenericException extends Exception{

    List<Error> errorList;

}
