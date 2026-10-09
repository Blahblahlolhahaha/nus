import java.util.Random;
import java.util.HashMap;
import java.util.Arrays;
/**
 * This is the main class for your Markov Model.
 *
 * Assume that the text will contain ASCII characters in the range [1,255].
 * ASCII character 0 (the NULL character) will be treated as a non-character.
 *
 * Any such NULL characters in the original text should be ignored.
 */
public class MarkovModel {

	// Use this to generate random numbers as needed
	private Random generator = new Random();
    private final int order;
    private HashMap<String,Pair> chain;
	// This is a special symbol to indicate no character
	public static final char NOCHARACTER = (char) 0;

	/**
	 * Constructor for MarkovModel class.
	 *
	 * @param order the number of characters to identify for the Markov Model sequence
	 * @param seed the seed used by the random number generator
	 */
	public MarkovModel(int order, long seed) {
		// Initialize your class here
        this.order = order;
        this.chain = new HashMap<>();
		// Initialize the random number generator
		generator.setSeed(seed);
	}

	/**
	 * Builds the Markov Model based on the specified text string.
	 */
	public void initializeText(String text) {
		// Build the Markov model here
        for(int i = 0;i < text.length(); i++){
            if(i+order < text.length()){
                int index = i + order;
                char c = 0;
                if(index < text.length()){
                    c = text.charAt(index);
                }
                String kgram = text.substring(i,i+order);
                if(chain.containsKey(kgram)){
                    Pair info = chain.get(kgram);
                    info.incrementCount();
                    if(c != 0){
                        info.incrementChar(c);
                    }
                }
                else{
                    int[] chars = new int[256];
                    int count = 1;
                    if(c != 0){
                        chars[c-1] = 1;
                    }
                    Pair info = new Pair(count,chars);
                    chain.put(kgram, info);
                } 
            }

        }
    }

	/**
	 * Returns the number of times the specified kgram appeared in the text.
	 */
	public int getFrequency(String kgram) {
        if(chain.containsKey(kgram)){
            return chain.get(kgram).getCount();
        }
		return 0;
	}

	/**
	 * Returns the number of times the character c appears immediately after the specified kgram.
	 */
	public int getFrequency(String kgram, char c) {
        if(chain.containsKey(kgram)) {
            return chain.get(kgram).getChar(c);
        }
        return 0;
	}

	/**
	 * Generates the next character from the Markov Model.
	 * Return NOCHARACTER if the kgram is not in the table, or if there is no
	 * valid character following the kgram.
	 */
	public char nextCharacter(String kgram) {
		// See the problem set description for details
		// on how to make the random selection.
		if(chain.containsKey(kgram)){
            Pair info = chain.get(kgram);
            int random = generator.nextInt(info.getCount());
            return info.returnChar(random);
        }
        return NOCHARACTER;
	}

    private class Pair{
        private int count;
        private int[] chars;

        public Pair(int count, int[] chars){
            this.count = count;
            this.chars = chars;
        }

        public void incrementCount(){
            count++;
        }

        public void incrementChar(char c){
            chars[c-1] = chars[c-1] + 1;
        }

        public int getCount(){
            return count;
        }

        public int getChar(char c){
            return chars[c-1];
        }

        public char returnChar(int ind){
            for(int i = 0; i< chars.length; i++){
                int num = chars[i];
                if(num > ind){
                    return (char)(i + 1);
                }
                ind -= num;
            }
            return NOCHARACTER;
        }

        @Override
        public String toString(){
            return "Count: " + count + " Chars: " + Arrays.toString(chars);
        }
    }
}
