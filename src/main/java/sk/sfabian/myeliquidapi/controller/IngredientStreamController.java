package sk.sfabian.myeliquidapi.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import sk.sfabian.myeliquidapi.service.IngredientChangeStreamService;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

@RestController
public class IngredientStreamController {
    private final IngredientChangeStreamService changeStreamService;
    private final ExecutorService executorService;

    public IngredientStreamController(IngredientChangeStreamService changeStreamService, ExecutorService executorService) {
        this.changeStreamService = changeStreamService;
        this.executorService = executorService;
    }

    @GetMapping(value = "/api/ingredients/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamChanges() {
        SseEmitter emitter = new SseEmitter();
        executorService.submit(() -> {
            try {
                changeStreamService.setEventConsumer(event -> {
                    try {
                        emitter.send(SseEmitter.event().data(event));
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                    }
                });
                emitter.onTimeout(emitter::complete);
                emitter.onCompletion(emitter::complete);
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }
}
