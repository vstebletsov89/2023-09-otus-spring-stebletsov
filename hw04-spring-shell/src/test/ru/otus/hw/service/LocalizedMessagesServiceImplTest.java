package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.LocaleConfig;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(classes = {LocalizedMessagesServiceImpl.class,
                           MessageSourceAutoConfiguration.class})
class LocalizedMessagesServiceImplTest {

    @MockBean
    private LocaleConfig localeConfig;

    @Autowired
    private LocalizedMessagesServiceImpl localizedMessagesService;

    @Test
    void getMessage_en_US() {
        doReturn(Locale.forLanguageTag("en-US")).when(localeConfig).getLocale();

        String localizedMessage = localizedMessagesService.getMessage("TestService.enter.the.answer");

        assertEquals("Enter your answer", localizedMessage);
    }

    @Test
    void getMessage_ru_RU() {
        doReturn(Locale.forLanguageTag("ru-RU")).when(localeConfig).getLocale();

        String localizedMessage = localizedMessagesService.getMessage("TestService.enter.the.answer");

        assertEquals("Введите ваш ответ", localizedMessage);
    }
}