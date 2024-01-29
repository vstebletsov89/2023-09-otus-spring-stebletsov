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
import ru.otus.hw.models.documents.BookDocument;
import ru.otus.hw.models.tables.BookTable;
import ru.otus.hw.service.BookService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class BookConfig {
    private static final int CHUNK_SIZE = 10;

    private static Map<Long, BookDocument> jpaIdToMongoObjectMap = new HashMap<>();

    private final BookService bookService;

    private final EntityManagerFactory entityManagerFactory;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    public static Map<Long, BookDocument> getJpaIdToMongoObjectMap() {
        return jpaIdToMongoObjectMap;
    }

    @Bean
    public ItemReader<BookTable> bookTableItemReader() {
        return new JpaCursorItemReaderBuilder<BookTable>()
                .name("bookItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select b from BookTable b")
                .build();
    }

    @Bean
    public MongoItemWriter<BookDocument> bookDocumentItemWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<BookDocument>()
                .collection("books")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public ItemProcessor<BookTable, BookDocument> bookProcessor() {
        return bookService::doConversion;
    }

    @Bean
    public Step migrateBooksStep(ItemReader<BookTable> reader,
                                   MongoItemWriter<BookDocument> writer,
                                   ItemProcessor<BookTable, BookDocument> itemProcessor) {
        return new StepBuilder("migrateBooksStep", jobRepository)
                .<BookTable, BookDocument>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(new ItemReadListener<>() {
                    public void onReadError(@NonNull Exception e) {
                        log.error("Book migration read error: " + e.getMessage());
                    }
                })
                .listener(new ItemWriteListener<BookDocument>() {
                    public void onWriteError(@NonNull Exception e, @NonNull List<BookDocument> list) {
                        log.error("Book migration write error: " + e.getMessage());
                    }
                })
                .listener(new ChunkListener() {
                    public void afterChunkError(@NonNull ChunkContext chunkContext) {
                        log.error("Book migration chunk error: " + chunkContext);
                    }
                })
                .build();
    }
}