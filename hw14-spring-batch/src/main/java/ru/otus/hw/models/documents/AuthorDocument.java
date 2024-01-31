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
@Document(collection = "authors")
public class AuthorDocument {
    @Id
    private String id;

    @Field(name = "full_name")
    private String fullName;

    public AuthorDocument(String fullName) {
        this.fullName = fullName;
    }
}
