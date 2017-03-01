package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;



/**
 * This is the main class to run the EmojiTarot application.
 * @author Wicke
 *
 */
public class tarotApp {
	
	final static Dimension SCREENRES = Toolkit.getDefaultToolkit().getScreenSize();
	final static double SCREENWIDTH = SCREENRES.getWidth();
	final static double SCREENHEIGHT = SCREENRES.getHeight();
	static String[] unicodes 	= new String[751];
	static String[] sentiScores = new String[751];
	static String imagePath  	= "./images/";
	static Emoji[] emojiList;
	static int historySize = 18;
	
	// Set Emoji images path
	private String emojiPath;
	private int numCateg 	 = 4;
	
	// Define mainFrame 
	private JFrame mainFrame;
	private JPanel topPanel;
	private JPanel cenPanel;
	private JPanel botPanel;
	private JPanel emoPanel;
	
	private JButton[] categories;
	private JLabel header;
	private JLabel descrip;
	private JLabel leftLabel;
	private JLabel rightLabel;
	private JPanel history;
	private JButton delete;
	private JButton done;
	
	private JButton[] buttons;
	private JButton goBack;
	// category buttons
	private JFrame frame;
	
	private String catPath = "./Source/images/";
	//private File folder;
	//private File[] listOfFiles;
	private int num_emoji;
	private String[] catNames = new String[numCateg];

	

/********************************************************************************
*  							MAIN CLASS SECTION	 								*		
********************************************************************************/
	
	public static void main(String[] args){
		System.out.println(SCREENRES);
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
			catPanel.setPreferredSize(new Dimension((int)(buttonSize.getWidth()),
	                (int)(buttonSize.getHeight() * 10)+1 * 2));
			// category names
			catNames[0] = "Faces";
			catNames[1] = "Nature";
			catNames[2] = "Objects";
			catNames[3] = "Signs";

			for(int i = 0; i < numCateg; i++){
				catPanel.add(categories[i]);
			}
			CatListener catListener = new CatListener();	

			for(int i = 0; i < numCateg; i++){
				
				categories[i].addActionListener(catListener);
			}			
			return catPanel;
	}

	private class CatListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// identify choosen category
			String categ = new String();
			for(int i = 0; i < numCateg; i++){
				if(e.getSource() == categories[i]) {
					categ = catNames[i];
				}
			}
			emoPanel = makeEmojiPanel(categ);
			mainFrame.remove(botPanel);
			mainFrame.revalidate();
			mainFrame.getContentPane().add(emoPanel, BorderLayout.PAGE_END);
			mainFrame.revalidate();
			mainFrame.repaint();
		}
			
		

	}


	private void makeStartWindow() {
		
		System.out.println("Creating main frame...");
		mainFrame = new JFrame();
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mainFrame.setUndecorated(true);
		
		topPanel = new JPanel(new BorderLayout());
		cenPanel = new JPanel();
		botPanel = new JPanel(new BorderLayout());
		
		mainFrame.getContentPane().add(topPanel, BorderLayout.PAGE_START);
		mainFrame.getContentPane().add(cenPanel, BorderLayout.CENTER);
		mainFrame.getContentPane().add(botPanel, BorderLayout.PAGE_END);
				
		System.out.println("Creating top panel...");
		topPanel = createTopPanel(topPanel);
		System.out.println("Creating central panel...");
		cenPanel = createCenPanel(cenPanel);
		System.out.println("Creating bottom panel...");
		botPanel = createBotPanel(botPanel);

		mainFrame.pack();
		mainFrame.getContentPane().setBackground(Color.ORANGE);
		mainFrame.setVisible(true);		
	}
	
private JPanel createBotPanel(JPanel panel) {
		// Initialize components
		categories	= new JButton[4];
		// add the category component
		for(int i = 0; i < numCateg; i++){			
			categories[i] = new JButton();
		}
		// add bottom panel
		panel.add(makeCatPanel());
		for(int i = 0; i < numCateg; i++){			
			categories[i].setText("<html><span style='font-size:15px'>"+catNames[i]+"</span></html>");
		}
		panel.setBorder( new EmptyBorder(0,80,200,80));
		panel.setVisible(true);
		return panel;
	}

private JPanel createCenPanel(JPanel panel) {
		
		// Create the delete button
		delete	 	= new JButton();
		delete.setIcon(new ImageIcon(getClass().getClassLoader().
				getResource("\\images\\guiGraphics\\del.png")));
        delete.setBorder(null);
        delete.setContentAreaFilled(false);
		// Create the done button
		done 		= new JButton();
		done.setIcon(new ImageIcon(getClass().getClassLoader().
				getResource("\\images\\guiGraphics\\ok.png")));
		done.setBorder(null);
        done.setContentAreaFilled(false);
		
		// define central panel
		panel.setLayout(new BoxLayout(panel,BoxLayout.LINE_AXIS));
		history = new JPanel();
		history.setLayout(new BoxLayout(history,BoxLayout.X_AXIS));
		panel.add(history);
		// call method to initialize history
		initHistory(history);
		history.setBorder(BorderFactory.createTitledBorder("Your Emoji History:"));
		
		panel.add(delete);
		panel.add(done);
		//panel.setBackground(Color.CYAN);
		panel.setBorder(BorderFactory.createEmptyBorder(-40, 30, 0, 0));
		panel.setVisible(true);
		return panel;
	}

