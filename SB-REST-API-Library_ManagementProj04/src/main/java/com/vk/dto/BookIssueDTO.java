package com.vk.dto;


import java.time.LocalDate;

import lombok.Data;

@Data
public class BookIssueDTO {
	
    private Long bookId;
    private Long userId; 
    private LocalDate dueDate;
    private String status;
}

