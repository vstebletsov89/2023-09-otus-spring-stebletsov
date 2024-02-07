package ru.otus.hw.service;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.domain.Butterfly;
import ru.otus.hw.domain.Caterpillar;

import java.util.Collection;

@MessagingGateway
public interface ButterflyGateway {

    @Gateway(requestChannel = "caterpillarsChannel", replyChannel = "butterflyChannel")
    Collection<Butterfly> process(Collection<Caterpillar> caterpillars);
}