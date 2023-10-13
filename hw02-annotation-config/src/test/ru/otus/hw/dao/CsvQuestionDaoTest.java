package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import ru.otus.hw.config.TestFileNameProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class CsvQuestionDaoTest {

    @Test
    void findAll() {
        TestFileNameProvider testFileNameProvider = mock(TestFileNameProvider.class);
        doReturn("/questions.csv").when(testFileNameProvider).getTestFileName();
        QuestionDao questionDao = new CsvQuestionDao(testFileNameProvider);
        var questions = questionDao.findAll();

        // 5 questions must be processed
        assertEquals(5, questions.size());

        // check parsed question/answer
        assertEquals("What word is often abbreviated as Fn on a keyboard?", questions.get(1).text());
        assertEquals("Function", questions.get(1).answers().get(0).text());
    }
}