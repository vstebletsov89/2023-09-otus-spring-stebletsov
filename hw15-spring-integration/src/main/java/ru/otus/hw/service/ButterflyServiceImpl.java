package ru.otus.hw.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Butterfly;
import ru.otus.hw.domain.Caterpillar;

@Slf4j
@Service
public class ButterflyServiceImpl implements ButterflyService{

    @Override
    public Butterfly transform(Caterpillar caterpillar) {
        log.info("Transforming {}", caterpillar.name());
        delay();
        log.info("Transforming {} done", caterpillar.name());
        return new Butterfly(caterpillar.name());
    }

    private static void delay() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
