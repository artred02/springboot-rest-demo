package org.grostarin.springboot.demorest.controllers;

import javax.validation.Valid;

import org.grostarin.springboot.demorest.annotations.LogExecutionTime;
import org.grostarin.springboot.demorest.domain.DeniedBook;
import org.grostarin.springboot.demorest.dto.BookSearch;
import org.grostarin.springboot.demorest.services.DeniedBookServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/denied/books")
public class DeniedBookController {

    @Autowired
    private DeniedBookServices bookServices;

    @GetMapping
    @LogExecutionTime
    public Iterable<DeniedBook> findAll(@Valid BookSearch bookSearchDTO) {
        return bookServices.findAll(bookSearchDTO);
    }

    @GetMapping("/{id}")
    public DeniedBook findOne(@PathVariable long id) {
        return bookServices.findOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeniedBook create(@RequestBody DeniedBook book) {
        return bookServices.create(book);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        bookServices.delete(id);
    }

    @PutMapping("/{id}")
    public DeniedBook updateBook(@RequestBody DeniedBook book, @PathVariable long id) {
        return bookServices.updateBook(book, id);
    }
}
