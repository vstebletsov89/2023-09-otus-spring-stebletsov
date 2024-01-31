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
import ru.otus.hw.models.documents.GenreDocument;
import ru.otus.hw.models.tables.GenreTable;
import ru.otus.hw.service.GenreService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class GenreConfig {

    private static final int CHUNK_SIZE = 10;

    private static Map<Long, String> jpaIdToMongoIdMap = new HashMap<>();

    private final GenreService genreService;

    private final EntityManagerFactory entityManagerFactory;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    public static Map<Long, String> getJpaIdToMongoIdMap() {
        return jpaIdToMongoIdMap;
    }

    @Bean
    public ItemReader<GenreTable> genreTableItemReader() {
        return new JpaCursorItemReaderBuilder<GenreTable>()
                .name("genreItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select g from GenreTable g")
                .build();
    }

    @Bean
    public MongoItemWriter<GenreDocument> genreDocumentItemWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<GenreDocument>()
                .collection("genres")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public ItemProcessor<GenreTable, GenreDocument> genreProcessor() {
        return genreService::doConversion;
    }

    @Bean
    public Step migrateGenresStep(ItemReader<GenreTable> reader,
                                   MongoItemWriter<GenreDocument> writer,
                                   ItemProcessor<GenreTable, GenreDocument> itemProcessor) {
        return new StepBuilder("migrateGenresStep", jobRepository)
                .<GenreTable, GenreDocument>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(new ItemReadListener<>() {
                    public void onReadError(@NonNull Exception e) {
                        log.error("Genre migration read error: " + e.getMessage());
                    }
                })
                .listener(new ItemWriteListener<GenreDocument>() {
                    public void onWriteError(@NonNull Exception e, @NonNull List<GenreDocument> list) {
                        log.error("Genre migration write error: " + e.getMessage());
                    }
                })
                .listener(new ChunkListener() {
                    public void afterChunkError(@NonNull ChunkContext chunkContext) {
                        log.error("Genre migration chunk error: " + chunkContext);
                    }
                })
                .build();
    }
}