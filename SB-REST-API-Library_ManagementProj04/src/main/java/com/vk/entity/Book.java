package com.vk.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="Books")
public class Book {

	@Id
	@SequenceGenerator(name="bookSeq1" , sequenceName ="bseq01" , initialValue = 111, allocationSize =1  )	
	@GeneratedValue(generator = "bookSeq1" ,strategy=GenerationType.SEQUENCE)
	private Long bid ;
	
	private String bookName;
	private String authorName ;
	private Double bookPrice;
	private  Integer totolBooks;
	private  Integer availableBooks ;
	
	
	
}
