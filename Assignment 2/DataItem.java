/*
	DataItem.java
	
	David Bover, July 2016
	
	Class for use in CSCI 241 Assignment 2
	Objects to be stored in a hash table
	
	*/


public class DataItem {

	String word;
	int count;		// occurrence count of this word

	public DataItem(String word) {
		this.word = word;
		this.count = 1;
	}

}