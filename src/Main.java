import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;

import org.json.simple.parser.ParseException;

public class Main {
	/**
     * @param args the command line arguments
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
    		 
    		FlashCardGui frame = new FlashCardGui();
 
         frame.pack();
         frame.setSize(1000, 600);
         frame.setTitle("Flashcards");
         frame.setLocationRelativeTo(null); // Center the frame
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setVisible(true);
    }
	
}
