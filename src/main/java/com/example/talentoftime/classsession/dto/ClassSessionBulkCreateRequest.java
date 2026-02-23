package com.example.talentoftime.classsession.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClassSessionBulkCreateRequest {

    @NotEmpty
    @Valid
    private List<ClassSessionCreateRequest> sessions;
}
