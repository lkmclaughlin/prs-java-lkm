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
	
	private static final String Status_Approved = "APPROVED";
	private static final String Status_Rejected = "REJECTED";
	private static final String Status_Review = "REVIEW";
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
	
	//\\//\\//
	
	@SuppressWarnings("rawtypes")
	@PutMapping("review/{id}")
	public ResponseEntity reviewRequest(@PathVariable int id, @RequestBody Request request) {
		String newStatus = request.getTotal() <= 100 ? Status_Approved : Status_Review;
		request.setStatus(newStatus);
		return putRequest(id, request);
		}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("approve/{id}")
	public ResponseEntity approveRequest(@PathVariable int id, @RequestBody Request request) {
		request.setStatus(Status_Approved);
		return putRequest(id, request);
	}

	@SuppressWarnings("rawtypes")
	@PutMapping("reject/{id}")
	public ResponseEntity rejectRequest(@PathVariable int id, @RequestBody Request request) {
		request.setStatus(Status_Rejected);
		return putRequest(id, request);
	}
//\\//\\//\\//\\
	
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity deleteRequest(@PathVariable int id){
		Optional<Request> request = reqRepo.findById(id);
		if(request.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		reqRepo.delete(request.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
	}	
	
}
