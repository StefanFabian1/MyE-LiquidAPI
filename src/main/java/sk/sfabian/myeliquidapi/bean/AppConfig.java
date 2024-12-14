package sk.sfabian.myeliquidapi.bean;

import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AppConfig {
    @Bean
    public ExecutorService executorService() {
        return Executors.newCachedThreadPool();
    }

    @PreDestroy
    public void shutdownExecutorService() {
        executorService().shutdown();
    }

}