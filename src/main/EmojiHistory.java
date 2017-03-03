package main;

import java.util.ArrayList;

public class EmojiHistory extends ArrayList<Emoji>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int maxSize;
	private Emoji placeholder = new Emoji("empty","guiGraphics",0);
	
	public EmojiHistory(int size) {
		setMaxSize(size);
	}
	
	public void setMaxSize(int size){
		this.maxSize = size;
	}
	public int getMaxSize(){
		return this.maxSize;
	}
	
	public void removeAllPlaceholder(){
		while(this.remove(this.placeholder));
	}

	public void fillPlaceholder() {
		while(this.size()<this.maxSize){
			this.add(this.placeholder);
		}
	}
	/**
	 * Method that checks if the last entry at position maxSize
	 * is the empty placeholder Emoji, i.e. if the last Emoji 
	 * is not the empty placeholder, the history is full.
	 * @return boolean true if the list is full
	 */
	public boolean isFull() {
		return !(this.get(maxSize-1).unicode.equals("empty"));
	}
}
