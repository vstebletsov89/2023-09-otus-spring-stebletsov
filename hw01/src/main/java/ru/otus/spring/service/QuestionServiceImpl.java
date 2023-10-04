package ru.otus.spring.service;

import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.Question;

public class QuestionServiceImpl implements QuestionService {
    private final QuestionDao questionDao;
    private final IOService ioService;

    public QuestionServiceImpl(QuestionDao questionDao, IOService ioService) {
        this.questionDao = questionDao;
        this.ioService = ioService;
    }


    @Override
    public void runTest() {
        var questions = questionDao.loadQuestions();

        Person person = new Person("Slava", "Stebletsov");
        ioService.println("Hello, " + person);

        for (Question question : questions) {
            ioService.println(question.text());
        }
    }

}