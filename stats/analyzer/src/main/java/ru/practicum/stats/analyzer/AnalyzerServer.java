package ru.practicum.stats.analyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import ru.practicum.stats.analyzer.starter.AnalyzerStarter;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AnalyzerServer {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AnalyzerServer.class, args);
        AnalyzerStarter starter = context.getBean(AnalyzerStarter.class);
        starter.start();
    }
}
