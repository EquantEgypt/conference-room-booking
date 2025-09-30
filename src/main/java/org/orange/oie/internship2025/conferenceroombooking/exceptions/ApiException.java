package org.orange.oie.internship2025.conferenceroombooking.exceptions;

import org.orange.oie.internship2025.conferenceroombooking.enums.ApiError;

public class ApiException extends RuntimeException {

    private final ApiError apiError;

    public ApiException(ApiError apiError,String message) {
        super(message);
        this.apiError = apiError;
    }

    public ApiException(ApiError apiError) {
        super(apiError.getDefaultMessage());
        this.apiError = apiError;
    }

    public ApiError getApiError() {
        return apiError;
    }
}
