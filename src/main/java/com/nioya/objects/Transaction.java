package com.nioya.objects;

public class Transaction {
	
	public double amount;
	public long timestamp;
	public Transaction(double amount, long timestamp) {
		super();
		this.amount = amount;
		this.timestamp = timestamp;
	}
	public Transaction() {
		super();
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	
	

}
