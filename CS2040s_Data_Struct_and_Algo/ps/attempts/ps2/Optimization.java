/**
 * The Optimization class contains a static routine to find the maximum in an array that changes direction at most once.
 */
public class Optimization {

    /**
     * A set of test cases.
     */
    static int[][] testCases = {
            {1, 3, 5, 7, 9, 11, 10, 8, 6, 4},
            {67, 65, 43, 42, 23, 17, 9, 100},
            {4, -100, -80, 15, 20, 25, 30},
            {2, 3, 4, 5, 6, 7, 8, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83},
            {5},
            {-1,-1},
            {-100,-50,-10,-50,-100},
            {1,2,3,5},
            {5,5,5,5,3,5,5,5,5},
            null,
            {},
            {-1},
            {-1,-2},
            {-2,-1},
            {1,2,1,0,-1}
    };

    /**
     * Returns the maximum item in the specified array of integers which changes direction at most once.
     *
     * @param dataArray an array of integers which changes direction at most once.
     * @return the maximum item in data Array
     */
    public static int searchMax(int[] dataArray) {
        // search for transition point, then compare ends if both
        
        if(dataArray == null || dataArray.length == 0) {
            // return 0 for empty/null array
            return 0;
        }
        int length = dataArray.length;
        if(length == 1) {
            //returns first element immediately if length == 1
            return dataArray[0];
        }
        boolean increasing = dataArray[0] <= dataArray[1];
        if(length == 2) {
            //might as well
            return increasing ? dataArray[1] : dataArray[0];
        }
        int begin = 0;
        int end = length - 1;
        while(begin <= end) {
            int mid = ((end + begin) / 2); //find mid point
            if( mid == length - 1 || mid == 0){
                return increasing ? dataArray[mid] : dataArray[0];
            }
            int test = dataArray[mid];
            int back = dataArray[mid - 1];
            int front = dataArray[mid + 1];
            boolean found = (back > test && 
                front > test) ||  
                (back < test && 
                front < test);
           if(found) {
              if(!increasing) {
                 return dataArray[0] > dataArray[length - 1] ? dataArray[0] : dataArray[length - 1];
              } else {
                 return dataArray[mid];
              } 
           }
           if(test <= front) {
               if(increasing) {
                   begin = mid + 1;
               } else {
                   end = mid;
               }
           }else {
               if(increasing) {
                   end = mid;
               }
               else {
                   begin = mid + 1;
               }
           }


        }

        return 0;
    }

    /**
     * A routine to test the searchMax routine.
     */
    public static void main(String[] args) {
        for (int[] testCase : testCases) {
            System.out.println(searchMax(testCase));
        }
    }
}
