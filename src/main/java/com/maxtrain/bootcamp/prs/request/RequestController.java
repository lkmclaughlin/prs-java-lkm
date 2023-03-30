package com.maxtrain.bootcamp.prs.request;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.maxtrain.bootcamp.prs.user.User;

@CrossOrigin
@RestController
@RequestMapping("/api/requests")
public class RequestController {
	
	@Autowired
	private RequestRepository reqRepo;

	@GetMapping
	public ResponseEntity<Iterable<Request>> getRequest(){
		Iterable<Request> requests = reqRepo.findAll();
		return new ResponseEntity<Iterable<Request>>(requests, HttpStatus.OK);
	}
	@GetMapping("{id}")
	public ResponseEntity<Request> getRequest(@PathVariable int id){
		Optional<Request> request = reqRepo.findById(id);
		if(request.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Request>(request.get(), HttpStatus.OK);		
	}
	
	@PostMapping
	public ResponseEntity<Request> postRequest(@RequestBody Request request){
		Request newRequest = reqRepo.save(request);
		return new ResponseEntity<Request>(newRequest, HttpStatus.CREATED);	
	}
	
	@SuppressWarnings("rawtypes")	
	@PutMapping("{id}")
	public ResponseEntity putRequest(@PathVariable int id, @RequestBody Request request){
		if(request.getId() != id) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		reqRepo.save(request);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity deleteRequest(@PathVariable int id){
		Optional<Request> user = reqRepo.findById(id);
		if(user.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		reqRepo.delete(user.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
	}	
	
}
