package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class TestServiceImplTest {

    static List<Question> questions = new ArrayList<>();

    @BeforeAll
    static void createQuestions() {

        List<Answer> answers1 = new ArrayList<>();
        answers1.add(new Answer("first answer", true));
        questions.add(new Question("first question", answers1));
        List<Answer> answers2 = new ArrayList<>();
        answers2.add(new Answer("second answer", true));
        questions.add(new Question("second question", answers2));
        List<Answer> answers3 = new ArrayList<>();
        answers3.add(new Answer("third answer", true));
        questions.add(new Question("third question", answers3));
        List<Answer> answers4 = new ArrayList<>();
        answers4.add(new Answer("fourth answer", true));
        questions.add(new Question("fourth question", answers4));
        List<Answer> answers5 = new ArrayList<>();
        answers5.add(new Answer("fifth answer", true));
        questions.add(new Question("fifth question", answers5));
    }

    @Test
    void executeTestFor() {
        IOService ioService = mock(IOService.class);
        StudentServiceImpl studentService = new StudentServiceImpl(ioService);
        doReturn("StudentFirstName").when(ioService).readStringWithPrompt("Please input your first name");
        doReturn("StudentLastName").when(ioService).readStringWithPrompt("Please input your last name");
        Student student = studentService.determineCurrentStudent();
        assertEquals("StudentFirstName StudentLastName", student.getFullName());


        QuestionDao questionDao = mock(QuestionDao.class);
        doReturn(questions).when(questionDao).findAll();
        TestServiceImpl testService = new TestServiceImpl(ioService, questionDao);
        doReturn("first answer", "second answer", "third answer", "wrong answer", "wrong answer").when(ioService).readString();
        var testResult = testService.executeTestFor(student);
        assertEquals(3, testResult.getRightAnswersCount());

    }
}