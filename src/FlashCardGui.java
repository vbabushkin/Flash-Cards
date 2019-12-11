import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;


public class FlashCardGui extends JFrame{
	static int sizeOfCurrentPile = 5;
	static List<FlashCard> globalPile =  new ArrayList<FlashCard>();
	static List<FlashCard> currentPile = new ArrayList<FlashCard>();
	static String wordlistFile = new File("data/wordlist.json").getAbsolutePath();
	static String currentPileFile = new File("data/currentPile.json").getAbsolutePath();
	private JFrame modifyCurrentPileFrame = new JFrame("Adding new word to a pile");
	private JFrame editFlashCardFrame = new JFrame("Edit flashcard");
	
	public JList list1;
	public JList list2;
	public JList editFcList;
	public DefaultListModel listModel1 =new DefaultListModel();
	public DefaultListModel listModel2= new DefaultListModel();
	public DefaultListModel editListModel =new DefaultListModel();
	
	static List<Integer> idxList =  new ArrayList<Integer>();
	int idx = 0;
	JPanel contentPane;
	
	public FlashCardGui() throws FileNotFoundException, IOException {
		//************************************************************************************************************//
        //*	read the json and make a global pile of flashcards
        //************************************************************************************************************//
		//load the whole wordlist in globalPile:
		globalPile = Methods.readAllWords(wordlistFile);
		
		// get the current pile
		currentPile = Methods.getCurrentPile(globalPile, wordlistFile,currentPileFile,sizeOfCurrentPile);
		
		//get the ids of words stored in currentPile
		List<Integer> tmpIdxList = Methods.getCurrentPileIds(currentPile);
        idxList.clear();
        for(int id: tmpIdxList)
      	  		idxList.add(id);
        
		//************************************************************************************************************//
        //*	specify the panels
        //************************************************************************************************************//
  		
		contentPane = new JPanel(new BorderLayout());
		JLabel jlbWord = new JLabel("");
		JLabel jlbAnswer = new JLabel("");

		jlbWord.setHorizontalAlignment(JLabel.CENTER);
		Font defaultFont = new JLabel().getFont();
		jlbWord.setFont(new Font(defaultFont.getName(),Font.BOLD, 20));
		jlbWord.setForeground(Color.RED);
        jlbAnswer.setHorizontalAlignment(JLabel.CENTER);
        jlbAnswer.setSize(800,500);

        //************************************************************************************************************//
        //*	add a menu bar
        //************************************************************************************************************//
  		JMenuBar menuBar = new JMenuBar();
  		
  		JMenu optionsMenu = new JMenu("Options");
  		JMenuItem newFlashcardItem = new JMenuItem("Add Flashcard");
  		JMenuItem editFlashcardItem = new JMenuItem("Edit Flashcard");
  		JMenuItem loadCurrentPileItem = new JMenuItem("Load Current Pile");
  		JMenuItem saveCurrentPileItem = new JMenuItem("Save Current Pile");
  		JMenuItem modifyCurrentPileItem = new JMenuItem("Modify Current Pile");
  		optionsMenu.add(newFlashcardItem);
  		optionsMenu.add(editFlashcardItem);
  		optionsMenu.add(loadCurrentPileItem);
  		optionsMenu.add(modifyCurrentPileItem);
  		optionsMenu.add(saveCurrentPileItem);
          
  		JMenu helpMenu = new JMenu("Help");
  		JMenuItem aboutItem = new JMenuItem("About");
  		helpMenu.add(aboutItem);
  		
  		menuBar.add(optionsMenu);
		menuBar.add(helpMenu);
        
		setJMenuBar(menuBar);
		
		
        //	add the options menu
        newFlashcardItem.addActionListener(new NewFlashcardItemListener());
		
        //************************************************************************************************************//
        //*	edit a flashcard
        //************************************************************************************************************//
        
        editFlashcardItem.addActionListener(new ActionListener() {
	          @Override
	          public void actionPerformed(ActionEvent e1) {
	        	  	              //Create and set up the content pane.
	              JComponent newContentPane = new EditFlashCardList();
	              newContentPane.setOpaque(true); //content panes must be opaque
	              editFlashCardFrame.setContentPane(newContentPane);
	              editFlashCardFrame.setSize( 500, 300 );
	              editFlashCardFrame.setResizable(false);
	              editFlashCardFrame.setLocationRelativeTo(null);  
	              editFlashCardFrame.setAlwaysOnTop(true);
	              editFlashCardFrame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
	              editFlashCardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	              
	              //Display the window.
	              editFlashCardFrame.pack();
	              editFlashCardFrame.setVisible(true);
	          }
        });   
        //  add the load current pile menu
        loadCurrentPileItem.addActionListener(new LoadCurrentPileItemListener());
		
        //	add the save flashcard menu
        saveCurrentPileItem.addActionListener(new SaveCurrentPileItemListener(currentPile));
        
        // add about item
		aboutItem.addActionListener(new AboutItemListener());
        
        //************************************************************************************************************//
        //*	add or remove words from a pile
        //************************************************************************************************************//
        
        modifyCurrentPileItem.addActionListener(new ActionListener() {
	          @Override
	          public void actionPerformed(ActionEvent e1) {
	        	  
	      		
	              //Create and set up the content pane.
	              JComponent newContentPane = new ModifyFlashCardList();
	              newContentPane.setOpaque(true); //content panes must be opaque
	              modifyCurrentPileFrame.setContentPane(newContentPane);
	              modifyCurrentPileFrame.setSize( 500, 300 );
	              modifyCurrentPileFrame.setResizable(false);
	              modifyCurrentPileFrame.setLocationRelativeTo(null);  
	              modifyCurrentPileFrame.setAlwaysOnTop(true);
	              modifyCurrentPileFrame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
	              modifyCurrentPileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	              
	              //Display the window.
	              modifyCurrentPileFrame.pack();
	              modifyCurrentPileFrame.setVisible(true);
	          }
        });   
        
        
		//************************************************************************************************************//
        //*	add content pane to the main frame
        //************************************************************************************************************//
		add(contentPane,BorderLayout.CENTER);

        //************************************************************************************************************//
        //*	specify the panel where words are displayed
        //************************************************************************************************************//
        
        JPanel wordPanel=new JPanel(new GridBagLayout());

        jlbWord.setText(currentPile.get(idx).word);
        idx++;
        GridBagConstraints cWordPanel = new GridBagConstraints();
        cWordPanel.fill = GridBagConstraints.VERTICAL;
        cWordPanel.insets = new Insets(2,2,0,2); 
        cWordPanel.anchor = GridBagConstraints.NORTH;
  	  	
        cWordPanel.weightx = 1;	
        cWordPanel.weighty = 1;
        cWordPanel.gridwidth  = 1;

  	  	cWordPanel.gridx = 0;
  	  	cWordPanel.gridy = 0;
  	  	wordPanel.add(jlbWord,cWordPanel);
		contentPane.add(wordPanel,BorderLayout.CENTER);
	  	
  	  	//************************************************************************************************************//
        //*	specify the panel where answers are displayed
        //************************************************************************************************************//
        
        JPanel answerPanel=new JPanel(new GridBagLayout());
  		
        GridBagConstraints cAnswerPanel = new GridBagConstraints();
        cAnswerPanel.fill = GridBagConstraints.VERTICAL;
        cAnswerPanel.insets = new Insets(2,2,0,2); 
        cAnswerPanel.anchor = GridBagConstraints.NORTH;
  	  	
        cAnswerPanel.weightx = 1;	
        cAnswerPanel.weighty = 1;
        cAnswerPanel.gridwidth  = 1;

        cAnswerPanel.gridx = 0;
        cAnswerPanel.gridy = 0;
        answerPanel.add(jlbAnswer,cAnswerPanel);
  	  	
  	  	// 
        //************************************************************************************************************//
        //*	set mouse listener to make flashcards clickable
        //************************************************************************************************************//
        // the subject iterates over flashcards using the left click
        // right click allows the subject to see the answer
  	  	WordPanelListener wpListener = new WordPanelListener(jlbWord, jlbAnswer, wordPanel,cWordPanel, answerPanel, cAnswerPanel);
  	  	
        contentPane.addMouseListener(wpListener);
        
        
        //************************************************************************************************************//
        //*	add everything to the main panel
        //************************************************************************************************************//
        
		contentPane.add(wordPanel,BorderLayout.CENTER);
  	  	
	}//end of constructor
	
	
	/**
	 * class to load currentPile from another file
	 * @author vahan
	 *
	 */
	class AboutItemListener implements ActionListener{
		
