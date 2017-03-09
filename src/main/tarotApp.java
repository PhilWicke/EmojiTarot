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
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;



/**
 * This is the main class to run the EmojiTarot application.
 * @author Wicke
 *
 */
public class tarotApp {
	
	final static Dimension SCREENRES = Toolkit.getDefaultToolkit().getScreenSize();
	final static double SCREENWIDTH = SCREENRES.getWidth();
	final static double SCREENHEIGHT = SCREENRES.getHeight();
	
	static String[] sentiCodes 	= new String[751];
	static double[] sentiScores = new double[751];
	
	static String[] unicodes = new String[800];
	static String imagePath  	= "./images/";
	//static Emoji[] emojiList;
	static EmojiHistory emojiHist;
	static int historySize = 18;
	
	// Set Emoji images path
	private String emojiPath;
	private int numCateg 	 = 4;
	private String currentCat;
	
	// Define mainFrame 
	private JFrame mainFrame;
	private JPanel topPanel;
	private JPanel cenPanel;
	private JPanel botPanel;
	private JSplitPane evalPanel;
	private JPanel emoPanel;
	
	private JButton[] categories;
	private JLabel header;
	private JLabel descrip;
	private JLabel leftLabel;
	private JLabel rightLabel;
	private JPanel history;
	private JButton delete;
	private JButton done;
	private JButton restart;
	
	// Four categories with data fields for 
	// buttons and unicodes
	private JButton[] buttonsFaces;
	private String[] unicodesFaces;
	private JButton[] buttonsNature;
	private String[] unicodesNature;
	private JButton[] buttonsObjects;
	private String[] unicodesObjects;
	private JButton[] buttonsSigns;
	private String[] unicodesSigns;
	private JButton goBack;
	
	private String catPath = "./Source/images/";
	private int num_emoji;
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
			catPanel.setPreferredSize(new Dimension((int)(buttonSize.getWidth()),
	                (int)(buttonSize.getHeight() * 8)+1 * 2));
			// category names
			catNames[0] = "Faces";
			catNames[1] = "Nature";
			catNames[2] = "Objects";
			catNames[3] = "Signs";
			
			// preload buttons
			for(int i = 0; i < numCateg; i++){
				System.out.println("Loading >"+catNames[i]+"< buttons.");
				currentCat = catNames[i];
				loadCategoryImages();
			}
			
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
			// identify chosen category
			for(int i = 0; i < numCateg; i++){
				if(e.getSource() == categories[i]) {
					currentCat = catNames[i];
				}
			}
			
