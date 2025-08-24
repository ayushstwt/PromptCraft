package com.ayshriv.promptcraft.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;

import java.util.Map;

@Service
public class PromptCraftService {

    private final ChatClient chat;

    public PromptCraftService(ChatClient.Builder chatBuilder) {
        this.chat = chatBuilder.build();
    }


    public String renderTemplate(String rawTemplate, Map<String, Object> vars) {
        PromptTemplate t = new PromptTemplate(rawTemplate);
        return t.render(vars == null ? Map.of() : vars);
    }

    public String generate(String system, String user, String model) {
        if (!StringUtils.hasText(user)) {
            throw new IllegalArgumentException("User message must not be empty");
        }

        var prompt = chat.prompt();

        if (StringUtils.hasText(system)) {
            prompt = prompt.system(system);
        }

        prompt = prompt.user(user);


        if (StringUtils.hasText(model)) {
            return prompt
                    .options(OpenAiChatOptions.builder().model(model).build())
                    .call()
                    .content();
        }

        return prompt.call().content();
    }


    public String generateFromTemplate(String system,
                                       String userTemplate,
                                       Map<String, Object> vars,
                                       String model) {
        String user = renderTemplate(userTemplate, vars);
        return generate(system, user, model);
    }
}
