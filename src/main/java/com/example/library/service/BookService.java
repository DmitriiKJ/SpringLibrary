package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public Book addBook(Book book){
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public Optional<Book> findBookById(long id){
        return bookRepository.findById(id);
    }

    public List<Book> findBooksByAuthor(String author){
        return bookRepository.findByAuthor(author);
    }

    public void deleteBookById(long id){
        bookRepository.deleteById(id);
    }

    public Book updateBook(Book book){
        return bookRepository.save(book);
    }
}
