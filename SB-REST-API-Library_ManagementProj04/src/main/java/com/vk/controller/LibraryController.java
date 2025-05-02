package com.vk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vk.dto.BookDTO;
import com.vk.dto.BookIssueDTO;
import com.vk.entity.Book;
import com.vk.entity.BookIssue;
import com.vk.entity.Users;
import com.vk.service.LibraryService;
import com.vk.util.JwtUtil;

@RestController
public class LibraryController {

	@Autowired
	LibraryService libService;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtUtil jwtUtil ;
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Users user){
		UsernamePasswordAuthenticationToken upaToken =
				      new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
		
		//Here AuthManager use DaoAuthenticationProvider object for table record
		Authentication authenticate = authManager.authenticate(upaToken);
		boolean status = authenticate.isAuthenticated();
		
		if(status) {
	        return new ResponseEntity<String>("Login Success...\nThis is token ::  "+jwtUtil.generateToken(user.getEmail()),HttpStatus.OK );
		}
		
		return new ResponseEntity<String>("Username/password Invalid /n Login Failed..." ,HttpStatus.BAD_REQUEST);
 
	}
	
	
	
	
	@PostMapping("/addBookInLibrary")
	public ResponseEntity<String> addBookInLibrary(@RequestBody BookDTO bookDto){
		Book book = new Book();
		book.setBookName(bookDto.getBookName());
		book.setAuthorName(bookDto.getAuthorName());
		book.setBookPrice(bookDto.getBookPrice());
		book.setTotolBooks(bookDto.getNoOfCopyOfBook());
		book.setAvailableBooks(bookDto.getNoOfCopyOfBook());
		String result = libService.addBook(book);
		return  ResponseEntity.ok(result);
	}
	
	@GetMapping("/addMoreBooksOnExisting/{bid}/{copies}")
	public ResponseEntity<String> addMoreBooksOnExisting(@PathVariable Long bid , @PathVariable(name="copies") Integer noOfCopyOfBook){
		Book book = libService.fetchBookById(bid);
		book.setTotolBooks(noOfCopyOfBook + book.getTotolBooks() );
		book.setAvailableBooks(noOfCopyOfBook + book.getAvailableBooks());
		 libService.addBook(book);
		return new ResponseEntity<String>(noOfCopyOfBook+" more books added on existing successfully..",HttpStatus.OK  );
	}
	
	@PostMapping("/registerUser")
	public ResponseEntity<String> registerUser(@RequestBody Users user){
		String result = libService.saveUser(user);
		return  ResponseEntity.ok(result);
	}
	
	@PostMapping("/saveBookIssuedDetails")
	public ResponseEntity<String> saveBookIssuedDetails(@RequestBody BookIssueDTO dto) {
	    Book book = libService.fetchBookById(dto.getBookId());	                    
	    Users user = libService.fetchUserById(dto.getUserId());	                    

	    BookIssue issue = new BookIssue();
	    issue.setBook(book);
	    issue.setUser(user);
	    issue.setDueDate(dto.getDueDate());
	    issue.setStatus(dto.getStatus());

	    // save using service
	    String result = libService.saveIssueBookDetails(issue);
	    return ResponseEntity.ok(result);
	}
	
	@GetMapping("/submitBookAndCalculateChagres/{issueId}")
	public ResponseEntity<String> submitBookAndCalculateChagres(@PathVariable Long issueId){
		String result = libService.updateIssuedTableBasedOnID(issueId);
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/showAllBook")
	public ResponseEntity<List<Book>>  showAllBook(){
		List<Book> fetchAllBooks = libService.fetchAllBooks();
	    return new ResponseEntity<List<Book>>(fetchAllBooks,HttpStatus.OK);
	}

}
