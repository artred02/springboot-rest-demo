package org.grostarin.springboot.demorest.repositories;

import org.grostarin.springboot.demorest.domain.DeniedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeniedBookRepository extends JpaRepository<DeniedBook, Long> {
    List<DeniedBook> findByTitle(String title);
}
