package com.vk.dto;

import lombok.Data;

@Data
public class BookDTO {

	private String bookName;
	private String authorName ;
	private Double bookPrice;
	private  Integer noOfCopyOfBook;
	
}
