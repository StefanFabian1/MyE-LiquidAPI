package sk.sfabian.myeliquidapi.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import sk.sfabian.myeliquidapi.service.IngredientChangeStreamService;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/api/ingredients")
public class IngredientStreamController {
    private final IngredientChangeStreamService changeStreamService;
    private final ExecutorService executorService;

    public IngredientStreamController(IngredientChangeStreamService changeStreamService, ExecutorService executorService) {
        this.changeStreamService = changeStreamService;
        this.executorService = executorService;
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamChanges() {
        // Nastavenie timeoutu pre SseEmitter (napr. 60 sekúnd)
        SseEmitter emitter = new SseEmitter(0L);

        // Spustenie na pozadí pomocou ExecutorService
        executorService.submit(() -> {
            try {
                // Nastavenie "ping" správ každých 25 sekúnd - potrebujeme udrzat spojenie "live"
                ScheduledExecutorService pingService = Executors.newScheduledThreadPool(1);
                pingService.scheduleAtFixedRate(() -> {
                    try {
                        emitter.send(SseEmitter.event().comment("ping")); // Ping správa
                        System.out.println(new Date() + " Ping sent");
                    } catch (IOException e) {
                        System.err.println(new Date() + "Ping failed: " + e.getMessage());
                        emitter.completeWithError(e); // Ak sa nepodarí odoslať, ukonči spojenie
                    }
                }, 0, 25, TimeUnit.SECONDS);

                // Spracovanie udalostí zo služby
                changeStreamService.setEventConsumer(event -> {
                    try {
                        System.out.println(new Date() + "Sending event: " + event);
                        emitter.send(SseEmitter.event().data(event)); // Posielanie udalostí
                    } catch (IOException e) {
                        System.err.println(new Date() + "Error sending event: " + e.getMessage());
                        emitter.completeWithError(e);
                    }
                });

                // Nastavenie správania pri ukončení
                emitter.onTimeout(() -> {
                    System.out.println(new Date() + "Completed on timeout");
                    pingService.shutdownNow(); // Ukonči plánované "ping" správy
                    emitter.complete();
                });

                // Ukonči plánované "ping" správy
                emitter.onCompletion(() -> {
                    System.out.println("Emitter completed. Shutting down pingService.");
                    pingService.shutdownNow(); // Ukončí plánované "ping" správy
                });

            } catch (Exception e) {
                System.out.println(new Date() + "Completed on exception" + e.getMessage());
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }
}
