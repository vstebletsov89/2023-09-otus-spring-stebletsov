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

    @GetMapping("/books/create")
    public String addBookPage() {
        return "add";
    }

    @GetMapping("/books/view")
    public String viewBookPage() {
        return "view";
    }
}