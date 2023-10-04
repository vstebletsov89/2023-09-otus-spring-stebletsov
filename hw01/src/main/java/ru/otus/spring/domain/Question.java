package ru.otus.spring.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import java.util.List;

public record Question(String text, List<Answer> answers) {
}
