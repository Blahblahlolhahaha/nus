import java.util.Arrays;

class WiFi {

    /**
     * Implement your solution here
     */
    public static double computeDistance(int[] houses, int numOfAccessPoints) {
        Arrays.sort(houses);
        double start = 0;
        double end = houses[houses.length - 1] - houses[0];
        double mid = (end + start) / 2;
        while(end > start){
            if(coverable(houses,numOfAccessPoints,mid)){
                end = mid;
            } else{
                start = mid + 0.5;
            }
            mid = (end + start) / 2;
        }
        return mid;
    }

    /**
     * Implement your solution here
     */
    public static boolean coverable(int[] houses, int numOfAccessPoints, double distance) {
        if(numOfAccessPoints == 0){
            return false;
        }
        if(numOfAccessPoints >= houses.length){
            return true;
        }
        int covered = 0;
        int hseInd = 0;
        for(int i = 0; i < numOfAccessPoints; i++){
           double loc =  distance + houses[hseInd];
           covered++;
           for(int x = hseInd + 1; x < houses.length; x++){
               if(Math.abs(houses[x] - loc) > distance){
                   hseInd = x;
                   break;
               }
               covered++;
           }
           if(covered == houses.length){
               return true;
           }
        }
        return false;
    }
}
