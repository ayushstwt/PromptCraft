package com.ayshriv.promptcraft.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public record GenerateRequest(
        @NotBlank String template,
        Map<String, Object> variables,
        String system,
        String model
) {}