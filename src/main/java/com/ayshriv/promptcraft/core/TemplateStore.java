package com.ayshriv.promptcraft.core;

import com.ayshriv.promptcraft.dto.PromptDefinition;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TemplateStore {

    private final Map<String, PromptDefinition> templates = new ConcurrentHashMap<>();

    @PostConstruct
    void seed() {
        templates.put("twitter-hooks", new PromptDefinition(
                "twitter-hooks",
                "Generate 5 short, scroll-stopping hooks",
                "You are a concise, high-conversion copywriter.",
                """
                Generate 5 short hooks for a tweet thread about {topic}.
                Style: {style}. Keep each under 100 characters.
                Output as a numbered list only.
                """
        ));

        templates.put("summarize", new PromptDefinition(
                "summarize",
                "TL;DR in bullet points",
                "You are an expert technical summarizer.",
                """
                Summarize the following content into 5 crisp bullets for {audience}:
                ---
                {content}
                ---
                """
        ));

        templates.put("rewrite-tone", new PromptDefinition(
                "rewrite-tone",
                "Rewrite text in a tone and length",
                "You are a helpful rewriting assistant.",
                """
                Rewrite the text in a {tone} tone and keep it around {words} words.
                Text:
                {text}
                """
        ));
    }

    public List<PromptDefinition> list() {
        return new ArrayList<>(templates.values());
    }

    public Optional<PromptDefinition> get(String name) {
        return Optional.ofNullable(templates.get(name));
    }

    public PromptDefinition upsert(PromptDefinition def) {
        templates.put(def.name(), def);
        return def;
    }
}