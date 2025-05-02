package com.vk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vk.entity.BookIssue;

@Repository
public interface BookIssueRepository extends JpaRepository<BookIssue, Long> {
  
}
