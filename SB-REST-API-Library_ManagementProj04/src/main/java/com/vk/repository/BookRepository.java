package com.vk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vk.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

}
