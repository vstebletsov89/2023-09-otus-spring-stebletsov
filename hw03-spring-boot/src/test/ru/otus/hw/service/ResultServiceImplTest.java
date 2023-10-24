package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import static org.mockito.Mockito.*;

class ResultServiceImplTest {

    @Test
    void successAttempt() {
        LocalizedIOService ioService = mock(LocalizedIOService.class);
        TestConfig testConfig = () -> 3; // number correct answers to pass test
        ResultServiceImpl resultService = new ResultServiceImpl(testConfig, ioService);
        TestResult testResult = mock(TestResult.class);
        doReturn(4).when(testResult).getRightAnswersCount();
        doReturn(new Student("Ivan", "Ivanov")).when(testResult).getStudent();

        resultService.showResult(testResult);

        verify(ioService, atMost(1)).printLineLocalized("ResultService.passed.test");
    }

    @Test
    void failedAttempt() {
        LocalizedIOService ioService = mock(LocalizedIOService.class);
        TestConfig testConfig = () -> 5; // number correct answers to pass test
        ResultServiceImpl resultService = new ResultServiceImpl(testConfig, ioService);
        TestResult testResult = mock(TestResult.class);
        doReturn(4).when(testResult).getRightAnswersCount();
        doReturn(new Student("Ivan", "Ivanov")).when(testResult).getStudent();

        resultService.showResult(testResult);

        verify(ioService, atMost(1)).printLineLocalized("ResultService.fail.test");
    }
}