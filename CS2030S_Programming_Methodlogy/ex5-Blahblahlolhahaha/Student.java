import java.util.HashMap;

class Student{
    
    private String name;
    private HashMap<String, Integer> scores;

    public Student(String name, HashMap<String, Integer> scores) {
        this.name = name;
        this.scores = scores;
    }

    public int getScore(String module) {
        return scores.get(module);
    }

    public boolean isTakingModule(String module) {
        return scores.containsKey(module);
    }

    public static String toGrade(int score) {
        if(score < 50) {
            return "F";
        }
        if(score < 55) {
            return "D";
        }
        if(score < 60) {
            return "C";
        }
        if(score < 65) {
            return "B-";
        }
        if(score < 70) {
            return "B";
        }
        if(score < 75) {
            return "B+";
        }
        if(score < 80) {
            return "A-";
        }
        if(score < 85) {
            return "A";
        }
        return "A+";

    }

    @Override
    public String toString() {
        return "Name: " + this.name;
    }
    

}
