package ru.otus.spring.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.Data;
import org.springframework.core.io.Resource;
import ru.otus.spring.domain.Question;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Data
public class CsvQuestionDao implements QuestionDao {

    private final Resource resource;

    public CsvQuestionDao(Resource resource) {
        this.resource = resource;
    }

    @Override
    public List<Question> loadQuestions() {

        List<Question> questions;
        Resource resource = getResource();
        try (InputStreamReader reader = new InputStreamReader(resource.getInputStream())) {
            questions = new CsvToBeanBuilder<Question>(reader)
                    .withType(Question.class)
                    .build()
                    .parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return questions;
    }
}