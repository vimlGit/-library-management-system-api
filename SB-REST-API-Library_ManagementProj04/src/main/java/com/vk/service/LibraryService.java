package com.vk.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vk.entity.Book;
import com.vk.entity.BookIssue;
import com.vk.entity.Users;
import com.vk.repository.BookIssueRepository;
import com.vk.repository.BookRepository;
import com.vk.repository.UsersRepository;

@Service
public class LibraryService implements UserDetailsService {

	@Autowired
    private	BookRepository      bookRepo;
	@Autowired
    private BookIssueRepository bookIssueRepo;
	@Autowired
    private UsersRepository     usersRepo ;   
	@Autowired
	private PasswordEncoder pwdEncoder;
	
	//add book in library
	public String addBook(Book book) {
		Book saveBook = bookRepo.save(book);
		return "Book added with id:"+saveBook.getBid()+" in library successfully..";
	}
	
	//save user
	public String saveUser(Users user) {
		user.setPassword( pwdEncoder.encode(user.getPassword()));
		Users savedUser = usersRepo.save(user);
		return "Id no: "+savedUser.getUid()+"  user is registered successfully";
	} 
	
	
	//maintain IssueBook 
	public String saveIssueBookDetails(BookIssue issueBook) {
		BookIssue issued = bookIssueRepo.save(issueBook);
		
		//decrease the count of book in table after book issued to Student
		Book book = issued.getBook();
		book.setAvailableBooks( book.getAvailableBooks()-1);
		bookRepo.save(book);
		
		return "Book id:"+issueBook.getBook().getBid()+" given to userID:"+issued.getUser().getUid()+" is saved with BookIssued ID:"+issued.getIssueid()+" successfully";
	} 
	
	
	//find book by id
	public Book fetchBookById(Long bid) {
		return bookRepo.findById(bid).orElseThrow(() -> new RuntimeException("Book not found"));
	}
	
	
	//find user by id
	public Users fetchUserById(Long uid) {
		return usersRepo.findById(uid).orElseThrow(() -> new RuntimeException("User not found"));

	}
	
	
	//update issued table record when book got return based book issued id
	public String updateIssuedTableBasedOnID(Long issuedId) {
	   	BookIssue bookIssue = bookIssueRepo.findById(issuedId).orElseThrow(() -> new RuntimeException("Issued Details not found..."));
	   	bookIssue.setReturnDate(LocalDate.now());
	   	bookIssue.setStatus("returned");
	   	bookIssueRepo.save(bookIssue);
	   	Double charges = charges(bookIssue);	   	
	   	//increase the book count in table after returned
	   	Book book = bookIssue.getBook();
		book.setAvailableBooks( book.getAvailableBooks()+1);
		bookRepo.save(book);
	   	
	   	return "Book submitted successfully.., Your total charges :: "+charges;
	}
	
			
	public Double charges(BookIssue bookIssue) {
		    LocalDate issueDate = bookIssue.getIssueDate();
		    LocalDate returnDate = bookIssue.getReturnDate();
		    Double bookPrice = bookIssue.getBook().getBookPrice();

		    if (issueDate == null || returnDate == null) {
		        System.out.println("Issue date or return date is missing.");
		        
		    }

		    long totalDays = ChronoUnit.DAYS.between(issueDate, returnDate);

		    double charges = 0.0;

		    if (totalDays > 15) {
		        long extraDays = totalDays - 15;
		        
		        if (extraDays <= 30) {
		            // charge ₹1 per day for next 30 days
		            charges = extraDays * 1;
		        } else {
		            // charge ₹1 per day for first 30 extra days, then ₹5 per day after that
		            long daysAfter45 = extraDays - 30;
		            charges = (30 * 1) + (daysAfter45 * 5);
		        }

		        // cap charges to book price
		        if (charges > bookPrice) {
		            charges = bookPrice;
		        }
		    }

		  //  System.out.println("Total charges: ₹" + charges);
		    return charges;
		}
    
	    //Fetch all books from DB	
		public List<Book> fetchAllBooks() {
			List<Book> allBook = bookRepo.findAll();
			return allBook;
		}
		
		
		//This method is used by Authentication manager 
		@Override
		public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
			com.vk.entity.Users user = usersRepo.findByEmail(email);
			
			 if (user == null) {
			        throw new UsernameNotFoundException("User not found with email: " + email);
			    }

			    // Create GrantedAuthority for role
			    List<GrantedAuthority> authorities = Collections.singletonList(
			            new SimpleGrantedAuthority("ROLE_" + user.getRole())
			    );
			
			return new User(user.getEmail() , user.getPassword() ,authorities
					                );
			
		}
		
		
		public com.vk.entity.Users findUserByEmail(String mail){
			com.vk.entity.Users user = usersRepo.findByEmail(mail);
			return user;
		}

	
	

}
