package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.MalformedInputException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;



/**
 * This is the main class to run the EmojiTarot application.
 * @author Wicke
 *
 */
public class tarotApp {
	
	static String[] unicodes 	= new String[751];
	static String[] sentiScores = new String[751];
	static String imagePath  	= "./images/";
	
	// Set Emoji images path
	private String emojiPath = "\\images\\faces\\";
	private int numCateg 	 = 4;
	
	// Define mainFrame 
	private JFrame mainFrame;
	private JButton[] categories;
	private JTextPane header;
	private JPanel history;
	private JButton delete;
	private JButton done;
	private JTextPane instr;
	
	
	private JButton[] buttons;
	private JButton refresh;
	// category buttons
	private JFrame frame;
	
	private File folder = new File("./Source/images/faces/");
	private File[] listOfFiles = folder.listFiles();
	private int num_emoji = listOfFiles.length - noPicNum(listOfFiles);
	private String[] unicodes1 = new String[num_emoji];
	
	private String[] catNames = new String[numCateg];

	

/********************************************************************************
*  							MAIN CLASS SECTION	 								*		
********************************************************************************/
	
	public static void main(String[] args){
		
		loadData();
		// init GUI
		tarotApp app = new tarotApp();
		app.makeStartWindow();

//		app.makeComponents();
//		app.makeLayout();
//		app.setVisible();
		
		
	}
	
/********************************************************************************
 *  							START WINDOW SECTION							*		
 ********************************************************************************/

	/**
	 * Builds  a button panel for choosing the categories.
	 * @return catPanel is a JPanel holding the choice buttons
	 */
	private Component makeCatPanel() {
			JPanel catPanel = new JPanel();
			catPanel.setBorder(BorderFactory.createTitledBorder("Categories"));
			catPanel.setLayout(new GridLayout(1,numCateg));
			
			Dimension buttonSize = categories[0].getPreferredSize();
			catPanel.setPreferredSize(new Dimension((int)(buttonSize.getWidth() * 10)+1,
	                (int)(buttonSize.getHeight() * 35)+1 * 2));
			
			catNames[0] = "Faces";
			catNames[1] = "Nature";
			catNames[2] = "Objects";
			catNames[3] = "Signs";

			for(int i = 0; i < numCateg; i++)
			{
				catPanel.add(categories[i]);
			}
			CatListener catListener = new CatListener();	

			for(int i = 0; i < numCateg; i++)
			{
				categories[i].addActionListener(catListener);
			}			
			return catPanel;
	}

	private class CatListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			for(int i = 0; i < numCateg; i++)
				if(e.getSource() == categories[i]) {
					System.out.print(catNames[i]+ " chosen.");
				}
		}

	}




	private void makeStartWindow() {
		mainFrame = new JFrame();
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mainFrame.setUndecorated(true);
		
		JPanel topPanel = new JPanel(new BorderLayout());
		JPanel botPanel = new JPanel(new BorderLayout());
		
		mainFrame.getContentPane().add(topPanel, BorderLayout.PAGE_START);
		mainFrame.getContentPane().add(botPanel, BorderLayout.PAGE_END);
		
		
		// initialize components
		categories	= new JButton[4];
		delete	 	= new JButton("<-");
		done 		= new JButton("DONE");
		
		for(int i = 0; i < numCateg; i++){			
			categories[i] = new JButton(catNames[i]);
		}
		
		botPanel.add(makeCatPanel());
		botPanel.setVisible(true);
		mainFrame.pack();
		mainFrame.setVisible(true);
		
		
		
	}
	
/********************************************************************************
 *   							PROCESSING SECTION								*		
********************************************************************************/
	



	/**
	 * Takes a file and returns the extension as a string
	 * @param file
	 * @return the extension of the file as string
	 */
	private String getExtension(String file){
		String extension = "";

		int i = file.lastIndexOf('.');
		if (i > 0) {
		    extension = file.substring(i+1);
		}
		return extension;	
	}
	
	/**
	 * Returns the number of png pictures in a file
	 * @param listOfFiles
	 * @return integer number of png in file
	 */
	private int noPicNum(File[] listOfFiles){
		String extension;
		int count = 0;
		for (int i = 0; i < listOfFiles.length; i++) {
			extension = getExtension(listOfFiles[i].getName()); 
			if(!(extension.equals("png"))){
				count++;
			}
		}
		return count;
	}

/********************************************************************************
*  							OTHER WINDOWS SECTION							*		
********************************************************************************/
	
	/**
	 * Build interface
	 */
	private void makeComponents() {
		frame = new JFrame("Emoji to Unicode - Keyboard");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		buttons = new JButton[num_emoji];
		refresh = new JButton("X");
		refresh.setBackground(Color.RED);
		refresh.setOpaque(true);
	
		String tempfile;
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				tempfile 	= listOfFiles[i].getName();
				if(tempfile.contains(".png")){
					String[] parts 	= tempfile.split(".png");
					String unicode 	= parts[0];
					unicodes[i] 		= unicode;
				}
			}
		}
		
		for(int i = 0; i < num_emoji; i++){
			String sourcePath = emojiPath+unicodes[i]+".png";
			
			buttons[i] = new JButton(new ImageIcon(getClass().getClassLoader().
					getResource(sourcePath)));
		}
	}


	private JPanel makeEmojiPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Emoji"));
		panel.setLayout(new GridLayout(10, 10));

		for(int i = 0; i < num_emoji; i++)
		{
			panel.add(buttons[i]);
		}
		panel.add(refresh);
		
		ButtonGroup bg = new ButtonGroup();
		for(int i = 0; i < num_emoji; i++)
		{
			bg.add(buttons[i]);
		}
		bg.add(refresh);

		EmojiListener emojiListener = new EmojiListener();

		for(int i = 0; i < num_emoji; i++)
		{
			buttons[i].addActionListener(emojiListener);
		}
		refresh.addActionListener(emojiListener);
		
		return panel;
	}

	private void makeLayout() {
		frame.add(makeEmojiPanel());
		frame.pack();
	}

	private void setVisible() {
		frame.setVisible(true);
	}

	public class EmojiListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			for(int i = 0; i < num_emoji; i++)
				if(e.getSource() == buttons[i]) {
					System.out.print(unicodes[i]+ " ");
				}
				else if(e.getSource() == refresh){
					System.out.println();
				}
		}

	}


	/**
	 * Data for the sentiment analysis ranking has been retrieved form:
	 * http://kt.ijs.si/data/Emoji_sentiment_ranking/
	 * and the processed data is stored in sentiWeb.txt
	 */
	private static void loadData() {
		int count = 0;
		String line;
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
