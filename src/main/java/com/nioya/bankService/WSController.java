package com.nioya.bankService;

import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nioya.objects.Statistics;
import com.nioya.objects.Transaction;
import com.nioya.others.Utils;

@Controller
public class WSController {
	final static Logger logger = Logger.getLogger(WSController.class);


	private Stack<Transaction> transactions = new Stack<Transaction>();
    private ConcurrentHashMap<String, Statistics> statistics = new ConcurrentHashMap<String, Statistics>(); // to avoid any overlapping, use concurrentHashMap instead of traditional one :)

    
    @GetMapping("/statistics")
    @ResponseBody
    public Statistics getStatistics(HttpServletRequest request) {
    	Utils utils = new Utils();
		String[] hashKeySet = utils.getHashKeySet(); // Get HashKey Set.
    	Statistics resultSet = new Statistics();
		for (int i = 0; i < hashKeySet.length; i++) { // Length of hashKeySet always will be constant (60). That's why complexity of this loop will be O(1)
			Statistics tempStat =  statistics.get(hashKeySet[i]);
			if(tempStat != null) {
				resultSet.setCount(resultSet.getCount() + tempStat.getCount());
				resultSet.setSum(resultSet.getSum() + tempStat.getSum());
				if(tempStat.getMax() > resultSet.getMax()) {
					resultSet.setMax(tempStat.getMax());
		    	}
		    	if(tempStat.getMin() < resultSet.getMin() || resultSet.getMin() == 0) {
		    		resultSet.setMin(tempStat.getMin());
		    	}				
			}

	    	
		}

		double avg = 0;
		if(resultSet.getCount() > 0) {
			avg = resultSet.getSum() / resultSet.getCount();
		}
		resultSet.setAvg(avg);
		logger.info("Function: statistics, Status: "+HttpStatus.OK+". Client IP: " + utils.getClientIP(request));
    	return resultSet;
    }
    
    
    @PostMapping("/transactions")
    @ResponseBody
    public ResponseEntity<Object> setTransaction(HttpServletRequest request,@RequestBody Transaction transaction) {
    	Utils utils = new Utils();
    	boolean timestampChecker = utils.timestampChecker(transaction.getTimestamp()); // check timestamp is in last 60 secs.
    	if(timestampChecker) {
    		String hashkeyFromTimestamp = utils.getHashKeyFromTime(transaction.getTimestamp()); // generete hashkey from timestamp
    		Statistics temp = statistics.get(hashkeyFromTimestamp);
    		if(temp== null) {
    			temp = new Statistics();
    		}
			temp.sum += transaction.getAmount();
			if(transaction.getAmount() > temp.max) {
	    		temp.max = transaction.getAmount();
	    	}
	    	if(transaction.getAmount() < temp.min || temp.min == 0) {
	    		temp.min = transaction.getAmount();
	    	}
	    	temp.count++;
    		statistics.put(hashkeyFromTimestamp, temp);
    		
    		
    		transactions.push(transaction);
    		logger.info("Function: transactions, Status: "+HttpStatus.CREATED+". Client IP: " + utils.getClientIP(request));
    		return new ResponseEntity<>(null, HttpStatus.CREATED);

    	}else {
    		logger.info("Function: transactions, Status: "+HttpStatus.NO_CONTENT+". Client IP: " + utils.getClientIP(request));
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    	}

    }

}
