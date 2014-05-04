package com.bevinisaditch.theinebriator.ClassFiles;

/**
 * Data structure of a term name and the frequency of which it occurs. Used by Ranker/Search Engine
 * 
 * @author michael
 *
 */
public class TermFrequency {

	//private variables
    Long _id;
    String _term;
    Float _frequency;
     
    /**
     * Empty Constructor
     */
    public TermFrequency(){
         
    }
    
    /**
     * Constructor
     * 
     * @param id - Id of term (from database)
     * @param term - Name of the term (String)
     * @param frequency - Frequency the term occurs (float)
     */
    public TermFrequency(Long id, String term, Float frequency){
        this._id = id;
        this._term = term;
        this._frequency = frequency;
    }
     
    /**
     * Constructor
     * 
     * @param term - Name of the term (String)
     * @param frequency - Frequency the term occurs (float)
     */
    public TermFrequency(String term, Float frequency){
        this._term = term;
        this._frequency = frequency;
    }
    
    /**
     * @return id of the term frequency
     */
    public Long getID(){
        return this._id;
    }
     
    /**
     * SetID
     * 
     * @param id - ID to be set (from database)
     */
    public void setID(Long id){
        this._id = id;
    }
     
    /**
     * getTerm
     * 
     * @return name of the term (String)
     */
    public String getTerm(){
        return this._term;
    }
     
    /**
     * setTerm()
     * 
     * @param name - Name of the term (String)
     */
    public void setTerm(String name){
        this._term = name;
    }
     
    /**
     * getFrequency()
     * 
     * @return frequency of the term (Float)
     */
    public Float getFrequency(){
        return this._frequency;
    }
     
    /**
     * setFrequency()
     * 
     * @param frequency - frequency the term occurs (float)
     */
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
