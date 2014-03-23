package com.bevinisaditch.theinebriator.ClassFiles;

public class TermFrequency {

	//private variables
    Long _id;
    String _term;
    Float _frequency;
     
    // Empty constructor
    public TermFrequency(){
         
    }
    // constructor
    public TermFrequency(Long id, String term, Float frequency){
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
    public Long getID(){
        return this._id;
    }
     
    // setting id
    public void setID(Long id){
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
    
    
	@Override
	public String toString() {
		return "TermFrequency [_id=" + _id + ", _term=" + _term
				+ ", _frequency=" + _frequency + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_frequency == null) ? 0 : _frequency.hashCode());
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result + ((_term == null) ? 0 : _term.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TermFrequency other = (TermFrequency) obj;
		if (_frequency == null) {
			if (other._frequency != null)
				return false;
		} else if (!_frequency.equals(other._frequency))
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (_term == null) {
			if (other._term != null)
				return false;
		} else if (!_term.equals(other._term))
			return false;
		return true;
	}
	
	
	
    
	
}
