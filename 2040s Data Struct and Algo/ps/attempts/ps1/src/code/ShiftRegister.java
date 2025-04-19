///////////////////////////////////
// This is the main shift register class.
// Notice that it implements the ILFShiftRegister interface.
// You will need to fill in the functionality.
///////////////////////////////////

/**
 * class ShiftRegister
 * @author 
 * Description: implements the ILFShiftRegister interface.
 */
public class ShiftRegister implements ILFShiftRegister {
    ///////////////////////////////////
    // Create your class variables here
    ///////////////////////////////////
    // TODO:
    int size; //size of the register
    int tap; //the index of the tap bit
    int[] register; //Holds the current state of the register

    ///////////////////////////////////
    // Create your constructor here:
    ///////////////////////////////////
    ShiftRegister(int size, int tap) {
        this.size = size;
        this.tap = tap;
    }

    ///////////////////////////////////
    // Create your class methods here:
    ///////////////////////////////////
    /**
     * sets the register to the seed provided
     * @param seed is the initial register for the ShiftRegister
     */
    @Override
    public void setSeed(int[] seed) {
        this.register = seed;
    }

    public void setSeed(String text) {
        int[] sad = new int[8 * text.length()];
        char[] chars = text.toCharArray();
        int count = 0;
        for(char x: chars){
            int i = x;
            int currentCount = 0;
            while(i > 0){
                sad[count] = i % 2;
                count ++;
                currentCount ++;
                i /= 2;
            }
            if(currentCount % 8 != 0){
                while(currentCount % 8 !=0){
                    sad[count] = 0;
                    count++;
                    currentCount++;
                }
            }
        }
        this.register = sad;
    }

    /**
     * Performs a single shift step 
     * @return LSB of resulting register
     */
    @Override
    public int shift() {
        // TODO:
        int feedback = register[tap] ^ register[size - 1]; //xors the tap bit with the MSB
        int[] newRegister = new int[size];
        for(int i = 0; i < size - 1; i++){
            newRegister[i + 1] = register[i]; //left shift operation
        }
        newRegister[0] = feedback; //sets LSB to the feedback bit
        this.register = newRegister; //replace the current state of the register
        return feedback;
    }

    /**
     * Extracts shift operation a set number of times, saving the bit returned from each shift operation and returning the resulting binary number as an integer 
     * @param k number of bits to save
     * @return An integer representation of the resulting bits returned from each shift operation 
     * Description:
     */
    @Override
    public int generate(int k) {
        int x = 0;
        for(int i = 0; i < k; i++){
            x *= 2;
            x += shift();
        }
        return x;
    }
}

