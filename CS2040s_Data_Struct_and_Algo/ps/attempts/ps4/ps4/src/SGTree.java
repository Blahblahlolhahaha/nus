/**
 * Scapegoat Tree class
 *
 * This class contains an implementation of a Scapegoat tree.
 */

public class SGTree {
    /**
     * TreeNode class.
     *
     * This class holds the data for a node in a binary tree.
     *
     * Note: we have made things public here to facilitate problem set grading/testing.
     * In general, making everything public like this is a bad idea!
     *
     */

    private static final double maxRatio = (2.0/3.0);
    private TreeNode highest = null;
    public static class TreeNode {
        int key;
        double weight;
        public TreeNode left = null;
        public TreeNode right = null;
        public TreeNode parent = null;
        TreeNode(int k) {
            key = k;
            weight = 1.0;
        }
    }

    // Root of the binary tree
    public TreeNode root = null;

    /**
     * Counts the number of nodes in the subtree rooted at node
     *
     * @param node the root of the subtree
     * @return number of nodes
     */
    public int countNodes(TreeNode node) {
        // TODO: Implement this
        if(node == null){
            return 0;
        }
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    /**
     * Builds an array of nodes in the subtree rooted at node
     *
     * @param node the root of the subtree
     * @return array of nodes
     */
    public TreeNode[] enumerateNodes(TreeNode node) {
        // TODO: Implement this
        if(node == null){
            return new TreeNode[0];
        }
        TreeNode[] nodes = new TreeNode[countNodes(node)];
        TreeNode[] left = enumerateNodes(node.left);
        int leftLength = left.length; 
        for(int i = 0;i < leftLength; i++){
            nodes[i] = left[i];
        }
        nodes[leftLength] = node;
        TreeNode[] right = enumerateNodes(node.right);
        for(int i = 0;i < right.length; i++){
            nodes[leftLength + 1 + i] = right[i];
        }
        return nodes;
    }

    public TreeNode buildTree(TreeNode[] nodeList,int start,int end){
        if(start > end){
            return null;
        }
        if(start == end){
            nodeList[start].left = null;
            nodeList[start].right = null;
            nodeList[start].weight = 1.0;
            return nodeList[start];
        }
        int mid = (end - start) / 2 + start;
        TreeNode middle = nodeList[mid];
        middle.left = buildTree(nodeList, start, mid - 1);
        middle.right = buildTree(nodeList, mid+1,end);
        middle.weight = 1.0;
        if(middle.right != null){
            middle.right.parent = middle;
            middle.weight += middle.right.weight;
        }
        if(middle.left != null){
            middle.left.parent = middle;
            middle.weight += middle.left.weight;
        }
        return middle; 
    }

    /**
     * Builds a tree from the list of nodes
     * Returns the node that is the new root of the subtree
     *
     * @param nodeList ordered array of nodes
     * @return the new root node
     */
    public TreeNode buildTree(TreeNode[] nodeList) {
        // TODO: Implement this
        return buildTree(nodeList,0,nodeList.length - 1);
    }

    /**
     * Determines if a node is balanced. If the node is balanced, this should return true. Otherwise, it should return
     * false. A node is unbalanced if either of its children has weight greater than 2/3 of its weight.
     *
     * @param node a node to check balance on
     * @return true if the node is balanced, false otherwise
     */
    public boolean checkBalance(TreeNode node) {
        // TODO: Implement this
        if(node.left == null || node.right == null){
            return node.weight <= 3.0;
        }
        boolean leftHeavy = node.left.weight > maxRatio * node.weight;
        boolean rightHeavy = node.right.weight > maxRatio * node.weight;
        if(leftHeavy || rightHeavy){
            return false;
        }
        return true;
    }

    /**
    * Rebuilds the subtree rooted at node
    * 
    * @param node the root of the subtree to rebuild
    */
    public void rebuild(TreeNode node) {
        // Error checking: cannot rebuild null tree
        if (node == null) {
            return;
        }

        TreeNode p = node.parent;
        TreeNode[] nodeList = enumerateNodes(node);
        TreeNode newRoot = buildTree(nodeList);

        if (p == null) {
            root = newRoot;
        } else if (node == p.left) {
            p.left = newRoot;
        } else {
            p.right = newRoot;
        }
        newRoot.parent = p;

    }

    /**
    * Inserts a key into the tree
    *
    * @param key the key to insert
    */
    public void insert(int key) {
        if (root == null) {
            root = new TreeNode(key);
            return;
        }

        insert(key, root);
    }

    private void checkAndSet(TreeNode node){
        if(!checkBalance(node)){
            highest = node;        
        }
    }

    // Helper method to insert a key into the tree
    private void insert(int key, TreeNode node) {
        
        if (key <= node.key) {
            if (node.left == null) {
                node.left = new TreeNode(key);
                node.left.parent = node;
                highest = null;
            } else {
                insert(key, node.left);
            }
        } else {
            if (node.right == null) {
                node.right = new TreeNode(key);
                node.right.parent = node;
                highest = null;
            } else {
                insert(key, node.right);
            }
        }
        node.weight += 1.0;
        checkAndSet(node);
        if(node == root && highest != null){
            rebuild(highest);
        }
    }

    // Simple main function for debugging purposes
    public static void main(String[] args) {
        SGTree tree = new SGTree();
        for (int i = 0; i < 100; i++) {
            tree.insert(i);
        }
        tree.rebuild(tree.root);
    }
}
