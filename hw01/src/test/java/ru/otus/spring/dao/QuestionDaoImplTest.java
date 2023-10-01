package ru.otus.spring.dao;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuestionDaoImplTest {

    @Test
    void loadQuestions() {
        Resource resource = new ClassPathResource("questions.csv");
        QuestionDao questionDao = new QuestionDaoImpl(resource);
        var questions = questionDao.loadQuestions();

        // 5 questions must be processed
        assertEquals(5, questions.size());

        // check parsed question/answer
        assertEquals("What word is often abbreviated as Fn on a keyboard?", questions.get(1).getText());
        assertEquals("Function", questions.get(1).getAnswer());
    }
}