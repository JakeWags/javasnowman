import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


/**
 * @author Jake Wagoner
 * @date January 31 2018
 * It's hangman but we can't call it that for reasons unknown to me...
 */
public class Snowman {
	public static final int NUMBER_OF_TRIES = 10;
	private String finalWord;
	private char[] currentWord;
	private char[] usedLetters = new char[500];
	private int currentGuess = 0;
	
	List<String> wordList = new ArrayList<String>();
	
	private Scanner s = new Scanner(System.in);

	/**
	 * @param word
	 * Constructor to create a new game of Snowman
	 */
	public Snowman(String filename) {
		this.readWords(filename);
		
		this.finalWord = this.getWord();
		this.currentWord = new char[finalWord.length()];
		for (int i = 0; i < currentWord.length; i++) {
			this.currentWord[i] = '_';
		}
		
		this.guess();
	}

	/**
	 * @param fileName  Path of the file
	 * Read in the list of words
	 */
	public void readWords(String fileName) {
		Scanner s;
		try {
			s = new Scanner(new File(fileName));

			while (s.hasNext())
				wordList.add(s.next());

			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * @return  Returns a random word from the word list provided
	 */
	public String getWord() {
		// returns a random word from wordList

		Random r = new Random();

		return wordList.get(r.nextInt(wordList.size()));
	}
	
	/**
	 * Main Game loop
	 */
	private void guess() {
		if (this.currentGuess < Snowman.NUMBER_OF_TRIES) {
			char c = this.askForLetter();
			
			int index = this.location(c);
			
			if (this.used(c) || (c > 122 || c < 65)) {
				System.out.println("That is a duplicate or not a letter...");
				this.guess();
			} else {
				if (index > -1) {
					this.correct(c);
				} else {
					this.currentGuess++;
					this.incorrect(c);
				}
			}
		} else {
			this.finish(false);
		}
	}
	
	/**
	 * @return  Returns the guessed letter
	 */
	private char askForLetter() {
		char c = '$';
		System.out.println("Guess a letter: ");
		String s1 = s.nextLine();
		if (s1.equals("")) {
			this.askForLetter();
		} else {
			c = s1.charAt(0);
		}
		
		return c;
	}
	
	/**
	 * @param c  Character to be checked, case insensitive
	 * @return  Returns the index of the character inside the string, or -1 if it is not found
	 */
	private int location(char c) {
		return finalWord.toUpperCase().indexOf(Character.toUpperCase(c));
	}
	
	/**
	 * @param c  Correctly guessed letter
	 */
	private void correct(char c) {
		for (int i = 0; i < this.finalWord.length(); i++) {
			if (Character.toUpperCase(this.finalWord.charAt(i)) == Character.toUpperCase(c)) {
				this.currentWord[i] = c;
			}
		}
		
		this.usedLetters[this.usedLetters.length - this.currentGuess - 1] = c;
		
		this.printCurrent();
		String s = new String(this.currentWord);
		if (s.equalsIgnoreCase(this.finalWord)) {
			this.finish(true);
		} else {
			System.out.println("You have " + (Snowman.NUMBER_OF_TRIES - this.currentGuess) + " guesses left.");
			this.guess();
		}
	}
	
	/**
	 * @param c  Incorrectly guessed letter
	 */
	private void incorrect(char c) {
		System.out.println("That letter was not found.");
		System.out.println("You have " + (Snowman.NUMBER_OF_TRIES - this.currentGuess) + " guesses left.");
		this.printCurrent();
		System.out.println("");
	
		
		this.usedLetters[this.currentGuess] = c;
		this.guess();
	}
	
	/**
	 * @param won  If the player won or lost
	 */
	private void finish(boolean won) {
		if (won) {
			System.out.println("You won!");
		} else {
			System.out.println("You failed to guess it. The word was: " + this.finalWord);
		}
		
		s.close();
	}
	
	/**
	 * @param c  Character to be checked
	 * @return  Returns true if the character has been guess previously
	 */
	private boolean used(char c) {
		boolean retVal = false;
		
		for (char letter : this.usedLetters) {
			if (Character.toUpperCase(letter) == Character.toUpperCase(c)) {
				retVal = true;
				break;
			} else {
				retVal = false;
			}
		}
		
		return retVal;
	}
	
	/**
	 * Prints the current progress on the word
	 */
	private void printCurrent() {
		for (char c : this.currentWord) {
			System.out.print(c);
		}
		System.out.println("");
	}
	
	
	
	public static void main(String[] args) {
		// word2.txt has 58000 words!
		Snowman s = new Snowman("words2.txt");
	}
}
