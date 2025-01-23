/**
 * Contains static routines for solving the problem of balancing m jobs on p processors
 * with the constraint that each processor can only perform consecutive jobs.
 */
public class LoadBalancing {

    /**
     * Checks if it is possible to assign the specified jobs to the specified number of processors such that no
     * processor's load is higher than the specified query load.
     *
     * @param jobSizes the sizes of the jobs to be performed
     * @param queryLoad the maximum load allowed for any processor
     * @param p the number of processors
     * @return true iff it is possible to assign the jobs to p processors so that no processor has more than queryLoad load.
     */
    public static boolean isFeasibleLoad(int[] jobSizes, int queryLoad, int p) {
        // TODO: Implement this
        int currentLoad = 0;
        int cp = 1;
        if(p <= 0 || jobSizes.length == 0) {
            return false;
        }
        for(int size: jobSizes) {
            if(size > queryLoad){
                return false;
            }
            currentLoad += size;
            if(currentLoad > queryLoad) {
                currentLoad = size;
                cp += 1;
                if(cp > p){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns the maximum load given the specified jobs
     *
     * @param jobSizes the sizes of the jobs to be performed
     * @return the heaviest job in the list
     */
    public static int findSum(int[] jobSizes){
        int total = 0;
        for(int size: jobSizes){
           total += size; 
        }
        return total;
    }

    /**
     * Returns the minimum achievable load given the specified jobs and number of processors.
     *
     * @param jobSizes the sizes of the jobs to be performed
     * @param p the number of processors
     * @return the maximum load for a job assignment that minimizes the maximum load
     */
    public static int findLoad(int[] jobSizes, int p) {
        // TODO: Implement this
        if(p <= 0 || jobSizes.length == 0) {
            return -1;
        }

        int begin = 0;
        int end = findSum(jobSizes);
        int mid = 0;
        if(p == 1){
           return end;
        } 
        
        while(begin <= end){
           mid = (begin + end) / 2; 
           if(isFeasibleLoad(jobSizes,mid,p)){
              end = mid - 1;
           }
           else{
             begin = mid + 1;
           }
        } 
        return begin;
    }

    // These are some arbitrary testcases.
    public static int[][] testCases = {
            {1, 3, 5, 7, 9, 11, 10, 8, 6, 4},
            {67, 65, 43, 42, 23, 17, 9, 100},
            {4, 100, 80, 15, 20, 25, 30},
            {2, 3, 4, 5, 6, 7, 8, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83},
            {7}
    };

    /**
     * Some simple tests for the findLoad routine.
     */
    public static void main(String[] args) {
        System.out.println(isFeasibleLoad(testCases[0],10,10));
        System.out.println(isFeasibleLoad(new int[]{1,2,3},6,1));
        for (int p = 1; p < 30; p++) {
            System.out.println("Processors: " + p);
            for (int[] testCase : testCases) {
                System.out.println(findLoad(testCase, p));
            }
        }
    }
}
