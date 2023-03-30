package com.maxtrain.bootcamp.prs.requestline;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.maxtrain.bootcamp.prs.user.User;

@CrossOrigin
@RestController
@RequestMapping("/api/requestlines")
public class RequestlineController {
	
	@Autowired
	private RequestlineRepository reqlRepo;
	
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
		return new ResponseEntity<Requestline>(newRequestline, HttpStatus.CREATED);	
	}
	
	@SuppressWarnings("rawtypes")	
	@PutMapping("{id}")
	public ResponseEntity putRequestline(@PathVariable int id, @RequestBody Requestline requestline){
		if(requestline.getId() != id) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		reqlRepo.save(requestline);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity deleteRequestline(@PathVariable int id){
		Optional<Requestline> requestline = reqlRepo.findById(id);
		if(requestline.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		reqlRepo.delete(requestline.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
	}

}
