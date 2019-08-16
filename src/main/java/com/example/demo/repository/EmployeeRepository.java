package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;

@Repository
@RepositoryRestResource
public interface EmployeeRepository extends CrudRepository<User,Integer> {
	Optional<User> findByEmail(String email);
	
	List<User> findByName(String name);
	
	@Query("Select u from User u where u.name = :name")
	List<User> findByOwnQuery(@Param("name") String name);

	boolean existsByEmail(String string);
}
