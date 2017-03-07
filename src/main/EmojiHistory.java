package main;

import java.util.ArrayList;

public class EmojiHistory extends ArrayList<Emoji>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int maxSize;
	private Emoji placeholder = new Emoji("empty","guiGraphics",0);
	
	public String[] attributeNames = {"Faces","Signs","Objects",
			"Nature","Food","Love"};
	
	private int[] attributes= new int[6];
	
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
	
	public boolean isEmpty(){
		return this.get(0).unicode.equals("empty");
	}
	
	public boolean atLeastFive(){
		return this.get(4).unicode.equals("empty");
	}

	public int getMeanSenti() {
		int meanSenti = 0;
		int i = 0;
		for (Emoji emoji : this) {
			if (!emoji.unicode.equals("empty") && emoji.sentiVal!=0){
				meanSenti = (int) (meanSenti + (emoji.sentiVal*100));
				i++;
			}
		}
		return (int) meanSenti/i;
	}

	public int[] getAttributes() {
		for (int i = 0; i < attributes.length; i++) {
			attributes[i] = sumCategory(i);
		}
		return attributes;
	}

	private int sumCategory(int i) {
		int sum = 0;
		for (Emoji emoji : this) {
			if (!emoji.unicode.equals("empty")){
				switch (emoji.folderName) {
				case "Faces":
					if(i==0)sum++;break;
				case "Signs":
					if(i==1)sum++;break;
				case "Objects":
					if(i==2)sum++;break;
				case "Nature":
					if(i==3)sum++;break;
				default:
					break;
				}
			if(i == 4 && emoji.food)
				sum++;
			if(i == 5 && emoji.love)
				sum++;
			}
		}
		return sum;
	}
}
