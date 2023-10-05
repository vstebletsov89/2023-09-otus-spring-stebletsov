package ru.otus.spring.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.Data;
import org.springframework.core.io.Resource;
import ru.otus.spring.dao.dto.QuestionDto;
import ru.otus.spring.domain.Question;
import ru.otus.spring.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CsvQuestionDao implements QuestionDao {

    private final Resource resource;

    public CsvQuestionDao(Resource resource) {
        this.resource = resource;
    }

    @Override
    public List<Question> loadQuestions() {

        List<QuestionDto> questionsDto;
        Resource resource = getResource();
        try (InputStreamReader reader = new InputStreamReader(resource.getInputStream())) {
            questionsDto = new CsvToBeanBuilder<QuestionDto>(reader)
                    .withSkipLines(1)
                    .withType(QuestionDto.class)
                    .build()
                    .parse();
        } catch (IOException e) {
            throw new QuestionReadException(e.getMessage(), e);
        }

        return questionsDto.stream().map(QuestionDto::toDomainObject).collect(Collectors.toList());
    }
}