			emoPanel = makeEmojiPanel();
			mainFrame.remove(botPanel);
			mainFrame.revalidate();
			mainFrame.getContentPane().add(emoPanel, BorderLayout.PAGE_END);
			mainFrame.revalidate();
			mainFrame.repaint();
		}
	}
	
	public class EmojiListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton[] buttons = null;
			buttons = getButtonsCat(buttons);
			unicodes = getUnicodesCat(unicodes);
			
			for(int i = 0; i < num_emoji; i++){
				if(e.getSource() == buttons[i]) {
					addToHistory(unicodes[i]);
					buttons[i].setEnabled(false);
					System.out.print(unicodes[i]+ " ");
				}
				else if(e.getSource() == goBack){
					callMainFrame();
				}
			}
			// block all buttons if history is full
			if (emojiHist.isFull()){
				openAllButtons(false);
			}
		}
	}
	
	private class restartListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			history.removeAll();
			initHistory(history);
			emojiHist.clear();
			emojiHist.fillPlaceholder();
			openAllButtons(true);
			history.revalidate();
			history.repaint();
			
			// rebuild done button
			done 		= new JButton();
			done.setIcon(new ImageIcon(getClass().getClassLoader().
					getResource("\\images\\guiGraphics\\ok.png")));
			done.setBorder(null);
	        done.setContentAreaFilled(false);
			
	        doneListener dListener = new doneListener();
	        done.addActionListener(dListener);
	        
			cenPanel.remove(restart);
			cenPanel.add(done);
			delete.setEnabled(true);
			
			
			mainFrame.getContentPane().remove(evalPanel);
			botPanel = new JPanel(new BorderLayout());

			mainFrame.getContentPane().add(botPanel, BorderLayout.PAGE_END);
			botPanel = createBotPanel(botPanel);
			cenPanel.revalidate();
			cenPanel.repaint();
			
			// reset top panel
			String startMsg = "<html><span style='font-size:18px'>Welcome to the Emoji handreading. Show me your Emoji and I tell you who you are. First, you take your smartphone and open you preferred messenging app. Now go to your history of Emoji and enter them into this program. Click through the categories to find at least 5 of your most used Emoji. If you are done, press <b>OK</b> and wait for the magic to happen.</span></html>";
			topPanel.removeAll();
			topPanel = createTopPanel(topPanel, startMsg);
			
			topPanel.revalidate();
			topPanel.repaint();
			
		}
	}
	
	private class deleteListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			history.removeAll();
			initHistory(history);
			emojiHist.clear();
			emojiHist.fillPlaceholder();
			openAllButtons(true);
			history.revalidate();
			history.repaint();
		}
	}

	private class doneListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
				
				if (!emojiHist.atLeastFive()){
					history.setOpaque(false);
					history.revalidate();
					history.repaint();
					
					restart = new JButton();
					restart.setIcon(new ImageIcon(getClass().getClassLoader().
						getResource("\\images\\guiGraphics\\restart.png")));
					restart.setBorder(null);
					restart.setContentAreaFilled(false);
				
					restartListener restListener = new restartListener();
					restart.addActionListener(restListener);
					cenPanel.remove(done);
					cenPanel.add(restart);
					cenPanel.setBorder(new EmptyBorder(10,30,10,10));
					delete.setEnabled(false);

					cenPanel.revalidate();
					cenPanel.repaint();
					
					String endMsg = "<html><span style='font-size:18px'>Thank you for showing me your Emoji history. Below you find the analysis of what they reveal to me about you.</span></html>";
					topPanel.removeAll();
					topPanel = createTopPanel(topPanel, endMsg);
					
					topPanel.revalidate();
					topPanel.repaint();
					
					evaluate();
				}
				else{
					history.setOpaque(true);
					history.setBackground(new Color(230, 130, 130));
					history.revalidate();
					history.repaint();
				}
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
		String startMsg = "<html><span style='font-size:18px'>Welcome to the Emoji handreading. Show me your Emoji and I tell you who you are. First, you take your smartphone and open you preferred messenging app. Now go to your history of Emoji and enter them into this program. Click through the categories to find at least 5 of your most used Emoji. If you are done, press <b>OK</b> and wait for the magic to happen.</span></html>";
		topPanel = createTopPanel(topPanel, startMsg);
		System.out.println("Creating central panel...");
		cenPanel = createCenPanel(cenPanel);
		System.out.println("Creating bottom panel...");
		botPanel = createBotPanel(botPanel);

		mainFrame.pack();
		mainFrame.setVisible(true);		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void evaluate() {
		
		int meanSenti = emojiHist.getMeanSenti();
		
		String analysisDesc = 
		"<html><head><style>div(text-align:justify;)</style></head><body><div>Your sentiment score has been evaluted using the"
		+ " Emoji Sentiment Ranking (Kralj Novak P, Smailović J, Sluban B,"
		+ " Mozetič I (2015) Sentiment of Emojis. PLoS ONE 10(12): e0144296."
		+ " doi:10.1371/journal.pone.0144296). This ranking has been "
		+ " evaluated analyzing 1.6 million tweets in 13 European languages"
		+ " by the sentiment polarity (negative, neutral, positive). You"
		+ " have a score of "+meanSenti+" in the range between -100"
		+ " (strongly negative) and +100 (strongly positive). <br>"
		+ " On the right side you see your distribution of Emoji. It "
		+ " shows the categories you mostly use based on your given "
		+ " history of Emoji.</div></body></html>";
		
//		String analysisDesc = 
//		"<html><body><div>Your sentiment score has been evaluted using the"
//		+ " Emoji Sentiment Ranking <br>(Kralj Novak P, Smailović J, Sluban B,"
//		+ " Mozetič I (2015) Sentiment of Emojis. <br>PLoS ONE 10(12): e0144296."
//		+ " doi:10.1371/journal.pone.0144296). This rank-<br>ing has been "
//		+ " evaluated analyzing 1.6 million tweets in 13 European lan-<br>guages"
//		+ " by the sentiment polarity (negative, neutral, positive). You"
//		+ " have a score<br> of "+meanSenti+" in the range between -100"
//		+ " (strongly negative) and +100 (strongly positive). <br><br>"
//		+ " On the right side you see your distribution of Emoji. It "
//		+ " shows the categories you<br> mostly use based on your given "
//		+ " history of Emoji.</div></body></html>";

		evalPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		
		// Define slider area
		JSplitPane sliderPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		sliderPanel.setDividerSize(0);
		JLabel sliderHeader = new JLabel("Your Emoji Sentiment Score: "+meanSenti,SwingConstants.CENTER);
		sliderHeader.setFont(new Font("SansSerif", Font.BOLD, 20));
		sliderPanel.add(sliderHeader);
		
		// define chart area
		JSplitPane chartPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		chartPanel.setDividerSize(0);
		JLabel chartDesc = new JLabel(analysisDesc);
		chartPanel.setResizeWeight(0.4);
		chartDesc.setFont(new Font("SansSerif", Font.PLAIN, 15));
		chartDesc.setBorder(new EmptyBorder(0,20,0,20));
		chartPanel.add(chartDesc);
		
		// Define pie chart	
	    org.knowm.xchart.PieChart chart = new PieChartBuilder().
	    		width(100).height(300).title("Your Emoji Distribution").
	    		build();
	    
	    // Customize Chart
	    Color[] sliceColors = new Color[] {
	    		new Color(210,180,140),	// ceramic
	    		new Color(60,179,113), // green
	    		new Color(178,34,34), // red
	    		new Color(147,112,219), // violett
	    		new Color(238,221,130),  // yellow
	    		new Color(100,149,237)}; // blue
	    chart.getStyler().setSeriesColors(sliceColors);
	 
	    // Series
	    int[] attributes = emojiHist.getAttributes();
	    for (int i = 0; i < 6; i++) {
	    	
	    	int value = attributes[i];
	    	String seriesName = emojiHist.attributeNames[i];
			chart.addSeries(seriesName, value);
			System.out.println(value);
		}
//	    chart.addSeries("Faces", 24);
//	    chart.addSeries("Signs", 21);
//	    chart.addSeries("Objects", 39);
//	    chart.addSeries("Nature", 17);
//	    chart.addSeries("Food", 20);
//	    chart.addSeries("Love", 20);

		
		// Define slider
		JSlider slider = createSlider(meanSenti);

		evalPanel.setResizeWeight(0.7);
		evalPanel.setEnabled(true);
		evalPanel.setDividerSize(0);
		
		sliderPanel.add(slider);
		chartPanel.add(new XChartPanel(chart));
		evalPanel.add(sliderPanel);
		evalPanel.add(chartPanel);
		evalPanel.setBorder(new EmptyBorder(0, 30, 20, 30));


		mainFrame.remove(botPanel);
		mainFrame.remove(emoPanel);
		mainFrame.revalidate();
		//mainFrame.repaint();
		mainFrame.getContentPane().add(evalPanel, BorderLayout.PAGE_END);		
		mainFrame.revalidate();
		mainFrame.repaint();
		
		
	}

	private JSlider createSlider(int meanSenti) {
		JSlider slider = new JSlider(JSlider.HORIZONTAL, -100, 100, meanSenti);
		slider.setEnabled(false);
		slider.setMinorTickSpacing(5);
		slider.setMajorTickSpacing(10);
		slider.setPaintTicks(true);
		slider.setPaintTrack(true);
		slider.setPaintLabels(true);
		slider.setToolTipText("Emoji Sentiment Score: "+meanSenti);
		slider.setUI(new MySliderUI(slider));
		
		 //Create the label table.
		        Hashtable<Integer, JLabel> labelTable = 
            new Hashtable<Integer, JLabel>();
        Hashtable<Integer, ImageIcon> iconTable = 
                new Hashtable<Integer, ImageIcon>();
        
        // set marker point
        ImageIcon veryNegIcon = new ImageIcon(getClass().getClassLoader().
         		 getResource("\\images\\guiGraphics\\veryNeg.png"));
        iconTable.put(-100, veryNegIcon);
        labelTable.put(-100,new JLabel(veryNegIcon));
        
        // set marker point
        ImageIcon slightNegIcon = new ImageIcon(getClass().getClassLoader().
        		 getResource("\\images\\guiGraphics\\slightlyNeg.png"));
        iconTable.put(-50, slightNegIcon);
        labelTable.put(-50,new JLabel(slightNegIcon));
        
        // set marker point
        ImageIcon neutralIcon = new ImageIcon(getClass().getClassLoader().
       		 getResource("\\images\\guiGraphics\\neutral.png"));
        iconTable.put(0, neutralIcon);
        labelTable.put(0,new JLabel(neutralIcon));
       
        // set marker point
        ImageIcon slightPosIcon = new ImageIcon(getClass().getClassLoader().
      		 getResource("\\images\\guiGraphics\\slightlyPos.png"));
        iconTable.put(50, slightPosIcon);
        labelTable.put(50,new JLabel(slightPosIcon));
      
        // set marker point
        ImageIcon veryPosIcon = new ImageIcon(getClass().getClassLoader().
     		 getResource("\\images\\guiGraphics\\veryPos.png"));
        iconTable.put(100, veryPosIcon);
        labelTable.put(100,new JLabel(veryPosIcon));
        
        // set all disabled icons
	    Enumeration<Integer> enumKey = labelTable.keys();
	    while(enumKey.hasMoreElements()) {
	    	int key = enumKey.nextElement();
	    	JLabel currLabel = labelTable.get(key);
	    	currLabel.setDisabledIcon(iconTable.get(key));
	    }
	    
		slider.setBorder(new EmptyBorder(30, 0, 0, 0));
        slider.setLabelTable(labelTable);
		return slider;
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
		panel.setBorder( new EmptyBorder(0,80,250,80));
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
        
        deleteListener delListener = new deleteListener();	
		delete.addActionListener(delListener);
        
		// Create the done button
		done 		= new JButton();
		done.setIcon(new ImageIcon(getClass().getClassLoader().
				getResource("\\images\\guiGraphics\\ok.png")));
		done.setBorder(null);
        done.setContentAreaFilled(false);
		
        doneListener dListener = new doneListener();
        done.addActionListener(dListener);
        
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

private JPanel createTopPanel(JPanel panel, String descr) {

		header      = new JLabel("Emoji - Palmistry", SwingConstants.CENTER);
		descrip 	= new JLabel(descr);

		panel.add(header, BorderLayout.CENTER);
		panel.add(descrip, BorderLayout.SOUTH);
		
		header.setFont(new Font("Helvetica",1,45));
		descrip.setBorder(new EmptyBorder(35, 0, 0, 0));
		panel.setBorder(new EmptyBorder(35, 30, 0, 30));
		
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
	emojiHist = new EmojiHistory(historySize);
	emojiHist.fillPlaceholder();
	
	// loop though Emoji list: insert placeholder and add it as icon
	for (int i = 0; i < emojiHist.size(); i++) {
		JLabel tempLab = new JLabel(new ImageIcon(getClass().getClassLoader().
				getResource(emojiHist.get(i).path)));
		history.add(tempLab);
	}

}

private void addToHistory(String unicode) {
	boolean hasSentiCode = false;
	for (int i = 0; i < sentiCodes.length; i++) {
		
		if(unicode.equals(sentiCodes[i])){
			Emoji emoji = new Emoji(unicode,currentCat,sentiScores[i]);
			emojiHist.removeAllPlaceholder();
			emojiHist.add(emoji);
			emojiHist.fillPlaceholder();
			hasSentiCode = true;
		}
	}
	if(!hasSentiCode){
			Emoji emoji = new Emoji(unicode,currentCat,0);
			emojiHist.removeAllPlaceholder();
			emojiHist.add(emoji);
			emojiHist.fillPlaceholder();
		}
	// create labels 
	history.removeAll();
	for (int i = 0; i < emojiHist.size(); i++) {
		JLabel tempLab = new JLabel(new ImageIcon(getClass().getClassLoader().
				getResource(emojiHist.get(i).path)));
		history.add(tempLab);
	}
	history.revalidate();
	history.repaint();
	
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
	private void loadCategoryImages() {
		// aim at correct button field
		JButton[] buttons = null;

		catPath = "./Source/images/"+currentCat+"/";
		emojiPath = "\\images\\"+currentCat+"\\";
		File folder = new File(catPath);
		File[] listOfFiles = folder.listFiles();
		num_emoji = listOfFiles.length - noPicNum(listOfFiles);
	
		buttons = new JButton[num_emoji];
		goBack = new JButton("↩");
		goBack.setBackground(Color.RED);
		goBack.setOpaque(true);
		
		String[] catUnicodes = new String[400];
		String tempfile;
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				tempfile 	= listOfFiles[i].getName();
				if(tempfile.contains(".png")){
					String[] parts 	= tempfile.split(".png");
					String unicode 	= parts[0];
					catUnicodes[i] 		= unicode;
				}
			}
		}
		setUnicodesCat(catUnicodes);
		
		for(int i = 0; i < num_emoji; i++){
			String sourcePath = emojiPath+catUnicodes[i]+".png";
			// retrieve icon and adjust size
			ImageIcon buttonIcon = new ImageIcon(getClass().getClassLoader().
					getResource(sourcePath));
			Image buttonImage = buttonIcon.getImage();
			buttonImage = buttonImage.getScaledInstance(buttonImage.getWidth(null)/3,
					buttonImage.getHeight(null)/3, Image.SCALE_SMOOTH);
			buttonIcon.setImage(buttonImage);
			// create button with icon
			buttons[i] = new JButton(buttonIcon);
		} 
		EmojiListener emojiListener = new EmojiListener();
		for(int i = 0; i < num_emoji; i++){
			buttons[i].addActionListener(emojiListener);
		}
		setButtonsCat(buttons);
	}

	private void setButtonsCat(JButton[] buttons) {
		switch (currentCat) {
		case "Faces":
			buttonsFaces = buttons;break;
		case "Nature":
			buttonsNature = buttons;break;
		case "Objects":
			buttonsObjects = buttons;break;
		case "Signs":
			buttonsSigns = buttons;break;
		default:
			break;
		}
	}

	private JButton[] getButtonsCat(JButton[] buttons) {
		switch (currentCat) {
		case "Faces":
			buttons = buttonsFaces;break;
		case "Nature":
			buttons = buttonsNature;break;
		case "Objects":
			buttons = buttonsObjects;break;
		case "Signs":
			buttons = buttonsSigns;break;
		default:
			break;
		}
		return buttons;
	}

	private void setUnicodesCat(String[] unicodes) {
		switch (currentCat) {
		case "Faces":
			unicodesFaces = unicodes;break;
		case "Nature":
			unicodesNature = unicodes;break;
		case "Objects":
			unicodesObjects = unicodes;break;
		case "Signs":
			unicodesSigns = unicodes;break;
		default:
			break;
		}
	}
	
	private String[] getUnicodesCat(String[] unicodes) {
		switch (currentCat) {
		case "Faces":
			unicodes = unicodesFaces;break;
		case "Nature":
			unicodes = unicodesNature;break;
		case "Objects":
			unicodes = unicodesObjects;break;
		case "Signs":
			unicodes = unicodesSigns;break;
		default:
			break;
		}
		return unicodes;
	}

	private JPanel makeEmojiPanel() {

		JButton[] buttons = null;
		buttons = getButtonsCat(buttons);
		
		catPath = "./Source/images/"+currentCat+"/";
		emojiPath = "\\images\\"+currentCat+"\\";
		File folder = new File(catPath);
		File[] listOfFiles = folder.listFiles();
		num_emoji = listOfFiles.length - noPicNum(listOfFiles);
		
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(currentCat));
		panel.setLayout(new GridLayout(10, 25));
		
		for(int i = 0; i < num_emoji; i++)
		{
			panel.add(buttons[i]);
		}
		panel.add(goBack);

		EmojiListener emojiListener = new EmojiListener();
		goBack.addActionListener(emojiListener);
		return panel;
	}
	
	
	private void callMainFrame() {
		mainFrame.remove(emoPanel);
		mainFrame.revalidate();
		mainFrame.getContentPane().add(botPanel, BorderLayout.PAGE_END);
		mainFrame.revalidate();
		mainFrame.repaint();
		
	}

	private void openAllButtons(boolean status) {
		for (JButton jButton : buttonsFaces) {
			jButton.setEnabled(status);
		}
		for (JButton jButton : buttonsNature) {
			jButton.setEnabled(status);
		}
		for (JButton jButton : buttonsObjects) {
			jButton.setEnabled(status);
		}
		for (JButton jButton : buttonsSigns) {
			jButton.setEnabled(status);
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
	        	sentiCodes[count] = lineData[2];
	        	sentiScores[count] = Double.parseDouble(lineData[8]);
	        	count++;
	        }
		br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

}
