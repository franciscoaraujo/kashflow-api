package br.com.bitewisebytes.kashflowapi.domain.validation;

import br.com.bitewisebytes.kashflowapi.domain.exceptions.UserException;
import br.com.bitewisebytes.kashflowapi.dto.request.UserResquestDto;

import java.math.BigDecimal;

public class UserRequestValidation {

    public static void validate(UserResquestDto userRequestDto) {
        if (userRequestDto == null) {
            throw new UserException("UserRequestDto cannot be null", "INVALID_REQUEST");
        }

        if (userRequestDto.name() == null || userRequestDto.name().isBlank()) {
            throw new UserException("Name cannot be null or blank", "INVALID_NAME");
        }

        if (userRequestDto.email() == null || userRequestDto.email().isBlank()) {
            throw new UserException("Email cannot be null or blank", "INVALID_EMAIL");
        }

        if (userRequestDto.document() == null || userRequestDto.document().isBlank()) {
            throw new UserException("Document number cannot be null or blank", "INVALID_DOCUMENT_NUMBER");
        }
    }
}