import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JLabel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Methods {
	/**
	 *  efficient Fisher-Yates shuffle array function
	 *  http://stackoverflow.com/questions/1519736/random-shuffling-of-an-array
	 * @param array
	 */
    private static int[] shuffleArray(int[] array)
    {
        int index, temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
        return array;
    }
    
    /**
     * 
     * @param currentPile
     * @return
     */
    public static List<Integer> getCurrentPileIds(List<FlashCard> currentPile){
    	
    		List<Integer> idxList = new ArrayList<Integer>();
    		
	    	for (int k = 0; k < currentPile.size();k++) {
	    		idxList.add((int) currentPile.get(k).id);
	    }
	    	
	    	return idxList;
    }
    
    /**
     * 
     * @param currentPile
     * @return
     */
    public static List<FlashCard> shuffleCurrentPile(List<FlashCard> currentPile){
    		List<FlashCard>shuffledPile = new ArrayList<FlashCard>();
    		
    		//get the ids of words stored in currentPile
        List<Integer>idxList = Methods.getCurrentPileIds(currentPile);
    		int sizeOfIdsList = idxList.size();
        int [] randIdxArray = new int[sizeOfIdsList];

        // first store all words ids into an array of integers
        for(int k = 0;k<sizeOfIdsList;k++)
        		randIdxArray[k] = k;
        
        //then shuffle that array
        randIdxArray = shuffleArray(randIdxArray);
        
       //if the first id equals to the previous id
       //replace the first element of randIdxArray with the last one 
       int  prevLastIdx = idxList.get(sizeOfIdsList-1);
       int newFirstIdx = (int) currentPile.get(randIdxArray[0]).id;
       
       if(newFirstIdx == prevLastIdx) {
    	   		int tmp = randIdxArray[0];
 			randIdxArray[0] = randIdxArray[randIdxArray.length-1];
 			randIdxArray[randIdxArray.length-1] = tmp;
       }
       
       // arrange the shuffled pile according to randIdxArray
       for(int k = 0;k<sizeOfIdsList;k++) {
    			shuffledPile.add(currentPile.get(randIdxArray[k]));
       }
        	
    	   return shuffledPile;
    }
    
    
    /**
     * 
     * @param globalPile
     * @param wordlistFile
     * @param currentPileFile
     * @param sizeOfCurrentPile
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<FlashCard> getCurrentPile(List<FlashCard>  globalPile, String wordlistFile, String currentPileFile, int sizeOfCurrentPile) throws FileNotFoundException, IOException {

		
		//load the current pile from last session file
		List<FlashCard> currentPileTmp = readAllWords(currentPileFile);
		
		
		// if currentPile is empty -- fill it with the last 5 words from the globalPile
		if(currentPileTmp.size() ==0) {
			for(int j = 0;j< sizeOfCurrentPile;j++)
				currentPileTmp.add(globalPile.get(globalPile.size()-sizeOfCurrentPile+j));
			//currentPileTmp = globalPile.subList(globalPile.size()-sizeOfCurrentPile, globalPile.size()-1);
		}

        return currentPileTmp;

	}
    /**
	* 
	* @param file
	* @return List<FlashCard>
	* @throws ParseException 
	* @throws IOException 
	* @throws FileNotFoundException 
	*/
	public static 	List<FlashCard>  readAllWords(String file) throws FileNotFoundException, IOException {
			List<FlashCard> tmpPile = new ArrayList<FlashCard>();
			JSONArray wordList;
			// read from json
			JSONParser parser = new JSONParser();
			try {
				wordList = (JSONArray) parser.parse(new FileReader(file));
				for (Object o : wordList){
		          JSONObject currentWord = (JSONObject) o;
		
		          String word = (String) currentWord.get("word");
		          
		          
		          long id = (Long) currentWord.get("id");
		          
		          String usage = (String) currentWord.get("usage");
		          
		          JSONArray explanation = (JSONArray) currentWord.get("explanation");
		
		          String [] explanationStr = new String [explanation.size()];
		          int j = 0;
		          for (Object c : explanation)
		          {
		            explanationStr[j] = c.toString();
		            j++;
		          }
		          
		          JSONArray translation = (JSONArray) currentWord.get("translation");
		          String [] translationStr = new String [translation.size()];
		          j = 0;
		          for (Object c : translation)
		          {
		            translationStr[j] = c.toString();
		            j++;
		          }
		
		          // add current flash card to a global pile
		          
		          tmpPile.add(new FlashCard(id, word, usage, explanationStr, translationStr));
		 
		        }
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	
		return tmpPile;
	}
    
    /**
     * saves the modified global pile to a file
     * @param globalPile
     * @param file
     * @throws IOException
     */
    	public static void savePileToFile(List<FlashCard> globalPile, String file) throws IOException {
    		JSONArray globalPileJson=new JSONArray();
    		for(FlashCard currentFc : globalPile) {
    			JSONObject obj = new JSONObject();
    			
    			obj.put("usage",currentFc.usage); 
    			
    			JSONArray transList = new JSONArray();
    			for(String transStr : currentFc.translation) {
    				transList.add(transStr);
    			}
    			
    			JSONArray expList = new JSONArray();
    			for(String expStr : currentFc.explanation) {
    				expList.add(expStr);
    			}
    			
    			obj.put("translation",transList); 
    			obj.put("explanation",expList);
    			obj.put("word",currentFc.word);
    			obj.put("id", currentFc.id);
    			
    			globalPileJson.add(obj);
    		}
    		
    		
    		StringWriter out = new StringWriter();
    		globalPileJson.writeJSONString(out);
          
    		String jsonText = out.toString();
    		// add to file
    		PrintWriter writer = new PrintWriter(file);
    		writer.printf("%s\n",jsonText);
    		writer.close();
    }
    
    /**
	 * to search for words in global pile of flashcards
	 * @param currentPile
	 * @param word
	 * @return
	 */
    	public static int wordSearch(List<FlashCard> currentPile, String word) {
		for(int i=0;i< currentPile.size();i++) {
			if (word.equals(currentPile.get(i).word)){
				return i;
			}
		}
		return -1;
    	}

    	/**
    	 * 
    	 * @param website
    	 */
    	public static void goWebsite(JLabel website) {
            website.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        Desktop.getDesktop().browse(new URI("http://vaanbabushkin.narod.ru"));
                    } catch (URISyntaxException | IOException ex) {
                        //It looks like there's a problem
                    }
                }
            });
        }

    	/**
    	 * 
    	 * @param contact
    	 */
    public static void sendMail(JLabel contact) {
            contact.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        Desktop.getDesktop().mail(new URI("mailto:vahanbabushkin@gmail.com?subject=TEST"));
                    } catch (URISyntaxException | IOException ex) {
                        //It looks like there's a problem
                    }
                }
            });
        }


}//end of Methods class
