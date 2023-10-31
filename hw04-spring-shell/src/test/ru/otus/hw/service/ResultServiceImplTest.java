package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.LocaleConfig;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import static org.mockito.Mockito.*;

@SpringBootTest
class ResultServiceImplTest {

    @MockBean
    private LocaleConfig localeConfig;

    @MockBean
    private TestConfig testConfig;

    @MockBean
    private TestFileNameProvider testFileNameProvider;

    @MockBean
    private LocalizedIOService localizedIOService;

    @MockBean
    private TestResult testResult;

    @Autowired
    private ResultServiceImpl resultService;

    @Test
    void successAttempt() {
        doReturn(3).when(testConfig).getRightAnswersCountToPass(); // number correct answers to pass test
        doReturn(4).when(testResult).getRightAnswersCount();
        doReturn(new Student("Ivan", "Ivanov")).when(testResult).getStudent();

        resultService.showResult(testResult);

        verify(localizedIOService, atMost(1)).printLineLocalized("ResultService.passed.test");
    }

    @Test
    void failedAttempt() {
        doReturn(5).when(testConfig).getRightAnswersCountToPass();
        doReturn(4).when(testResult).getRightAnswersCount();
        doReturn(new Student("Ivan", "Ivanov")).when(testResult).getStudent();

        resultService.showResult(testResult);

        verify(localizedIOService, atMost(1)).printLineLocalized("ResultService.fail.test");
    }
}