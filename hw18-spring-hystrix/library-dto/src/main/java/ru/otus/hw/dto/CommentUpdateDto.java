package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateDto {
    @NotNull
    private Long id;

    @NotBlank(message = "Text field must not be blank")
    private String text;

    @NotNull
    private Long bookId;
}
