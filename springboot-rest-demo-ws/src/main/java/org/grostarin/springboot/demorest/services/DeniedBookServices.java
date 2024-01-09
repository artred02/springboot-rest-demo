package org.grostarin.springboot.demorest.services;

import org.grostarin.springboot.demorest.domain.DeniedBook;
import org.grostarin.springboot.demorest.dto.BookSearch;
import org.grostarin.springboot.demorest.exceptions.BookIdMismatchException;
import org.grostarin.springboot.demorest.exceptions.BookNotFoundException;
import org.grostarin.springboot.demorest.repositories.DeniedBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class DeniedBookServices {

    @Autowired
    private DeniedBookRepository DeniedBookRepository;

    public Iterable<DeniedBook> findAll(BookSearch bookSearchDTO) {
        if(bookSearchDTO!=null && StringUtils.hasText(bookSearchDTO.title())) {
            return DeniedBookRepository.findByTitle(bookSearchDTO.title());
        }
        return DeniedBookRepository.findAll();
    }

    public DeniedBook findOne(long id) {
        return DeniedBookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);
    }

    public DeniedBook create(DeniedBook book) {
        DeniedBook book1 = DeniedBookRepository.save(book);
        return book1;
    }

    public void delete(long id) {
        DeniedBookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);
        DeniedBookRepository.deleteById(id);
    }

    public DeniedBook updateBook(DeniedBook book, long id) {
        if (book.getId() != id) {
            throw new BookIdMismatchException();
        }
        DeniedBookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);
        return DeniedBookRepository.save(book);
    }
}
