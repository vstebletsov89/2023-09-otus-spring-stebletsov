package ru.otus.hw.service;

import ru.otus.hw.domain.Butterfly;
import ru.otus.hw.domain.Caterpillar;

public interface ButterflyService {
    Butterfly transform(Caterpillar caterpillar);
}

