package ru.otus.hw.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommentPagesController {

    @GetMapping("/comments/edit")
    public String editCommentPage() {
        return "comments/edit";
    }

    @GetMapping("/comments/add")
    public String addCommentPage() {
        return "comments/add";
    }

}
