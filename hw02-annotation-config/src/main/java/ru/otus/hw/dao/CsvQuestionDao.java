package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {

        List<QuestionDto> questionsDto;
        var inputStream = getClass().getResourceAsStream(fileNameProvider.getTestFileName());
        try (var reader = new BufferedReader(new InputStreamReader(inputStream))) {
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