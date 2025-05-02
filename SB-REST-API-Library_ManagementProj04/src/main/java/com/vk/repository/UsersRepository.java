package com.vk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vk.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
 
	public Users findByEmail(String emmail);
}
