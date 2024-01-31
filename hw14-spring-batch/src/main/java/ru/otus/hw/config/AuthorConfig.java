package ru.otus.hw.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.models.documents.AuthorDocument;
import ru.otus.hw.models.tables.AuthorTable;
import ru.otus.hw.service.AuthorService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class AuthorConfig {

    private static final int CHUNK_SIZE = 10;

    private static Map<Long, AuthorDocument> jpaIdToMongoObjectMap = new HashMap<>();

    private final AuthorService authorService;

    private final EntityManagerFactory entityManagerFactory;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    public static Map<Long, AuthorDocument> getJpaIdToMongoObjectMap() {
        return jpaIdToMongoObjectMap;
    }

    @Bean
    public ItemReader<AuthorTable> authorTableItemReader() {
        return new JpaCursorItemReaderBuilder<AuthorTable>()
                .name("authorItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select a from AuthorTable a")
                .build();
    }

    @Bean
    public MongoItemWriter<AuthorDocument> authorDocumentItemWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<AuthorDocument>()
                .collection("authors")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public ItemProcessor<AuthorTable, AuthorDocument> authorProcessor() {
        return authorService::doConversion;
    }

    @Bean
    public Step migrateAuthorsStep(ItemReader<AuthorTable> reader,
                                   MongoItemWriter<AuthorDocument> writer,
                                   ItemProcessor<AuthorTable, AuthorDocument> itemProcessor) {
        return new StepBuilder("migrateAuthorsStep", jobRepository)
                .<AuthorTable, AuthorDocument>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(new ItemReadListener<>() {
                    public void onReadError(@NonNull Exception e) {
                        log.error("Author migration read error: " + e.getMessage());
                    }
                })
                .listener(new ItemWriteListener<AuthorDocument>() {
                    public void onWriteError(@NonNull Exception e, @NonNull List<AuthorDocument> list) {
                        log.error("Author migration write error: " + e.getMessage());
                    }
                })
                .listener(new ChunkListener() {
                    public void afterChunkError(@NonNull ChunkContext chunkContext) {
                        log.error("Author migration chunk error: " + chunkContext);
                    }
                })
                .build();
    }
}