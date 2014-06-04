package com.snappydb;

public class SnappydbException extends Exception  {
//	private String theReason;
//	private int errorCode;
	
	private static final long serialVersionUID = 1L;

	public SnappydbException (){
		super();
	}
	
	public SnappydbException (String detailMessage){
		super(detailMessage);
	}
	
//	public SnappydbException(String theReason, int errorCode) {
//		this.theReason = theReason;
//		this.errorCode = errorCode;
//	}
//	
//	public String getTheReason() {
//		return theReason;
//	}
//	public int getErrorCode() {
//		return errorCode;
//	}
	
	
}