private JPanel createTopPanel(JPanel panel) {
		String descr = "<html><span style='font-size:18px'>Welcome to the Emoji handreading. Show me your Emoji and I tell you who you are. First, you take your smartphone and open you preferred messenging app. Now go to your history of Emoji and enter them into this program. Click through the categories to find at least 5 of your most used Emoji. If you are done, press <b>DONE</b> and wait for the magic to happen.</span></html>";

		header      = new JLabel("Emoji - Palmistry", SwingConstants.CENTER);
		descrip 	= new JLabel(descr);

		panel.add(header, BorderLayout.CENTER);
		panel.add(descrip, BorderLayout.SOUTH);
		
		header.setFont(new Font("Helvetica",1,45));
		descrip.setBorder(new EmptyBorder(40, 0, 0, 0));
		panel.setBorder(new EmptyBorder(80, 30, 0, 30));
		
		// create icons
		leftLabel = new JLabel(new ImageIcon(getClass().getClassLoader().
				getResource("\\images\\guiGraphics\\header.png")));
		rightLabel = new JLabel(new ImageIcon(getClass().getClassLoader().
				getResource("\\images\\guiGraphics\\header.png")));
		
		// add the header components
		panel.add(leftLabel, BorderLayout.WEST);
		panel.add(rightLabel, BorderLayout.EAST);
		
		return panel;
	}

private void initHistory(JPanel history) {
			// define placholder Emoji
			Emoji placeholder = new Emoji("empty","guiGraphics",0);
			emojiList = new Emoji[historySize];
			
			// loop though Emoji list: insert placeholder and add it as icon
			for (int i = 0; i < emojiList.length; i++) {
				emojiList[i] = placeholder; 
				JLabel tempLab = new JLabel(new ImageIcon(getClass().getClassLoader().
						getResource(emojiList[i].path)));
				history.add(tempLab);
			}
		
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
	private void loadCategoryImages(String category) {
		
		catPath = "./Source/images/"+category+"/";
		emojiPath = "\\images\\"+category+"\\";
		System.out.println(catPath);
		File folder = new File(catPath);
		File[] listOfFiles = folder.listFiles();
		num_emoji = listOfFiles.length - noPicNum(listOfFiles);
	
		buttons = new JButton[num_emoji];
		goBack = new JButton("RETURN");
		goBack.setBackground(Color.RED);
		goBack.setOpaque(true);
	
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
			// retrieve icon and adjust size
			ImageIcon buttonIcon = new ImageIcon(getClass().getClassLoader().
					getResource(sourcePath));
			Image buttonImage = buttonIcon.getImage();
			buttonImage = buttonImage.getScaledInstance(buttonImage.getWidth(null)/2,
					buttonImage.getHeight(null)/2, Image.SCALE_SMOOTH);
			buttonIcon.setImage(buttonImage);
			// create button with icon
			buttons[i] = new JButton(buttonIcon);
		}
	}


	private JPanel makeEmojiPanel(String categ) {
		loadCategoryImages(categ);
		
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(categ));
		panel.setLayout(new GridLayout(10, 10));

		for(int i = 0; i < num_emoji; i++)
		{
			panel.add(buttons[i]);
		}
		panel.add(goBack);
		
		ButtonGroup bg = new ButtonGroup();
		for(int i = 0; i < num_emoji; i++)
		{
			bg.add(buttons[i]);
		}
		bg.add(goBack);

		EmojiListener emojiListener = new EmojiListener();

		for(int i = 0; i < num_emoji; i++)
		{
			buttons[i].addActionListener(emojiListener);
		}
		goBack.addActionListener(emojiListener);
		
		panel.setMaximumSize(new Dimension((int)SCREENWIDTH ,30));
		return panel;
	}

	private void setVisible() {
		frame.setVisible(true);
	}

	public class EmojiListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			for(int i = 0; i < num_emoji; i++)
				if(e.getSource() == buttons[i]) {
					// TODO: Emoji Button functionality
					System.out.print(unicodes[i]+ " ");
				}
				else if(e.getSource() == goBack){
					callMainFrame();
				}
		}

		private void callMainFrame() {
			mainFrame.remove(emoPanel);
			mainFrame.revalidate();
			mainFrame.getContentPane().add(botPanel, BorderLayout.PAGE_END);
			mainFrame.revalidate();
			mainFrame.repaint();
			
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
