package com.vk.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;

@Data
@Entity
@Table(name="BookIssued")
public class BookIssue {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long issueid;
	
	    @ManyToOne(fetch=FetchType.EAGER)
	    @JoinColumn(name = "book_id", referencedColumnName = "bid" )
	    private Book book;

	    @ManyToOne(fetch=FetchType.EAGER)
	    @JoinColumn(name = "user_id", referencedColumnName = "uid")
	    private Users user;

	    private LocalDate issueDate =LocalDate.now()  ; //
	    private LocalDate dueDate;
	    private LocalDate returnDate;
	    private String status  ;   // Issued or returned
	    
	    @Version
	    private Long renewalCount ;
}
