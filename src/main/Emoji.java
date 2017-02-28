package main;

public class Emoji {
	
	String unicode;
	String folderName;
	String path;
	double sentiVal;
	
	public Emoji(String unicode, String folderName, double sentiVal){
		this.unicode = unicode;
		this.sentiVal = sentiVal;
		this.folderName = folderName;
		this.path = "\\images\\"+folderName+"\\"+unicode+".png";
	}
	 

}
