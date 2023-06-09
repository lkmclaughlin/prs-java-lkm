package com.maxtrain.bootcamp.prs.request;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/requests")
public class RequestController {
	
	private final String Status_New = "NEW";
	private final String Status_Approved = "APPROVED";
	private final String Status_Rejected = "REJECTED";
	private final String Status_Review = "REVIEW";
	
	@Autowired
	private RequestRepository reqRepo;	

	@GetMapping
	public ResponseEntity<Iterable<Request>> getRequests(){
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
	
	@GetMapping("reviews/!{userId}")
	public ResponseEntity<Iterable<Request>> getRequestsInReview(@PathVariable int userId){
		Iterable<Request> requestsInReview = reqRepo.findByStatusAndUserIdNot(Status_Review, userId);
		return new ResponseEntity<Iterable<Request>>(requestsInReview, HttpStatus.OK);
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
		String newStatus = request.getTotal() <= 50 ? Status_Approved : Status_Review;
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
//\\//\\//
	
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
