//package ru.otus.hw.commands;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.shell.standard.ShellComponent;
//import org.springframework.shell.standard.ShellMethod;
//import ru.otus.hw.converters.GenreConverter;
//import ru.otus.hw.services.GenreService;
//
//import java.util.stream.Collectors;
//
//@RequiredArgsConstructor
//@ShellComponent
//public class GenreCommands {
//
//    private final GenreService genreService;
//
//    private final GenreConverter genreConverter;
//
//    @ShellMethod(value = "Find all genres", key = "ag")
//    public String findAllGenres() {
//        return genreService.findAll()
//                .stream()
//                .map(genreConverter::genreToString)
//                .collect(Collectors.joining("," + System.lineSeparator()));
//    }
//
//    @ShellMethod(value = "Find genre by id", key = "fg")
//    public String findGenreById(long id) {
//        return genreConverter.genreToString(genreService.findById(id));
//    }
//}
