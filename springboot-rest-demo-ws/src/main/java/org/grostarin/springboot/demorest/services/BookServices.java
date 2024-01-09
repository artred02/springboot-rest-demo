package org.grostarin.springboot.demorest.services;

import org.grostarin.springboot.demorest.domain.Book;
import org.grostarin.springboot.demorest.domain.DeniedBook;
import org.grostarin.springboot.demorest.dto.BookSearch;
import org.grostarin.springboot.demorest.exceptions.BookDeniedException;
import org.grostarin.springboot.demorest.exceptions.BookIdMismatchException;
import org.grostarin.springboot.demorest.exceptions.BookNotFoundException;
import org.grostarin.springboot.demorest.repositories.BookRepository;
import org.grostarin.springboot.demorest.repositories.DeniedBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class BookServices {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private DeniedBookRepository deniedBookRepository;

    public Iterable<Book> findAll(BookSearch bookSearchDTO) {
        if(bookSearchDTO!=null && StringUtils.hasText(bookSearchDTO.title())) {
            return bookRepository.findByTitle(bookSearchDTO.title());  
        }
        return bookRepository.findAll();
    }

    public Book findOne(long id) {
        return bookRepository.findById(id)
          .orElseThrow(BookNotFoundException::new);
    }

    public Book create(Book book) {
        if (isDenied(book)) {
            throw new BookDeniedException();
        } else {
            Book book1 = bookRepository.save(book);
            return book1;
        }
    }

    public void delete(long id) {
        bookRepository.findById(id)
          .orElseThrow(BookNotFoundException::new);
        bookRepository.deleteById(id);
    }

    public boolean isDenied(Book book) {
        List<DeniedBook> deniedBooks = deniedBookRepository.findAll();
        for (DeniedBook deniedBook : deniedBooks) {
            return deniedBook.getTitle().equals(book.getTitle()) && deniedBook.getAuthor().equals(book.getAuthor());
        }
        return false;
    }

    public Book updateBook(Book book, long id) {
        if (book.getId() != id) {
            throw new BookIdMismatchException();
        }
        bookRepository.findById(id)
          .orElseThrow(BookNotFoundException::new);
        return bookRepository.save(book);
    }
}
