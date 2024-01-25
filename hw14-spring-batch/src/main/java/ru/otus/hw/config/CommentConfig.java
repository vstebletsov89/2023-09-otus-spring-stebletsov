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
import ru.otus.hw.mappers.CommentConverter;
import ru.otus.hw.models.documents.CommentDocument;
import ru.otus.hw.models.tables.CommentTable;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class CommentConfig {

    private static final int CHUNK_SIZE = 10;

    private final EntityManagerFactory entityManagerFactory;

    private final CommentConverter commentConverter;
    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public ItemReader<CommentTable> commentTableItemReader() {
        return new JpaCursorItemReaderBuilder<CommentTable>()
                .name("commentItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select c from CommentTable c")
                .build();
    }

    @Bean
    public MongoItemWriter<CommentDocument> commentDocumentItemWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<CommentDocument>()
                .collection("comments")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public ItemProcessor<CommentTable, CommentDocument> commentProcessor(CommentConverter commentConverter) {
        return commentConverter::convert;
    }

    @Bean
    public Step migrateCommentsStep(ItemReader<CommentTable> reader,
                                   MongoItemWriter<CommentDocument> writer,
                                   ItemProcessor<CommentTable, CommentDocument> itemProcessor) {
        return new StepBuilder("migrateCommentsStep", jobRepository)
                .<CommentTable, CommentDocument>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(new ItemReadListener<CommentTable>() {
                    public void onReadError(@NonNull Exception e) {
                        log.error("Comment migration read error: " + e.getMessage());
                    }
                })
                .listener(new ItemWriteListener<CommentDocument>() {
                    public void onWriteError(@NonNull Exception e, @NonNull List<CommentDocument> list) {
                        log.error("Comment migration write error: " + e.getMessage());
                    }
                })
                .listener(new ChunkListener() {
                    public void afterChunkError(@NonNull ChunkContext chunkContext) {
                        log.error("Comment migration chunk error: " + chunkContext.toString());
                    }
                })
                .build();
    }
}