package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService productService){
        this.bookService = productService;
    }

    @GetMapping
    public String index(){
        return "index";
    }

    // Read
    @GetMapping("/read")
    public String read(Model model){
        model.addAttribute("books", bookService.getAllBooks());
        return "crud/read";
    }

    @GetMapping("/read/author")
    public String read(@RequestParam String authorName, Model model){
        model.addAttribute("books", bookService.findBooksByAuthor(authorName));
        return "crud/read";
    }

    // Create
    @GetMapping("/create")
    public String create(Model model){
        return "crud/save";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Book book){
        bookService.addBook(book);
        return "redirect:/books/read";
    }

    // Delete
    @GetMapping("/delete")
    public String delete(@RequestParam long id, Model model){
        Book book = bookService.findBookById(id).get();
        model.addAttribute("book", book);
        return "crud/delete";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam long id){
        Book book = bookService.findBookById(id).get();
        bookService.deleteBookById(id);
        return "redirect:/books/read";
    }

    // Update
    @GetMapping("/update")
    public String update(@RequestParam long id, Model model){
        model.addAttribute("book", bookService.findBookById(id).get());
        return "crud/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Book book){
        bookService.updateBook(book);
        return "redirect:/books/read";
    }
}
