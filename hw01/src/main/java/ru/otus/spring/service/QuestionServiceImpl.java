package ru.otus.spring.service;

import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.Question;

public class QuestionServiceImpl implements QuestionService {
    private final QuestionDao questionDao;

    public QuestionServiceImpl(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }


    @Override
    public void runTest() {
        var questions = questionDao.loadQuestions();

        Person person = new Person("Slava", "Stebletsov");
        System.out.println("Hello, " + person);

        for (Question question : questions) {
            System.out.println(question.text());
        }
    }

}