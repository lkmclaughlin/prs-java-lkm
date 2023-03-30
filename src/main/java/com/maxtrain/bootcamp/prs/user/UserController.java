package com.maxtrain.bootcamp.prs.user;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.maxtrain.bootcamp.prs.vendor.Vendor;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserRepository userRepo;
	
	@GetMapping
	public ResponseEntity<Iterable<User>> getUsers(){
		Iterable<User> users = userRepo.findAll();
		return new ResponseEntity<Iterable<User>>(users, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<User> getUser(@PathVariable int id){
		Optional<User> user = userRepo.findById(id);
		if(user.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(user.get(), HttpStatus.OK);		
	}
	
	@PostMapping
	public ResponseEntity<User> postUser(@RequestBody User user){
		User newUser = userRepo.save(user);
		return new ResponseEntity<User>(newUser, HttpStatus.CREATED);	
	}
	
	@SuppressWarnings("rawtypes")	
	@PutMapping("{id}")
	public ResponseEntity putUser(@PathVariable int id, @RequestBody User user){
		if(user.getId() != id) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		userRepo.save(user);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity deleteUser(@PathVariable int id){
		Optional<User> user = userRepo.findById(id);
		if(user.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		userRepo.delete(user.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
	}
	
}
