package com.maxtrain.bootcamp.prs.vendor;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/vendors")
public class VendorController {

	@Autowired
	private VendorRepository vendRepo;
	
	@GetMapping
	public ResponseEntity<Iterable<Vendor>> getVendors(){
		Iterable<Vendor> vendors = vendRepo.findAll();
		return new ResponseEntity<Iterable<Vendor>>(vendors, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Vendor> getVendor(@PathVariable int id){
		Optional<Vendor> vendor = vendRepo.findById(id);
		if(vendor.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Vendor>(vendor.get(), HttpStatus.OK);
	}

	@GetMapping("code/{code}")
	public ResponseEntity<Vendor> getVendorByCode(@PathVariable String code){
		Optional<Vendor> vendor = vendRepo.findByCode(code);
		if(vendor.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Vendor>(vendor.get(), HttpStatus.OK);
	}		
	
	@PostMapping
	public ResponseEntity<Vendor> postVendor(@RequestBody Vendor vendor){
		Vendor newVendor = vendRepo.save(vendor);
		return new ResponseEntity<Vendor>(newVendor, HttpStatus.CREATED);	
	}
	
	@SuppressWarnings("rawtypes")	
	@PutMapping("{id}")
	public ResponseEntity putVendor(@PathVariable int id, @RequestBody Vendor vendor){
		if(vendor.getId() != id) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		vendRepo.save(vendor);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity deleteVendor(@PathVariable int id){
		Optional<Vendor> vendor = vendRepo.findById(id);
		if(vendor.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		vendRepo.delete(vendor.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
	}
	
}
