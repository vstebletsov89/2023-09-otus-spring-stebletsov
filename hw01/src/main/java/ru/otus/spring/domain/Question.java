package ru.otus.spring.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class Question {

    @CsvBindByName
    private String text;

    @CsvBindByName
    private String answer;

    public boolean isCorrectAnswer(String studentAnswer) {
        return answer.equalsIgnoreCase(studentAnswer.trim());
    }

}
