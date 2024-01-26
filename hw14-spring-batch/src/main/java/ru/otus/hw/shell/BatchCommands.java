package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.output.BookOutput;
import ru.otus.hw.output.CommentOutput;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.CommentService;

import java.util.Properties;

@RequiredArgsConstructor
@ShellComponent
public class BatchCommands {

    private static final String IMPORT_LIBRARY_JOB_NAME = "importLibraryJob";

    private final JobOperator jobOperator;

    private final JobExplorer jobExplorer;

    private final BookOutput bookOutput;

    private final CommentOutput commentOutput;

    private final BookService bookService;

    private final CommentService commentService;

    //TODO: update commands here
    //TODO: add command to get all consolidated data from migrated mongodb

    @SuppressWarnings("unused")
    @ShellMethod(value = "startMigrationJob", key = "sm")
    public void startMigrationJobWithJobOperator() throws Exception {
        Properties properties = new Properties();
        Long executionId = jobOperator.start(IMPORT_LIBRARY_JOB_NAME, properties);
        System.out.println(jobOperator.getSummary(executionId));
    }

    @SuppressWarnings("unused")
    @ShellMethod(value = "showInfo", key = "i")
    public void showInfo() {
        System.out.println(jobExplorer.getJobNames());
        System.out.println(jobExplorer.getLastJobInstance(IMPORT_LIBRARY_JOB_NAME));
    }

    @SuppressWarnings("unused")
    @ShellMethod(value = "showDataFromMongoDb", key = "d")
    public void showData() {
        System.out.println("Migrated data:");
        var books = bookService.findAll();
        books.forEach(bookOutput::printBook);
        books.forEach(bookDocument -> commentService
                        .findAllByBookId(bookDocument.getId())
                        .forEach(commentOutput::printComment));
    }
}