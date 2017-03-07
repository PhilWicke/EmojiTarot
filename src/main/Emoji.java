package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Emoji {
	
	String unicode;
	String folderName;
	String path;
	double sentiVal;
	public boolean food = false;
	public boolean love = false;

	public String[] attributeNames = {"Faces","Signs","Objects",
			"Nature","Love","Food"};
	
	public Emoji(String unicode, String folderName, double sentiVal){
		this.unicode = unicode;
		this.sentiVal = sentiVal;
		this.folderName = folderName;
		this.path = "\\images\\"+folderName+"\\"+unicode+".png";
		checkCategory();
	}

	private void checkCategory() {
		String filePath = "./Source/foodLove.txt";
		
		String line;
        BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(filePath));
	        line = in.readLine();
	        while(line != null)
	        {	//System.out.println(line);
	        	String[] content = line.split("\t");
	        	String unicode = content[0];
	        	String food = content[1];
	        	String love = content[2];
	        	
	        	if(unicode.equals(this.unicode) && food.equals("1"))
	        		this.food = true;
	        	if(unicode.equals(this.unicode) && love.equals("1"))
	        		this.love = true;
	        	
	        	line = in.readLine();
	        }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 

}
