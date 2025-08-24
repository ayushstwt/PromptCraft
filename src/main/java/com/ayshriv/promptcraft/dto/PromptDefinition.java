package com.ayshriv.promptcraft.dto;

public record PromptDefinition(
        String name,
        String description,
        String system,
        String userTemplate
) {}
