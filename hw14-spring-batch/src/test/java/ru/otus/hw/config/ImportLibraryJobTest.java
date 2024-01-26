package ru.otus.hw.config;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import ru.otus.hw.output.BookOutput;
import ru.otus.hw.repositories.BookRepository;


import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@SpringBatchTest
class ImportLibraryJobTest {

    private static final String IMPORT_LIBRARY_JOB_NAME = "importLibraryJob";

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookOutput bookOutput;

    @BeforeEach
    void clearMetaData() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void testJob() throws Exception {
        var classLoader = ImportLibraryJobTest.class.getClassLoader();

        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo(IMPORT_LIBRARY_JOB_NAME);

        JobParameters parameters = new JobParametersBuilder()
                .toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(parameters);
        var books = bookRepository.findAll();
        books.forEach(bookOutput::printBook);

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
        assertEquals(3, books.size());
    }
}