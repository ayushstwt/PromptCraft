package com.ayshriv.promptcraft.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateOrUpdateTemplateRequest(
        @NotBlank String name,
        String description,
        String system,
        @NotBlank String userTemplate
) {}