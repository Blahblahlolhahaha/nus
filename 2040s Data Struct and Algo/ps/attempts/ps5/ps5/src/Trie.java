import java.util.ArrayList;

public class Trie {

    // Wildcards
    final char WILDCARD = '.';
    TrieNode root;
    private class TrieNode {
        // TODO: Create your TrieNode class here.
        int[] presentChars = new int[62];
        TrieNode[] children = new TrieNode[62];
        boolean end = false;
    }

    public Trie() {
        // TODO: Initialise a trie class here.
        root = new TrieNode();
    }

    int getInd(char c){
        if(c >= 'a'){
           return 10 + 26 + c - 'a';
        } else if(c >= 'A'){
           return 10 + c - 'A'; 
        }
        return c - '0';
    }

    char getChar(int i){
        if(i<10){
            return (char)('0' + i);
        }
        i -= 10;
        if(i<26){
            return (char)('A' + i);
        }
        i -= 26;
        return (char)('a' + i);
    }

    void insert(String s, TrieNode node, int i){
        if(i == s.length()){
            node.end = true;
            return;
        }
        int child = getInd(s.charAt(i));

        if(node.presentChars[child] == 0){
            node.presentChars[child] = 1;
            node.children[child] = new TrieNode();
        }
        insert(s,node.children[child], i+1);
    }

    /**
     * Inserts string s into the Trie.
     *
     * @param s string to insert into the Trie
     */
    void insert(String s) {
        insert(s,root,0);
    }

    boolean contains(String s, TrieNode node, int i){
        if(i == s.length()){
            return node.end == true;
        }
        int child = getInd(s.charAt(i));
        if(node.presentChars[child] == 0){
            return false;
        }
        return contains(s,node.children[child],i+1);
    }

    /**
     * Checks whether string s exists inside the Trie or not.
     *
     * @param s string to check for
     * @return whether string s is inside the Trie
     */
    boolean contains(String s) {
        // TODO
        return contains(s,root,0);
    }

    void prefixSearch(String s, ArrayList<String> results, int limit, TrieNode node, int i,String res){
        if(results.size() == limit || node == null){
            return;
        }
        if(i >= s.length()){
            if(node.end){
                results.add(res);
            }
            for(int x = 0; x < 62;x++){
               String sad = res + getChar(x);
               prefixSearch(s,results,limit,node.children[x],i+1,sad);
            } 
        }
        else{
            char c = s.charAt(i);
            if(c == '.'){
               for(int x = 0; x < 62;x++){
                   String sad = res + getChar(x);
                   prefixSearch(s,results,limit,node.children[x],i+1,sad);
               } 
            } else{
                int y = getInd(c);
                if(i == s.length() - 1 && node.end && !s.contains(".")){
                    results.add(s + c);
                }
                prefixSearch(s,results,limit,node.children[y],i+1,res+c);
            }
        }
    }

    /**
     * Searches for strings with prefix matching the specified pattern sorted by lexicographical order. This inserts the
     * results into the specified ArrayList. Only returns at most the first limit results.
     *
     * @param s       pattern to match prefixes with
     * @param results array to add the results into
     * @param limit   max number of strings to add into results
     */
    void prefixSearch(String s, ArrayList<String> results, int limit) {
        // TODO
        prefixSearch(s,results,limit,root,0,"");
    }


    // Simplifies function call by initializing an empty array to store the results.
    // PLEASE DO NOT CHANGE the implementation for this function as it will be used
    // to run the test cases.
    String[] prefixSearch(String s, int limit) {
        ArrayList<String> results = new ArrayList<String>();
        prefixSearch(s, results, limit);
        System.out.println(results);
        return results.toArray(new String[0]);
    }


    public static void main(String[] args) {
        Trie t = new Trie();
        t.insert("peter");
        t.insert("piper");
        t.insert("picked");
        t.insert("a");
        t.insert("peck");
        t.insert("of");
        t.insert("pickled");
        t.insert("peppers");
        t.insert("pepppito");
        t.insert("pepi");
        t.insert("pik");

        String[] result1 = t.prefixSearch("pe", 10);
        String[] result2 = t.prefixSearch("", 1);
        // result1 should be:
        // ["peck", "pepi", "peppers", "pepppito", "peter"]
        // result2 should contain the same elements with result1 but may be ordered arbitrarily
    }
}
