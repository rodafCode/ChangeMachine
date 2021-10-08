package com.adp.changemachine.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ChangeProcess {
	
	private static Map<Integer, Integer> coinMaster = null;
	private static Map<Integer, Integer> transactionCoin = null;
	private static int breakAmount=0;
	static {
		coinMaster = new LinkedHashMap<Integer, Integer>();
		coinMaster.put(1, 100);
		coinMaster.put(5, 100);
		coinMaster.put(10, 100);
		coinMaster.put(25, 100);
	}
	
	public ResponseEntity<Object>  getChange(int InputAmount) throws JsonProcessingException
	{
		
		
		int givenNumber = InputAmount;
		breakAmount=0;
		transactionCoin=new LinkedHashMap<Integer, Integer>();
		Set<Integer> keyset = coinMaster.keySet();
		
		for (Integer coin : keyset) {
			
			if(breakAmount==InputAmount)
				break;
			
			if((int)coinMaster.get(coin)>0)
				givenNumber = cents(givenNumber,(int)coin, (int)coinMaster.get(coin));
			
		}
		System.out.println(breakAmount+"="+InputAmount+"==="+(breakAmount!=InputAmount));
		
		if(breakAmount!=InputAmount)
		{
			return new ResponseEntity<Object>("{\"error\":\"Insufficient fund\",\"error_description\":\"No sufficient Change\"}",HttpStatus.BAD_REQUEST);
		}
		else
		{
			return responseData();
		}
//		transactionCoin.forEach((k,v) -> {
//			System.out.println("key:"+k+" & value:"+v);
//		});
		//return null;
	}
	
	
	
	private int cents(int givenNumber,int cent, int coinNumber)
	{
	
		int pennis  = (int) Math.round(givenNumber/cent);
		
		double amtpennis=0.0;
		
		if(pennis>coinNumber) {
			pennis=coinNumber;
			amtpennis=(double) (cent*pennis);
			givenNumber= (int)(givenNumber-amtpennis);
		}
		else {
			amtpennis=(double) (cent*pennis);
			givenNumber=(int) (givenNumber-amtpennis);
		}
		
		System.out.println(cent+"*"+(int)pennis+"="+amtpennis/100);
		
		transactionCoin.put(cent, pennis);
		
		breakAmount=(int) (breakAmount+amtpennis);
		return (int) givenNumber;
	}
	
	private ResponseEntity<Object> responseData() throws JsonProcessingException
	{
		Map<String,String> responseMap = new HashMap<String,String>();
		
		transactionCoin.forEach((k,v) -> {
			
			int coinUsed = coinMaster.get(k)-v;
			coinMaster.put(k, coinUsed);
			
			System.out.println(String.valueOf(k+"=="+((double)k/100))+","+ String.valueOf((k*v)/100));
			
			responseMap.put(String.valueOf((double)k/100), String.valueOf((k*v)/100));
			
		});
		
		String json = new ObjectMapper().writeValueAsString(responseMap);
		return new ResponseEntity<Object>(json,HttpStatus.OK);
	}
	
	public ResponseEntity<Object> reFreshCoinNumber(int newCoinNumber)
	{
		String json=null;
		try {
			coinMaster.forEach((k,v) -> {
				coinMaster.put(k,newCoinNumber);
			});
			
			json = new ObjectMapper().writeValueAsString(coinMaster);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Object>(json,HttpStatus.OK);
	}

}
