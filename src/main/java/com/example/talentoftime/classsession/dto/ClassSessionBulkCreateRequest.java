package com.example.talentoftime.classsession.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record ClassSessionBulkCreateRequest(

        @NotEmpty
        List<ClassSessionCreateRequest> sessions
) {
}
