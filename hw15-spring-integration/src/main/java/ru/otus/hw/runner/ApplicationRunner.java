package ru.otus.hw.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.hw.domain.Caterpillar;
import ru.otus.hw.service.ButterflyGateway;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ApplicationRunner implements CommandLineRunner {
    private final ButterflyGateway butterflyGateway;

    @Override
    public void run(String... args)  {
        log.info("Started integration flow...");
        var caterpillars = List.of(
                new Caterpillar("Капустная гусеница"),
                new Caterpillar("Пяденица"),
                new Caterpillar("Тутовый шелкопряд"),
                new Caterpillar("Непарный шелкопряд"),
                new Caterpillar("Гусеница бабочки махаон"),
                new Caterpillar("Гусеница бабочки медведицы"),
                new Caterpillar("Листовертка"),
                new Caterpillar("Гусеница боярышницы"),
                new Caterpillar("Гусеница златогузки"),
                new Caterpillar("Гусеница бабочки большой гарпии")
        );
        var result = butterflyGateway.process(caterpillars);
        log.info("All caterpillars: done");
        log.info(result.toString());
    }
}
