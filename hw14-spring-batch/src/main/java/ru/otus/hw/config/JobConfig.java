package ru.otus.hw.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JobConfig {
    private static final String IMPORT_LIBRARY_JOB_NAME = "importLibraryJob";

    private JobRepository jobRepository;

    @Bean
    public Job importLibraryJob(Step migrateAuthorsStep,
                                Step migrateGenresStep,
                                Step migrateBooksStep,
                                Step migrateCommentsStep) {

        return new JobBuilder(IMPORT_LIBRARY_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(migrateAuthorAndGenres(migrateAuthorsStep, migrateGenresStep))
                .next(migrateBooksStep)
                .next(migrateCommentsStep)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        log.info("Start library migration");
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        log.info("Migration completed");
                    }
                })
                .build();

    }

    @Bean
    public Flow migrateAuthorAndGenres(Step migrateAuthorsStep,
                                       Step migrateGenresStep) {
        return new FlowBuilder<SimpleFlow>("migrateAuthorAndGenres")
                .split(taskExecutor())
                .add(migrateAuthors(migrateAuthorsStep), migrateGenres(migrateGenresStep))
                .build();
    }

    @Bean
    public Flow migrateAuthors(Step migrateAuthorsStep) {
        return new FlowBuilder<SimpleFlow>("migrateAuthors")
                .start(migrateAuthorsStep)
                .build();
    }

    @Bean
    public Flow migrateGenres(Step migrateGenresStep) {
        return new FlowBuilder<SimpleFlow>("migrateGenres")
                .start(migrateGenresStep)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }
}

