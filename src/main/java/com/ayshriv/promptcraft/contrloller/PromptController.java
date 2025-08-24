package com.ayshriv.promptcraft.contrloller;

import com.ayshriv.promptcraft.core.TemplateStore;
import com.ayshriv.promptcraft.dto.CreateOrUpdateTemplateRequest;
import com.ayshriv.promptcraft.dto.GenerateRequest;
import com.ayshriv.promptcraft.dto.GenerateResponse;
import com.ayshriv.promptcraft.dto.PromptDefinition;
import com.ayshriv.promptcraft.service.PromptCraftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PromptController {
    private final TemplateStore store;
    private final PromptCraftService service;

    @GetMapping("/templates")
    public List<PromptDefinition> list() {
        return store.list();
    }

    @PostMapping("/templates")
    public ResponseEntity<PromptDefinition> upsert(@Valid @RequestBody CreateOrUpdateTemplateRequest req) {
        var def = new PromptDefinition(req.name(), req.description(), req.system(), req.userTemplate());
        store.upsert(def);
        return ResponseEntity.created(URI.create("/api/templates/" + def.name())).body(def);
    }

    @PostMapping("/generate")
    public GenerateResponse generate(@Valid @RequestBody GenerateRequest req) {
        String system = req.system();

        // If existing template name is found, use it; else treat req.template as raw template
        String userTemplate = store.get(req.template())
                .map(PromptDefinition::userTemplate)
                .orElse(req.template());

        if (system == null) {
            system = store.get(req.template()).map(PromptDefinition::system).orElse(null);
        }

        String content = service.generateFromTemplate(system, userTemplate, req.variables(), req.model());
        return new GenerateResponse(content);
    }
}
