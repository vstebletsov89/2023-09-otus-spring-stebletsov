package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;


    @Override
    public CommentDto findById(long id) {
        return CommentMapper.commentToCommentDto(
                commentRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Comment with id %d not found".formatted(id))));
    }

    @Override
    public List<CommentDto> findAllByBookId(long id) {
        var book =
                bookRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Book with id %d not found"
                                .formatted(id)
                        ));
        return commentRepository.findAllByBookId(id)
                .stream()
                .map(CommentMapper::commentToCommentDto)
                .toList();
    }

    @Transactional
    @Override
    public CommentDto create(CommentDto commentDto) {
        var book =
                bookRepository.findById(commentDto.getBookId())
                        .orElseThrow(() -> new NotFoundException("Book with id %d not found"
                                .formatted(commentDto.getBookId())
                        ));
        var newComment = new Comment(commentDto.getId(), commentDto.getText(), book);
        return CommentMapper.commentToCommentDto(
                commentRepository.save(newComment));
    }

    @Transactional
    @Override
    public CommentDto update(CommentDto commentDto) {
        bookRepository.findById(commentDto.getBookId())
                .orElseThrow(() -> new NotFoundException("Book with id %d not found"
                        .formatted(commentDto.getBookId())
                ));

        var updatedComment =
                commentRepository.findById(commentDto.getId())
                        .orElseThrow(() -> new NotFoundException("Comment with id %d not found"
                                .formatted(commentDto.getId())));

        updatedComment.setText(commentDto.getText());
        return CommentMapper.commentToCommentDto(commentRepository.save(updatedComment));
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

}
