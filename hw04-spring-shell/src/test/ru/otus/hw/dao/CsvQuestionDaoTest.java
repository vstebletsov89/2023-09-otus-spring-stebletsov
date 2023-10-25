package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.TestFileNameProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(classes = {CsvQuestionDao.class})
class CsvQuestionDaoTest {

    @MockBean
    private TestFileNameProvider testFileNameProvider;

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @Test
    void findAll_en_US() {
        doReturn("/questions.csv").when(testFileNameProvider).getTestFileName();
        var questions = csvQuestionDao.findAll();

        // 5 questions must be processed
        assertEquals(5, questions.size());
        // check parsed question/answer
        assertEquals("What word is often abbreviated as Fn on a keyboard?", questions.get(1).text());
        assertEquals("Function", questions.get(1).answers().get(0).text());
    }

    @Test
    void findAll_ru_RU() {
        doReturn("/questions_ru.csv").when(testFileNameProvider).getTestFileName();

        var questions = csvQuestionDao.findAll();

        // 5 questions must be processed
        assertEquals(5, questions.size());
        // check parsed question/answer
        assertEquals("Какое слово на клавиатуре часто сокращается до Fn?", questions.get(1).text());
        assertEquals("Function", questions.get(1).answers().get(0).text());
    }
}