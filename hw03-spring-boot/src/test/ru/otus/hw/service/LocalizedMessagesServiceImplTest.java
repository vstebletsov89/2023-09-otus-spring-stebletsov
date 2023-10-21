package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import ru.otus.hw.config.LocaleConfig;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalizedMessagesServiceImplTest {

    static ReloadableResourceBundleMessageSource messageSource;

    @BeforeAll
    static void prepareMessages() {
        messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setFallbackToSystemLocale(false);
    }

    @Test
    void getMessage_en_US() {
        LocaleConfig localeConfig = () -> Locale.forLanguageTag("en-US");
        LocalizedMessagesServiceImpl localizedMessagesService = new LocalizedMessagesServiceImpl(localeConfig, messageSource);

        String localizedMessage = localizedMessagesService.getMessage("TestService.enter.the.answer");

        assertEquals("Enter your answer", localizedMessage);
    }

    @Test
    void getMessage_ru_RU() {
        LocaleConfig localeConfig = () -> Locale.forLanguageTag("ru-RU");
        LocalizedMessagesServiceImpl localizedMessagesService = new LocalizedMessagesServiceImpl(localeConfig, messageSource);

        String localizedMessage = localizedMessagesService.getMessage("TestService.enter.the.answer");

        assertEquals("Введите ваш ответ", localizedMessage);
    }
}