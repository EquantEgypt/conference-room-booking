package org.orange.oie.internship2025.conferenceroombooking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class ErrorCode {
    private HttpStatus httpStatus;
    private String errorMessage;
}

