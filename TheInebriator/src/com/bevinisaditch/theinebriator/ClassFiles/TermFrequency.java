package com.bevinisaditch.theinebriator.ClassFiles;

public class TermFrequency {

	//private variables
    int _id;
    String _term;
    Float _frequency;
     
    // Empty constructor
    public TermFrequency(){
         
    }
    // constructor
    public TermFrequency(int id, String term, Float frequency){
        this._id = id;
        this._term = term;
        this._frequency = frequency;
    }
     
    // constructor
    public TermFrequency(String term, Float frequency){
        this._term = term;
        this._frequency = frequency;
    }
    // getting ID
    public int getID(){
        return this._id;
    }
     
    // setting id
    public void setID(int id){
        this._id = id;
    }
     
    // getting term
    public String getTerm(){
        return this._term;
    }
     
    // setting term
    public void setTerm(String name){
        this._term = name;
    }
     
    // getting term frequency
    public Float getFrequency(){
        return this._frequency;
    }
     
    // setting term frequency
    public void setFrequency(Float frequency){
        this._frequency = frequency;
    }
	
}