		 private JFrame about = new JFrame();
		 JPanel finalFrame=new JPanel(new GridBagLayout());
		
		 public AboutItemListener() {
	  	  	}
		 
         @Override
         public void actionPerformed(ActionEvent e1) {

	         JLabel progName = new JLabel("FlashCards v2.1");
	     
	         JLabel progDate = new JLabel("January 13, 2018");
	       
	         JLabel contact = new JLabel();
	         JLabel website = new JLabel();
	         
	       
	         contact.setText("<html> Contact : <a href=\"\">vahanbabushkin@gmail.com</a></html>");
	         contact.setCursor(new Cursor(Cursor.HAND_CURSOR));
	
	         website.setText("<html> Website : <a href=\"\">http://vaanbabushkin.narod.ru</a></html>");
	         website.setCursor(new Cursor(Cursor.HAND_CURSOR));
	         
	         JLabel copyrightText = new JLabel("Copyright: Vahan Babushkin, 2018");

	         
	         JLabel rightsReservedText = new JLabel("Licensed under GNU General Public License (GPL) version 3");
	         
	             
	         GridBagConstraints cAbout = new GridBagConstraints();
	         cAbout.fill = GridBagConstraints.VERTICAL;
	         cAbout.insets = new Insets(2,2,0,2); 
	         cAbout.anchor = GridBagConstraints.NORTH;
	   	  	
	         cAbout.weightx = 1;	
	         cAbout.weighty = 1;
	         cAbout.gridwidth  = 1;

	         cAbout.gridx = 0;
	         cAbout.gridy = 0;
	         finalFrame.add(progName,cAbout);
	         
	         cAbout.gridx = 0;
	         cAbout.gridy = 1;
	         finalFrame.add(progDate,cAbout);
	         
	         cAbout.gridx = 0;
	         cAbout.gridy = 2;
	         finalFrame.add(contact,cAbout);
	         
	         cAbout.gridx = 0;
	         cAbout.gridy = 3;
	         finalFrame.add(website,cAbout);
	         
	         cAbout.gridx = 0;
	         cAbout.gridy = 4;
	         finalFrame.add(copyrightText,cAbout);
	         
	         cAbout.gridx = 0;
	         cAbout.gridy = 5;
	         finalFrame.add(rightsReservedText,cAbout);
	         
             
             about.add(finalFrame);
             
             about.setTitle( "About" );
             about.setSize(450, 200 );
             about.setResizable(false);
             about.setLocationRelativeTo(null);  
             about.setVisible(true);
             Methods.sendMail(contact);
             Methods.goWebsite(website);
         }
		
	}
	
	
	/**
	 * Class for monitoring mouse clicks on the word panel
	 * @author vahan
	 *
	 */
	class WordPanelListener implements MouseListener {

