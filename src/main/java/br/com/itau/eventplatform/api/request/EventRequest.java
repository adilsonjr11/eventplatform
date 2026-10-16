package br.com.itau.eventplatform.api.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record EventRequest(

        @NotBlank
        String transactionId,

        @NotBlank
        String correlationId,

        @NotBlank
        String accountFrom,

        @NotBlank
        String accountTo,

        @NotNull
        @DecimalMin("0.01")
        BigDecimal amount

) {
}