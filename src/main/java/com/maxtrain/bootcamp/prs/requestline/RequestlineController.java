package com.maxtrain.bootcamp.prs.requestline;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.maxtrain.bootcamp.prs.request.Request;
import com.maxtrain.bootcamp.prs.product.Product;
import com.maxtrain.bootcamp.prs.request.RequestRepository;
import com.maxtrain.bootcamp.prs.product.ProductRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/requestlines")
public class RequestlineController {
	
	@Autowired
	private RequestlineRepository reqlRepo;
	@Autowired
	private RequestRepository reqRepo;	
	@Autowired
	private ProductRepository prodRepo;
	
	private boolean recalcRequestTotal(int requestId) {
		Optional<Request> theRequest = reqRepo.findById(requestId);
		if(theRequest.isEmpty()) {
			return false;
		}
		Request request = theRequest.get();
		Iterable<Requestline> requestlines = reqlRepo.findByRequestId(requestId);
		double total = 0;
		for(Requestline rl : requestlines) {
			if(rl.getProduct().getName() == null) {
				Product product = prodRepo.findById(rl.getProduct().getId()).get();
				rl.setProduct(product);
			}
			total += rl.getQuantity() * rl.getProduct().getPrice();
		}
		request.setTotal(total);
		reqRepo.save(request);
		
		return true;
	}
	
	
	@GetMapping
	public ResponseEntity<Iterable<Requestline>> getRequestlines(){
		Iterable<Requestline> requestlines = reqlRepo.findAll();
		return new ResponseEntity<Iterable<Requestline>>(requestlines, HttpStatus.OK);
	}

	@GetMapping("{id}")
	public ResponseEntity<Requestline> getRequestline(@PathVariable int id){
		Optional<Requestline> requestline = reqlRepo.findById(id);
		if(requestline.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Requestline>(requestline.get(), HttpStatus.OK);		
	}
	
	@PostMapping
	public ResponseEntity<Requestline> postRequestline(@RequestBody Requestline requestline){
		Requestline newRequestline = reqlRepo.save(requestline);
		reqlRepo.findById(newRequestline.getId());
		Optional<Request> request = reqRepo.findById(requestline.getRequest().getId());
		if(!request.isEmpty()) {
			boolean success = recalcRequestTotal(request.get().getId());
			if(!success) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<Requestline>(newRequestline, HttpStatus.CREATED);	
	}
	
	@SuppressWarnings("rawtypes")	
	@PutMapping("{id}")
	public ResponseEntity putRequestline(@PathVariable int id, @RequestBody Requestline requestline){
		if(requestline.getId() != id) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		reqlRepo.save(requestline);
		Optional<Request> request = reqRepo.findById(requestline.getRequest().getId());
		if(!request.isEmpty()) {
			boolean success = recalcRequestTotal(request.get().getId());
			if(!success) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings({ "rawtypes", "null" })
	@DeleteMapping("{id}")
	public ResponseEntity deleteRequestline(@PathVariable int id){
		Optional<Requestline> requestline = reqlRepo.findById(id);
		if(requestline.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		reqlRepo.delete(requestline.get());
		Optional<Request> request = reqRepo.findById(requestline.get().getRequest().getId());
		if(!request.isEmpty()) {
			boolean success = recalcRequestTotal(request.get().getId());
			if(!success) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
	}
	

}
