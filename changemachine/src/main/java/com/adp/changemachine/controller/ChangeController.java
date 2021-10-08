package com.adp.changemachine.controller;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adp.changemachine.service.ChangeProcess;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
public class ChangeController {
	
	@Autowired
	ChangeProcess changProcess;
	
	 @GetMapping(path = {"/Change"},produces = {"application/json"}, consumes = { MediaType.APPLICATION_JSON_VALUE } )
		public ResponseEntity<Object> getChange(@RequestParam(name = "amount") double amount) throws JsonParseException, JsonMappingException, IOException
		{
		 	int allowedBill[] = {1,2,5,10,20,50,100};
		 	ResponseEntity<Object> responeData = null;
		 	
		 	 if((amount%1)==0) 
		 	 {
		 		 Arrays.sort(allowedBill);
		 		 int res = Arrays.binarySearch(allowedBill, (int)(Math.round(Math.ceil(amount))));
		 		 if(res>=0) 
		 		 {
		 			 int givenNumber = (int) Math.round(amount*100);
				 	 responeData = changProcess.getChange(givenNumber);
		 		 }
		 		 else
		 		 {
		 			responeData = new ResponseEntity<Object>("{\"error\":\"Not Found\",\"error_description\":\"Not Valid number\"}",HttpStatus.BAD_REQUEST);
		 		 }
		 	 }
		 	 else
		 	 {
		 			responeData = new ResponseEntity<Object>("{\"error\":\"Bad Request\",\"error_description\":\"Not Valid number\"}",HttpStatus.BAD_REQUEST);

		 	 }
		 	
			 return responeData;
		
		}
	 
	 @PutMapping(path = {"/CoinNumber"},produces = {"application/json"}, consumes = { MediaType.APPLICATION_JSON_VALUE } )
	 public ResponseEntity<Object> updateCoinMaster (@RequestParam(name = "coinnumber") int number) throws JsonParseException, JsonMappingException, IOException
	 {
		 return changProcess.reFreshCoinNumber(number);
	 }

}
