package ru.otus.hw.models.documents;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "books")
public class BookDocument {
    @Id
    private String id;

    @Field(name = "title")
    private String title;

    @Field(name = "author")
    private AuthorDocument author;

    @Field(name = "genre")
    private GenreDocument genre;

    public BookDocument(String title, AuthorDocument author, GenreDocument genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
    }
}
