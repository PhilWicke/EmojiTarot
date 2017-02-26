package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;


/**
 * This is the main class to run the EmojiTarot application.
 * Data for the sentiment analysis ranking has been retrieved form:
 * http://kt.ijs.si/data/Emoji_sentiment_ranking/
 * and the processed data is stored in sentiWeb.txt
 * 
 * @author Wicke
 *
 */
public class tarotApp {
	
	public static void main(String[] args){
		
		int count = 0;
		String line;
		String filePath = "./images/";
		String[] unicodes 	= new String[751];
		String[] sentiScores= new String[751];
		
		try {
			FileInputStream fis = new FileInputStream("sentiWeb.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
			
	        while((line=br.readLine())!=null){
	        	String[] lineData = line.split("\t");
	        	unicodes[count] = lineData[2];
	        	sentiScores[count] = lineData[8];
	        	count++;
	        }
		br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
	}

}