		JLabel jlbWord = new JLabel();
		JLabel jlbAnswer = new JLabel("");
		JPanel wordPanel = new JPanel();
		JPanel answerPanel = new JPanel();
		GridBagConstraints cAnswerPanel;
		GridBagConstraints cWordPanel;
		
		public WordPanelListener( JLabel jlbWord, JLabel jlbAnswer, JPanel wordPanel,GridBagConstraints cWordPanel , JPanel answerPanel, GridBagConstraints cAnswerPanel) {

			this.jlbWord = jlbWord;
			this.jlbAnswer  = jlbAnswer;
			this.wordPanel = wordPanel;
			this.answerPanel = answerPanel;
			this.cAnswerPanel=cAnswerPanel;
			this.cWordPanel=cWordPanel;
		}
		
		public void mouseClicked(MouseEvent e)  {
			int prevIdx = 0;

			if(e.getButton() == MouseEvent.BUTTON1) {
	        		//Show word on left click and iterate over words
	        		// if idx greater than size of the currentPile -- just set it to 0 and shuffle currentPile
				if(idx >= currentPile.size()){
		    			// display the word
		    			//jlbWord.setText("Reached the end of the pile. Click to start over.");
					prevIdx = idx - 1;
		    			idx = 0;
		    			List <FlashCard> tmpPile = Methods.shuffleCurrentPile(currentPile);
		    			currentPile.clear();
		    			for(FlashCard fc: tmpPile)
		    				currentPile.add(fc);
		    			
		    			idxList.clear();
		    			List<Integer> tmpIdxList = Methods.getCurrentPileIds(currentPile);
		    			for(int id: tmpIdxList)
		    				idxList.add(id);
		    		}//end checking the size
		    		
	    			for (Component component : contentPane.getComponents()) {
	  			       
	    	            if (answerPanel == component) {
	    	            		contentPane.remove(answerPanel);		
	    	            } else {
	    	            		contentPane.remove(wordPanel);
	    	            }
	    			}

	        		// display the word
	        		jlbWord.setText(currentPile.get(idx).word);	
	        		wordPanel.add(jlbWord,cWordPanel);
	        		contentPane.add(wordPanel,BorderLayout.CENTER);
	        		contentPane.repaint();
	        		contentPane.revalidate();
	        		idx++;

			}// end of checking the left click
			
			
			if(e.getButton() == MouseEvent.BUTTON3) {
				int displayIdx;
				//Show word on left click and iterate over words
	        		// if idx greater than size of the currentPile -- just set it to 0 and shuffle currentPile
				
				if(idx >= currentPile.size()){
		    			// display the word
		    			//jlbWord.setText("Reached the end of the pile. Click to start over.");
					prevIdx = idx - 1;
					
		    			idx = 0;

		    		}//end checking the size
	    			for (Component component : contentPane.getComponents()) {
	    				if (wordPanel == component) {
	    					contentPane.remove(wordPanel);
	    					if(idx == 0) {
	    						displayIdx = prevIdx;
	    					}
	    					else {
	    						displayIdx = idx-1;
	    					}
	    					
	    					//System.out.println("idx = "+idx + " displayIdx = "+displayIdx+ " prevIdx = "+prevIdx+" Displaying answer to word "+currentPile.get(displayIdx).word);
	    					String [] explanationStrArray = currentPile.get(displayIdx).explanation;
     	            		String [] translationStrArray = currentPile.get(displayIdx).translation;
     	            		String outUsageText = "<font size =\"5\">"+ currentPile.get(displayIdx).usage +"</font>"+"<BR>";
     	            		
     	            		
             			String outExplanationText = "<BR>";
             			for(String str: explanationStrArray) {
             				outExplanationText = outExplanationText+str+"<BR>";
             			}
             			
     	            		
             			String outTranslationText = "<BR>";
             			for(String str: translationStrArray) {
             				outTranslationText = outTranslationText+str+"<BR>";
             			}
             			
             			outUsageText=outUsageText+"<BR>"+outExplanationText+"<BR>"+outTranslationText;
             			String outUsageTextStr = String.format("<html><div WIDTH=%d>%s</div></html>", 400, outUsageText);
             			jlbAnswer.setText(outUsageTextStr);
                			contentPane.add(answerPanel,BorderLayout.CENTER);
                			
                			if(idx == 0) {
                				List <FlashCard> tmpPile = Methods.shuffleCurrentPile(currentPile);
	        		    			currentPile.clear();
	        		    			for(FlashCard fc: tmpPile)
	        		    				currentPile.add(fc);
	        		    			
	        		    			idxList.clear();
	        		    			List<Integer> tmpIdxList = Methods.getCurrentPileIds(currentPile);
	        		    			for(int id: tmpIdxList)
	        		    				idxList.add(id);
                			}
                			idx++;
	    					
	    				}//end checking if it is a word component
	    				
	    				if (answerPanel == component) {
     	            		contentPane.remove(answerPanel);
     	            		cWordPanel.gridx = 0;
     	            		cWordPanel.gridy = 0;
 	             	  	wordPanel.add(jlbWord,cWordPanel);
     	            		jlbWord.setText(currentPile.get(idx).word);
     	            		//System.out.println("idx = "+idx +" prevIdx = "+prevIdx+" current  word "+currentPile.get(idx).word);
	    					
 	            			contentPane.add(wordPanel,BorderLayout.CENTER);
 	            			idx++;
     	            }//end checking if it is an answer component
	    				
	    				
	    				
	    				contentPane.repaint();
	               	contentPane.revalidate();
	    			}//end of iterating over the all components
		    		
			}// end of checking the right click

	        	
        }//end of mouse clicking event
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	
    }// end of WordPanelListener class
	
