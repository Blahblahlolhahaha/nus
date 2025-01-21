import static org.junit.Assert.*;

import org.junit.Test;

/**
 * ShiftRegisterTest
 * @author dcsslg
 * Description: set of tests for a shift register implementation
a*/
public class ShiftRegisterTest {
    /**
     * Returns a shift register to test.
     * @param size
     * @param tap
     * @return a new shift register
     */
    ILFShiftRegister getRegister(int size, int tap) {
        return new ShiftRegister(size, tap);
    }
    
    @Test
    public void msbTest() {
        //since setting tap bit to MSB results in it xoring with the MSB during shifting, it shld return 0 for all cases
        ILFShiftRegister r = getRegister(8,7);
        int [] seed = {1,0,0,1,0,1,1,1};
        r.setSeed(seed);
        int[] expected = {0,0,0,0};
        for(int i = 0; i < 4; i++){
            assertEquals(expected[i], r.shift());
        }
    }

    @Test
    public void randomGenerateTest() {
        ILFShiftRegister r = getRegister(8,3);
        int[] seed  = {1,0,0,1,0,1,1,1};
        r.setSeed(seed);
        int[] expected = {3,7,5};
        for(int i = 0; i< 3;i++){
            assertEquals(expected[i], r.generate(3));
        }
    }

    @Test
    public void randomShiftTest() {
        ILFShiftRegister r = getRegister(8,3);
        int [] seed = {1,0,0,1,0,1,1,1};
        r.setSeed(seed);
        int[] expected = {0,1,1,1};
        for(int i = 0; i < 4; i++){
            assertEquals(expected[i], r.shift());
        }
    }
    
    /**
     * Tests shift with simple example.
     */
    @Test
    public void testShift1() {
        ILFShiftRegister r = getRegister(9, 7);
        int[] seed = { 0, 1, 0, 1, 1, 1, 1, 0, 1 };
        r.setSeed(seed);
        int[] expected = { 1, 1, 0, 0, 0, 1, 1, 1, 1, 0 };
        for (int i = 0; i < 10; i++) {
            assertEquals(expected[i], r.shift());
        }
    }

    /**
     * Tests generate with simple example.
     */
    @Test
    public void testGenerate1() {
        ILFShiftRegister r = getRegister(9, 7);
        int[] seed = { 0, 1, 0, 1, 1, 1, 1, 0, 1 };
        r.setSeed(seed);
        int[] expected = { 6, 1, 7, 2, 2, 1, 6, 6, 2, 3 };
        for (int i = 0; i < 10; i++) {
            assertEquals("GenerateTest", expected[i], r.generate(3));
        }
    }

    /**
     * Tests register of length 1.
     */
    @Test
    public void testOneLength() {
        ILFShiftRegister r = getRegister(1, 0);
        int[] seed = { 1 };
        r.setSeed(seed);
        int[] expected = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
        for (int i = 0; i < 10; i++) {
            assertEquals(expected[i], r.generate(3));
        }
    }

    /**
     * Tests with erroneous seed.
     */
    @Test
    public void testError() {
        //Should throw an exception which needs to be caught by the user when implementing the ShiftRegister
        ILFShiftRegister r = getRegister(4, 1);
        int[] seed = { 1, 0, 0, 0, 1, 1, 0 };
        r.setSeed(seed);
        r.shift();
        r.generate(4);
    }
}
