import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class GuessingTest {


    @Test
    public void guess() {
        Random r = new Random();
        for(int  i = 0;i<1001;i++){
        
        
        int hiddenNumber = i;
        System.out.println(hiddenNumber);
        Guessing g = new Guessing();

        int remainingGuesses = 10;
        while (remainingGuesses >= 0) {
            int guess = g.guess();
            if (guess == hiddenNumber) {
                break;
            } else if(guess > hiddenNumber) {
                g.update(1);
                remainingGuesses--;
            } else {
                g.update(-1);
                remainingGuesses--;
            }
        }
        assertTrue(remainingGuesses >= 0);
    
        }
    }
}