	/**
	 * class to load currentPile from another file
	 * @author vahan
	 *
	 */
	class LoadCurrentPileItemListener implements ActionListener{
		
		JFileChooser jfc;
		JPanel warningPanel = new JPanel();
		
		public LoadCurrentPileItemListener() {
			
  	  	}
		@Override
        public void actionPerformed(ActionEvent e1) {
      	  		try {
      	  		jfc = new JFileChooser(FileSystemView.getFileSystemView().getParentDirectory(new File(wordlistFile)));

      	  			int returnValue = jfc.showOpenDialog(null);
      	  			//int returnValue = jfc.showSaveDialog(null);

	      			if (returnValue == JFileChooser.APPROVE_OPTION) {
	      				File selectedFile = jfc.getSelectedFile();
	      				//load the current pile from last session file
	      				List<FlashCard> currentPileTmp = Methods.readAllWords(selectedFile.getAbsolutePath());
	      				
	      				// if currentPile is empty -- report
	      				if(currentPileTmp.size() ==0) {
	      					JOptionPane.showMessageDialog(warningPanel, "The file is empty","Warning", JOptionPane.WARNING_MESSAGE);
	      				}
	      				else {
	      					currentPile = currentPileTmp;
	      				}
	      				
	      			}
	      			
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
	}// end of LoadCurrentPileItemListener class
	
	
	/**
	 * class to save currentPile
	 * @author vahan
	 *
	 */
	class SaveCurrentPileItemListener implements ActionListener{
		List<FlashCard> currentPile;
		String currentPileFile;
		JFileChooser jfc;
		public SaveCurrentPileItemListener(List<FlashCard> currentPile) {
			this.currentPileFile = currentPileFile;
  	  	}
		@Override
        public void actionPerformed(ActionEvent e1) {
      	  		try {
      	  		jfc = new JFileChooser(FileSystemView.getFileSystemView().getParentDirectory(new File(wordlistFile)));

      	  			//int returnValue = jfc.showOpenDialog(null);
      	  			int returnValue = jfc.showSaveDialog(null);

	      			if (returnValue == JFileChooser.APPROVE_OPTION) {
	      				File selectedFile = jfc.getSelectedFile();
	      				System.out.println(selectedFile.getAbsolutePath());
	      				Methods.savePileToFile(currentPile, selectedFile.getAbsolutePath());
	      			}
	      			
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
	}// end of SaveCurrentPileItemListener class
	
	/**
	 * class to add a new flashcard and save it to wordlist
	 * @author vahan
	 *
	 */
	class NewFlashcardItemListener implements ActionListener{
		
  	  public NewFlashcardItemListener() {

  	   }
  	   @Override
  	   public void finalize() {
  	    
  	   }  
  	  	
	  	@Override
	    public void actionPerformed(ActionEvent e1) {
		  		 JPanel mainPanel=new JPanel(new GridBagLayout());
		  	  	 JLabel jlbWord = new JLabel("Enter a new word"); 
		  	  	 JLabel jlbUsage = new JLabel("Enter a sentence");
		  	  	 JLabel jlbExplain = new JLabel("Enter explanation");
		  	  	 JLabel jlbTranslate = new JLabel("Enter translation");
			  	 JFrame addNewWordFrame = new JFrame("Adding new word to a pile");
			  	 JTextField txtWord = new JTextField("");
			  	 JTextField txtUsage = new JTextField(""); 
			  	 JTextField txtExplain = new JTextField("");
			  	 JTextField txtTranslate = new JTextField("");
			  	 JPanel warningPanel = new JPanel();
				 JButton jbtAdd = new JButton("Add");
			
		  		 
			  	  GridBagConstraints c = new GridBagConstraints();
			  	  c.fill = GridBagConstraints.HORIZONTAL;
			  	  c.insets = new Insets(2,2,0,2); 
			  	  c.anchor = GridBagConstraints.NORTH;
			  	  c.weightx = 1;	
			  	  c.weighty = 1;
			  	  c.gridwidth  = 1;
			
			  	  c.gridx = 0;
			  	  c.gridy = 0;
			  	  mainPanel.add(jlbWord,c);
			  	  c.gridx = 1;
			  	  c.gridy = 0;
			  	  mainPanel.add(txtWord,c);
			  	  c.gridx = 0;
			  	  c.gridy = 1;
			  	  mainPanel.add(jlbUsage,c);
			  	  c.gridx = 1;
			  	  c.gridy = 1;
			  	  mainPanel.add(txtUsage,c);
			  	  c.gridx = 0;
			  	  c.gridy = 2;
			  	  mainPanel.add(jlbExplain,c);
			  	  c.gridx = 1;
			  	  c.gridy = 2;
			  	  mainPanel.add(txtExplain,c);
			  	  c.gridx = 0;
			  	  c.gridy = 3;
			  	  mainPanel.add(jlbTranslate,c);
			  	  c.gridx = 1;
			  	  c.gridy = 3;
			  	  mainPanel.add(txtTranslate,c);
			  	  c.gridx = 1;
			  	  c.gridy = 4;
			  	  mainPanel.add(jbtAdd,c);
		    
			  	  addNewWordFrame.add(mainPanel);
			  	  addNewWordFrame.setSize( 450, 200 );
			  	  addNewWordFrame.setResizable(false);
			  	  addNewWordFrame.setLocationRelativeTo(null); 
			  	  addNewWordFrame.setAlwaysOnTop(true);
			  	  addNewWordFrame.toFront();
			  	  addNewWordFrame.setVisible(true);
			  	  
			  	  // add buttoon action listener
				  jbtAdd.addActionListener(new ActionListener() {
					  
		                @Override
		                public void actionPerformed(ActionEvent e) {
		                		
		              	  	//fill the new flashcard
		              	  	//first assign the id next to the highest one
		                		
		              	  	long newId  =  globalPile.get(globalPile.size()-1).id+1;
		              	  	
		              	  	
		              	  	String newWord = txtWord.getText();
		              	  	String newUsage = txtUsage.getText();
		              	  	String tmpNewExpl = txtExplain.getText();
		              	  	String[] newExpl = tmpNewExpl.split(",");
		              	  	String tmpNewTrans = txtTranslate.getText();
		              	  	String[] newTrans = tmpNewTrans.split(",");
		              	  	
		              	  	
		              	  	
		              	  	if(newWord.isEmpty()) {
		              	  			final JDialog dialog = new JDialog();
		              	  			dialog.setAlwaysOnTop(true);
		              	  			JOptionPane.showMessageDialog(dialog, "Please enter the new word.","Warning", JOptionPane.WARNING_MESSAGE);
					 
		              	  		}
		              	  	else {
		              	  		
			              	  	FlashCard newFlashCard = new FlashCard(newId,newWord,newUsage,newExpl,newTrans);
			              	  	System.out.println(Methods.wordSearch(globalPile,newWord));
			              	  	
			              	  	if (Methods.wordSearch(globalPile,newWord)==-1) {
			              	  		
			              	  	     // add to the globalPile
				                	  	 globalPile.add(newFlashCard);

				                     try {
											Methods.savePileToFile(globalPile, wordlistFile);
										} catch (IOException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
				                     addNewWordFrame.dispose();
			              	  	}
			              	  	else {
			              	  		final JDialog dialog = new JDialog();
			              	  		dialog.setAlwaysOnTop(true);
			              	  		JOptionPane.showMessageDialog(dialog, "The word "+newWord+" already exists","Warning", JOptionPane.WARNING_MESSAGE);
								  
			              	  	}
		              	  	}
	              	  	}
		              	  	
		                 
		        	  });// end of Add button action listener 
	      
				 
		  	  
	  	}//end of main action listener
	}// end of NewFlashcardItemListener class
	
	/**
     * Class responsible for editing a flashcard
     * @author vahan
     *
     */
    public class EditFlashCardList extends JPanel implements ListSelectionListener {
    		
    		JPanel mainPanel=new JPanel(new GridBagLayout());
    		
    		public EditFlashCardList() {
    			//add elements from the global pile to the left list
			for(FlashCard gfc: globalPile) {
				if(!editListModel.contains(gfc.word))
					editListModel.addElement(gfc.word);
			}
			
			//Create the list and put it in a scroll pane.
			editFcList = new JList(editListModel);
			editFcList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			editFcList.setSelectedIndex(0);
			editFcList.addListSelectionListener(this);
			editFcList.setVisibleRowCount(5);
			editFcList.setBounds(282, 26, 142, 224);
			JScrollPane listScrollPane1 = new JScrollPane(editFcList);
			Dimension d1 = editFcList.getPreferredSize();
			d1.height = 300;
			d1.width = 200;
			listScrollPane1.setPreferredSize(d1);
			
			
			editFcList.addMouseListener(new EditFlashCardListener());
			
			GridBagConstraints c = new GridBagConstraints();
      	  	c.fill = GridBagConstraints.HORIZONTAL;
      	  	c.insets = new Insets(2,2,0,2); 
      	  	c.anchor = GridBagConstraints.NORTH;
      	  	c.weightx = 1;	
      	  	c.weighty = 1;
      	  	c.gridwidth  = 1;

      	  	c.gridx = 0;
	    	  	c.gridy = 0;
	    	  	mainPanel.add(new JLabel("All words"),c);
	    	  	
	    		c.gridx = 0;
      	  	c.gridy = 1;
      	  	mainPanel.add(listScrollPane1,c);
			
			add(mainPanel, BorderLayout.CENTER);
			
    		}// end of constructor
    		
		@Override
		public void valueChanged(ListSelectionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
    }// end of EditFlashCardList 
	/**
     * Class responsible for manipulating the vocabulary of the current pile of flashcards
     * @author vahan
     *
     */
    public class ModifyFlashCardList extends JPanel implements ListSelectionListener {
    		List<FlashCard> previousPile = new ArrayList<FlashCard>();
		JPanel mainPanel=new JPanel(new GridBagLayout());
		
		private JButton saveNewPileButton = new JButton("Save");
		private JButton cancelNewPileButton = new JButton("Cancel");
		
		public ModifyFlashCardList() {
			super(new BorderLayout());

			for(FlashCard fc : currentPile)
				previousPile.add(fc);
			
			//add elements from the global pile to the left list
			for(FlashCard gfc: globalPile) {
				if(!listModel1.contains(gfc.word))
					listModel1.addElement(gfc.word);
			}
			
			//add elements from the current pile to the right list
			for(FlashCard fc: currentPile) {
				if(!listModel2.contains(fc.word))
					listModel2.addElement(fc.word);
			}
			
			
			
			//Create the list and put it in a scroll pane.
			list1 = new JList(listModel1);
			list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list1.setSelectedIndex(0);
			list1.addListSelectionListener(this);
			list1.setVisibleRowCount(5);
			list1.setBounds(282, 26, 142, 224);
			JScrollPane listScrollPane1 = new JScrollPane(list1);
			Dimension d1 = list1.getPreferredSize();
			d1.height = 300;
			d1.width = 200;
			listScrollPane1.setPreferredSize(d1);
			
	
			//Create the list and put it in a scroll pane.
			list2 = new JList(listModel2);
			list2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list2.setSelectedIndex(0);
			list2.addListSelectionListener(this);
			list2.setVisibleRowCount(5);
			list2.setBounds(282, 26, 142, 224);
			JScrollPane listScrollPane2 = new JScrollPane(list2);
			Dimension d2 = list2.getPreferredSize();
			d2.height = 300;
			d2.width = 200;
			listScrollPane2.setPreferredSize(d2);
			
			
			
			//SaveNewPileButtonListener saveNewPileButtonListener = new SaveNewPileButtonListener(saveNewPileButton);
			//saveNewPileButton.addActionListener(saveNewPileButtonListener);
			saveNewPileButton.setEnabled(false);
			
			
			list1.addMouseListener(new MouseAdapter(){

		          @Override
		          public void mouseClicked(MouseEvent e) {

		        	  	if (e.getClickCount() == 2) {
		        	  		  
			              int index = list1.getSelectedIndex();
			              Object o = list1.getModel().getElementAt(index);
				          String newWord = (String)o;
			              int newWordId = Methods.wordSearch(globalPile,newWord);
			              
			              //get all ids of words in currentPile
			              List<Integer> tmpIdxList = Methods.getCurrentPileIds(currentPile);
			              idxList.clear();
			              for(int id: tmpIdxList)
			            	  		idxList.add(id);
			              
			              if(!listModel2.contains(o)) {
					          currentPile.add(globalPile.get(newWordId));
					          tmpIdxList.clear();
					          tmpIdxList = Methods.getCurrentPileIds(currentPile);
				              idxList.clear();
				              for(int id: tmpIdxList)
				            	  		idxList.add(id);
					          //update the right list
					          DefaultListModel tmpListModel2 = (DefaultListModel) list2.getModel();
					          tmpListModel2.addElement(o);
					          list2.setModel(tmpListModel2);
			              }

			              if (index == -1) { //no selection, so insert at beginning
			            	  	index = 0;
			              } else {           //add after the selected item
			                index++;
			              }
		        	  	 }
		          }
		    });

			list2.addMouseListener(new MouseAdapter(){
		          @Override
		          public void mouseClicked(MouseEvent e) {
		        	  	if (e.getClickCount() == 2) {
		        	  		  
		        	  		  int index = list2.getSelectedIndex();
				         
		        	  		  if (index != -1) {
				        	  	  Object o = list2.getModel().getElementAt(index);
				        	  	  String newWord = (String)o;
				        	  	  int newWordId = Methods.wordSearch(globalPile,newWord);

				        	  	  idxList.remove(idxList.indexOf((int)globalPile.get(newWordId).id));
				        	  	  
					          //remove selected word from the currentPile
					          currentPile.remove(Methods.wordSearch(currentPile,newWord));
					          List<Integer> tmpIdxList = Methods.getCurrentPileIds(currentPile);
				              idxList.clear();
				              for(int id: tmpIdxList)
				            	  		idxList.add(id);
					          //update the right list
					          DefaultListModel tmpListModel2 = (DefaultListModel) list2.getModel();
					          tmpListModel2.removeElement(o);
					          list2.setModel(tmpListModel2);
			              } 
			              
		        	  	 }
		          }
		    });
			
			saveNewPileButton.addActionListener(new ActionListener() {
		          @Override
		          public void actionPerformed(ActionEvent e1) {
		        	  		try {
		        	  				Methods.savePileToFile(currentPile, currentPileFile);
								modifyCurrentPileFrame.dispose();
								listModel2.removeAllElements();
								List <FlashCard> tmpPile = Methods.shuffleCurrentPile(currentPile);
					    			currentPile.clear();
					    			for(FlashCard fc: tmpPile)
					    				currentPile.add(fc);
					    			List<Integer> tmpIdxList = Methods.getCurrentPileIds(currentPile);
						              idxList.clear();
						              for(int id: tmpIdxList)
						            	  		idxList.add(id);
		                        
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		          }
	      	});// end of saveFlashcardItem listener
			
			cancelNewPileButton.addActionListener(new ActionListener() {
		          @Override
		          public void actionPerformed(ActionEvent e1) {
		        	  		
        	  				currentPile.clear();
        	  				for(FlashCard fc:previousPile)
        	  					currentPile.add(fc);
        	  				List<Integer> tmpIdxList = Methods.getCurrentPileIds(currentPile);
  			              idxList.clear();
  			              for(int id: tmpIdxList)
  			            	  		idxList.add(id);
						modifyCurrentPileFrame.dispose();
						listModel2.removeAllElements();

							
		          }
	      	});// end of saveFlashcardItem listener
			

			GridBagConstraints c = new GridBagConstraints();
      	  	c.fill = GridBagConstraints.HORIZONTAL;
      	  	c.insets = new Insets(2,2,0,2); 
      	  	c.anchor = GridBagConstraints.NORTH;
      	  	c.weightx = 1;	
      	  	c.weighty = 1;
      	  	c.gridwidth  = 1;

      	  	c.gridx = 0;
	    	  	c.gridy = 0;
	    	  	mainPanel.add(new JLabel("All words"),c);
	    	  	c.gridx = 2;
	    	  	c.gridy = 0;
	    	  	mainPanel.add(new JLabel("Current pile"),c);
	    	  	
      	  	c.gridx = 0;
      	  	c.gridy = 1;
      	  	mainPanel.add(listScrollPane1,c);
      	  	c.gridx = 2;
      	  	c.gridy = 1;
      	  	mainPanel.add(listScrollPane2,c);
      	  	
			c.gridx = 0;
			c.gridy = 2;
			mainPanel.add(cancelNewPileButton,c);
					
      	  	c.gridx = 2;
    	  	  	c.gridy = 2;
    	  	  	mainPanel.add(saveNewPileButton,c);
			
			add(mainPanel, BorderLayout.CENTER);

		}

		
		@Override
		public void valueChanged(ListSelectionEvent e) {
			// TODO Auto-generated method stub
			if (e.getValueIsAdjusting() == false) {
				//System.out.println(list2.getModel().getSize());
	            if (list2.getModel().getSize() == 0) {
		            //No  word is selected disable the save button
		            	saveNewPileButton.setEnabled(false);

	            } else {
	            		//Not empty, enable the save button.
	            		saveNewPileButton.setEnabled(true);
	            }
	        }
			
		}
		
		
    }// end of ModifyFlashCardList class 	
	
    
    
    /**
	 * class to add a new flashcard and save it to wordlist
	 * @author vahan
	 *
	 */
	class EditFlashCardListener implements MouseListener{
		JPanel mainPanel=new JPanel(new GridBagLayout());
  	  	JLabel jlbWord = new JLabel("Enter a new word"); 
  	  	JLabel jlbUsage = new JLabel("Enter a sentence");
  	  	JLabel jlbExplain = new JLabel("Enter explanation");
  	  	JLabel jlbTranslate = new JLabel("Enter translation");
	  	JFrame addNewWordFrame = new JFrame("Adding new word to a pile");
	  	JTextField txtWord = new JTextField("");
	  	JTextField txtUsage = new JTextField(""); 
	  	JTextField txtExplain = new JTextField("");
	  	JTextField txtTranslate = new JTextField("");
	  	JPanel warningPanel = new JPanel();
		JButton jbtAdd = new JButton("Add");
		FlashCard fc;
		
  	  	public EditFlashCardListener() {

  	  	}
	  	 
  	  	@Override
  	  	public void mouseClicked(MouseEvent e) {

    	  	if (e.getClickCount() == 2) {

	              int index = editFcList.getSelectedIndex();
	              Object o = editFcList.getModel().getElementAt(index);
		          String newWord = (String)o;
	              int newWordId = Methods.wordSearch(globalPile,newWord);
	              
	              FlashCard fc = globalPile.get(newWordId);
	              
	              txtWord.setText(fc.word);
		          txtUsage.setText(fc.usage);
		          txtExplain.setText(String.join(",", fc.explanation));
		          txtTranslate.setText(String.join(",", fc.translation));
			       
		          globalPile.remove(fc);
		          
	              if (index == -1) { //no selection, so insert at beginning
	            	  	index = 0;
	              } else {           //add after the selected item
	                index++;
	              }
    	  	 	
  	
			  	  GridBagConstraints c = new GridBagConstraints();
			  	  c.fill = GridBagConstraints.HORIZONTAL;
			  	  c.insets = new Insets(2,2,0,2); 
			  	  c.anchor = GridBagConstraints.NORTH;
			  	  c.weightx = 1;	
			  	  c.weighty = 1;
			  	  c.gridwidth  = 1;
			
			  	  c.gridx = 0;
			  	  c.gridy = 0;
			  	  mainPanel.add(jlbWord,c);
			  	  c.gridx = 1;
			  	  c.gridy = 0;
			  	  mainPanel.add(txtWord,c);
			  	  c.gridx = 0;
			  	  c.gridy = 1;
			  	  mainPanel.add(jlbUsage,c);
			  	  c.gridx = 1;
			  	  c.gridy = 1;
			  	  mainPanel.add(txtUsage,c);
			  	  c.gridx = 0;
			  	  c.gridy = 2;
			  	  mainPanel.add(jlbExplain,c);
			  	  c.gridx = 1;
			  	  c.gridy = 2;
			  	  mainPanel.add(txtExplain,c);
			  	  c.gridx = 0;
			  	  c.gridy = 3;
			  	  mainPanel.add(jlbTranslate,c);
			  	  c.gridx = 1;
			  	  c.gridy = 3;
			  	  mainPanel.add(txtTranslate,c);
			  	  c.gridx = 1;
			  	  c.gridy = 4;
			  	  mainPanel.add(jbtAdd,c);
		    
			  	  addNewWordFrame.add(mainPanel);
			  	  addNewWordFrame.setSize( 450, 200 );
			  	  addNewWordFrame.setResizable(false);
			  	  addNewWordFrame.setLocationRelativeTo(null); 
			  	  addNewWordFrame.setAlwaysOnTop(true);
			  	  addNewWordFrame.toFront();
			  	  addNewWordFrame.setVisible(true);

			  	  // add buttoon action listener
				  jbtAdd.addActionListener(new ActionListener() {
		
		                @Override
		                public void actionPerformed(ActionEvent e) {
		              	  	
		              	  	//fill the new flashcard
		              	  	//first assign the id next to the highest one
		                		
		              	  	long newId  =  globalPile.get(globalPile.size()-1).id+1;
		              	  	String newWord = txtWord.getText();
		              	  	String newUsage = txtUsage.getText();
		              	  	String tmpNewExpl = txtExplain.getText();
		              	  	String[] newExpl = tmpNewExpl.split(",");
		              	  	String tmpNewTrans = txtTranslate.getText();
		              	  	String[] newTrans = tmpNewTrans.split(",");
		              	  	
		              	  	FlashCard newFlashCard = new FlashCard(newId,newWord,newUsage,newExpl,newTrans);
		              	  	//System.out.println(wordSearch(globalPile,newWord));
		              	  	if (Methods.wordSearch(globalPile,newWord)==-1) {
		              	  	    // add to the globalPile
		              	  		globalPile.add(newFlashCard);
		              	  		
		              	  		int newFlashCardId = Methods.wordSearch(currentPile, newFlashCard.word);
		              	  		currentPile.set(newFlashCardId,newFlashCard);
		              	  		
		              	  		if (newFlashCardId!=-1) {
		              	  			
				                	  	List <FlashCard> tmpPile = Methods.shuffleCurrentPile(currentPile);
						    			currentPile.clear();
						    			for(FlashCard fc: tmpPile)
						    				currentPile.add(fc);
						    			
						    			idxList.clear();
						    			List<Integer> tmpIdxList = Methods.getCurrentPileIds(currentPile);
						    			for(int id: tmpIdxList)
						    				idxList.add(id);
		              	  		}
			                	  	 addNewWordFrame.dispose();
			                     
			                     try {
										Methods.savePileToFile(globalPile, wordlistFile);
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
		              	  	}
		              	  	else {
		              	  		 JOptionPane.showMessageDialog(warningPanel, "The word "+newWord+" already exists","Warning", JOptionPane.WARNING_MESSAGE);
		              	  	}
		                }
		                 
		        	});// end of Add button action listener 
    	  		}//end of counting clicks
		  	  
	  	}//end of main action listener

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}// end of NewFlashcardItemListener class
	
    
}
