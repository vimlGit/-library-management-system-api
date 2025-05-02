package com.vk.entity;

import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="Users01")
public class Users {

	@Id
	@SequenceGenerator(name="userSeq1" , sequenceName ="useq01" , initialValue = 10000, allocationSize =1  )	
	@GeneratedValue(generator = "userSeq1" ,strategy=GenerationType.SEQUENCE)
	private Long uid ;
	private String name;
	private String email;
	private String password;
	
  //Librarian or Student
	private String role;
	/*@ElementCollection
	@CollectionTable(
			   name="roletabs" ,
		       joinColumns =@JoinColumn(name = "id")
			)
	private Set<String> roles;*/
}
