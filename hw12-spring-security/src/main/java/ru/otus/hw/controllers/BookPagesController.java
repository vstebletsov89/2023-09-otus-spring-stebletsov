package ru.otus.hw.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookPagesController {

    @GetMapping("/")
    public String listBooksPage() {
        return "list";
    }

    @GetMapping("/books/edit")
    public String editBookPage() {
        return "edit";
    }

    @GetMapping("/books/add")
    public String addBookPage() {
        return "add";
    }

    @GetMapping("/books/info")
    public String viewBookPage() {
        return "info";
    }
}