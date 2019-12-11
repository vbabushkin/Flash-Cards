
public class FlashCard {
	public String word;
	public long id;
	public String usage;
	public String [] explanation;
	public String [] translation;

	FlashCard (long id, String word, String usage, String [] explanation, String [] translation){
		this.word = word;
		this.id = id;
		this.usage = usage;
		this.explanation = explanation;
		this.translation = translation;
	}
	
	public void printFlashCard() {
		System.out.println("***********************************");
		System.out.println("id: "+this.id);
		System.out.println(this.word);
		System.out.println(this.usage);
		for(String c : this.explanation)
			System.out.print(String.format("\t %s\n",c));
		for(String c : this.translation)
			System.out.print(String.format("\t %s\n",c));
	}
}
