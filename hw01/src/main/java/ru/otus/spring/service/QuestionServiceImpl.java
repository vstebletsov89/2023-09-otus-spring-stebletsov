package ru.otus.spring.service;

import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.Question;

import java.util.Scanner;

public class QuestionServiceImpl implements QuestionService {
    private final QuestionDao questionDao;

    public QuestionServiceImpl(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }


    @Override
    public void runTest() {
        var questions = questionDao.loadQuestions();
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter your first name:");
            String studentFirstName = scanner.nextLine();
            System.out.println("Enter your last name:");
            String studentLasttName = scanner.nextLine();

            Person person = new Person(studentFirstName, studentLasttName);
            System.out.println("Hello, " + person);
            int totalCorrectAnswers = 0;
            int totalQuestions = questions.size();

            for (Question question : questions) {
                System.out.println(question.getText());
                System.out.println("Enter your answer:");
                String answer = scanner.nextLine();
                if (question.isCorrectAnswer(answer)) {
                    totalCorrectAnswers++;
                }
            }
            System.out.println(" Your exam score: " + totalCorrectAnswers +
                    " / " + totalQuestions);
        }
    }
}