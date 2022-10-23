// ******************ERRORS********************************
// Throws UnderflowException as appropriate

class UnderflowException extends RuntimeException {
    /**
     * Construct this exception object.
     *
     * @param message the error message.
     */
    public UnderflowException(String message) {
        super(message);
    }
}

public class Tree<E extends Comparable<? super E>> {
    private BinaryNode<E> root;  // Root of tree
    private String treeName;     // Name of tree

    /**
     * Create an empty tree
     * @param label Name of tree
     */
    public Tree(String label) {
        treeName = label;
        root = null;
    }

    /**
     * Create non ordered tree from list in preorder
     * @param arr   List of elements
     * @param label Name of tree
     */
    public Tree(E[] arr, String label, boolean ordered) {
        treeName = label;
        if (ordered) {
            root = null;
            for (int i = 0; i < arr.length; i++) {
                bstInsert(arr[i]);
            }
        } else root = buildUnordered(arr, 0, arr.length - 1);
    }

    /**
     * Build a NON BST tree by preorder
     * @param arr nodes to be added
     * @return new tree
     */
    private BinaryNode<E> buildUnordered(E[] arr, int low, int high) {
        if (low > high) return null;
        int mid = (low + high) / 2;
        BinaryNode<E> curr = new BinaryNode<>(arr[mid], null, null);
        curr.left = buildUnordered(arr, low, mid - 1);
        curr.right = buildUnordered(arr, mid + 1, high);
        return curr;
    }
    /**
     * Create BST from Array
     * @param arr   List of elements to be added
     * @param label Name of  tree
     */
    public Tree(E[] arr, String label) {
        root = null;
        treeName = label;
        for (int i = 0; i < arr.length; i++) {
            bstInsert(arr[i]);
        }
    }

    /**
     * Change name of tree
     * @param name new name of tree
     */
    public void changeName(String name) {
        this.treeName = name;
    }

    /**
     * Return a string displaying the tree contents as a tree with one node per line
     */
    public String toString() {
        if (root == null)
            return (treeName + " Empty tree\n");
        else
            return treeName+"\n" + toString(root,"");
        // return treeName + "\n" + toString2(root);
    }

    /**
     * Return a String that shows the binary tree displayed on its side
     * @param t the current node
     * @param indent the size of the indent per level
     * @return the right string plus this node plus the left string
     */
    private String toString(BinaryNode<E> t, String indent) {
        String right = "", left = "";       //initialize the strings to be empty

        if(t.right != null)
            right = toString(t.right, indent+"\t"); //if there is a right node, create a string for that side
        if(t.left != null)
            left = toString(t.left, indent+"\t"); //if there is a left node create a string for that side

        return right + indent + t.element + "\n" + left; //combine the three strings and return the value
    }

    /**
     * Return a string displaying the tree contents as a single line
     */
    public String toString2() {
        if (root == null)
            return treeName + " Empty tree";
        else
            return treeName + " " + toString2(root);
    }

    /**
     * Internal method to return a string of items in the tree in order
     * This routine runs in O(??)
     *
     * @param t the node that roots the subtree.
     */
    private String toString2(BinaryNode<E> t) {
        if (t == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(toString2(t.left));
        sb.append(t.element.toString() + " ");
        sb.append(toString2(t.right));
        return sb.toString();
    }

    /**
     * Finds and returns deepest node in a given tree
     *
     */
    public E deepestNode(){
        if(root == null ){
            return null;
        }
        else{
             return deepestNode(root, 0);
        }
    }

    /**
     * Finds the deepest node in a tree and returns its element
     * @param t the current node
     * @param level the level of the current node
     * @return the data held at the deepest node
     */
    private E deepestNode(BinaryNode<E> t, int level){
        E right = null, left = null;
        if (t.left != null) {
            left = deepestNode(t.left, level +1);   //if the left node exists go to it
        }
        if (t.right != null) {
            right = deepestNode(t.right, level+1);  //if the right node exists go to it
        }

        if(level == maxLevel(root, 0)){
            return t.element;                             //if it is the deepest node return the data
        }
        if(t.right == null && t.left == null){
            return null;                                  //if it is a leaf, but not the deepest node return null
        }


        if(right != null){
            return right;                                //if the right node has data in it return the data
        }
        return left;                                     //if not, return the data in the left node
    }

    /**
     * Goes through a tree and returns the deepest level in that tree
     * @param t the current node
     * @param level the level of the current node
     * @return the deepest level found
     */
    private Integer maxLevel(BinaryNode<E> t, Integer level){
        if(t.left == null && t.right == null){
            return level;                               //if we are at a leaf return how deep it is
        }
        Integer right = -1, left = -1;
        if(t.left != null){
            left = maxLevel(t.left, level+1);    //if the left node exists, go to it, adding 1 to level
        }
        if(t.right != null){
            right = maxLevel(t.right, level+1); //if the right node exists, go to it, adding 1 to level
        }
        if(left >= right){
            return left;                             //if the deepest level in the left subtree is deeper than the right
        }                                            //subtree, return the left deepest level
        return right;                                //if not, return the right subtree's deepest level
    }


    /**
     * reverse left and right children recursively
     */
    public void flip() {
        if(root != null){
            flip(root);     //if root is not null, flip the tree
        }
    }

    /**
     * Flip all left and right nodes of all elements in a tree
     * @param t the current node
     */
    private void flip(BinaryNode<E> t){
        if (t.left != null){
            flip(t.left);               //if left node isn't empty call flip
        }
        if(t.right != null){
            flip(t.right);              //if right node isn't empty call flip
        }
        BinaryNode<E> temp = t.right;   //store right as a temporary value

        t.right = t.left;
        t.left = temp;                  //swap positions of left and right nodes
    }

    /**
     * Counts number of nodes in specified level
     * @param level Level in tree, root is zero
     * @return count of number of nodes at specified level
     */
    public int nodesInLevel(int level) {
        if(root == null){
            return 0;
        }
        else{
            return nodesInLevel(root, level);
        }
    }

    /**
     * Returns the number of nodes on a given level, assuming that the root is at level
     * @param t the current node
     * @param level how many times that will have to be recurssed to get to the level
     * @return the number of nodes at that level from each side
     */
    private int nodesInLevel(BinaryNode<E> t, int level){
        if(level < 0){
            return 0;                                     //if there are none return 0
        }
        int left = 0, right = 0;
        if (level == 0){
            return 1;                                     //if on the correct level, return 1
        }
        if(t.left != null){
            left = nodesInLevel(t.left,level-1);    //otherwise, if left node exists go to next level
        }
        if(t.right != null){
            right = nodesInLevel(t.right, level-1); //if right node exists go to next level
        }
        return left+right;                               //return number of nodes on correct level from each side
    }

    /**
     * Print all paths from root to leaves
     */
    public void printAllPaths() {
        if(root != null){
            printAllPaths(root, "");
        }
    }

    /**
     * Prints out all the possible paths to traverse in a given tree
     * @param t the current node
     * @param upto the string of elements preceding the current node
     */
    private void printAllPaths(BinaryNode<E> t, String upto){
        if(t.left == null && t.right == null){
            System.out.println(upto+t.element);                 //if node is a leaf print path and leave method
            return;
        }
        if(t.left != null){
            printAllPaths(t.left,upto+t.element+" -> ");  //otherwise call left node leaf with path up to that point
        }
        if(t.right != null){
            printAllPaths(t.right, upto+t.element+" -> ");//call right node leaf with path up to that point
        }
    }


    /**
     * Counts all non-null binary search trees embedded in tree
     * @return Count of embedded binary search trees
     */
    public Integer countBST() {
        if (root == null) return 0;     //if there is no tree there are no BST's

        return countBST(root);          //Otherwise, count the BST's
    }

    /**
     * Counts the number of Binary Search Trees in an unsorted tree
     * @param t the current node
     * @return the number in the left and right nodes and if the current node is a BST
     */
    private int countBST(BinaryNode<E> t){
        if(t.right == null && t.left == null){
            return 1;                           //if the current node is a leaf return 1
        }

        int left = 0, right = 0, isTree = 0;

        if(t.right != null){
            right = countBST(t.right);          //recursively find the number of BST's in the right tree
        }
        if(t.left != null){
            left = countBST(t.left);            //recursively find the number of BST's in the left tree
        }

        if (isSearchTree(t)){
            isTree = 1;                         //if the current node is a BST add 1 to the total
        }

        return right + left + isTree;           //return the combined number of BST's
    }

    /**
     * Determines whether a node is a binary search tree or not
     * @param t the current node
     * @return whether the top node is a BST
     */
    private boolean isSearchTree(BinaryNode<E> t){
        if(t.right == null && t.left == null){
            return true;                                    //if t is a leaf return true
        }

        boolean left = true, right = true, isTree = false;

        if(t.right != null){
            right = isSearchTree(t.right);                  //if the right node exists determine if it is a search tree
        }
        if ((t.left != null)){
            left = isSearchTree(t.left);                    //if the left node exists determine if it is a search tree
        }

        if(t.right != null && t.left!= null){               //if both nodes exist
            if((Integer)t.right.element > (Integer)t.element && (Integer)t.left.element < (Integer) t.element){
                isTree = true;                              //the left node has to be less than the root and the
            }                                               //right node has to be greater than the root
        }
        if(t.right == null && t.left != null){
            if((Integer)t.left.element < (Integer) t.element){
                isTree = true;                              //if only the left node exists then it has to be less than
            }                                               //the root
        }
        if(t.left == null && t.right != null){
            if((Integer)t.right.element > (Integer) t.element){
                isTree = true;                              //if only the right node exists then it has to be greater
            }                                               //than the root
        }

        return isTree && right && left;                     //if the right, left, and current nodes are BST, return true
    }

    /**
     * Insert into a bst tree; duplicates are allowed
     * @param x the item to insert.
     */
    public void bstInsert(E x) {

        root = bstInsert(x, root);
    }

    /**
     * Internal method to insert into a subtree.
     * In tree is balanced, this routine runs in O(log n)
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BinaryNode<E> bstInsert(E x, BinaryNode<E> t) {
        if (t == null)
            return new BinaryNode<E>(x, null, null);
        int compareResult = x.compareTo(t.element);
        if (compareResult < 0) {
            t.left = bstInsert(x, t.left);
        } else {
            t.right = bstInsert(x, t.right);
        }
        return t;
    }

    /**
     * Determines if item is in tree
     * @param item the item to search for.
     * @return true if found.
     */
    public boolean contains(E item) {
        return contains(item, root);
    }

    /**
     * Internal method to find an item in a subtree.
     * This routine runs in O(log n) as there is only one recursive call that is executed and the work
     * associated with a single call is independent of the size of the tree: a=1, b=2, k=0
     *
     * @param x is item to search for.
     * @param t the node that roots the subtree.
     * @return node containing the matched item.
     */
    private boolean contains(E x, BinaryNode<E> t) {
        if (t == null)
            return false;

        int compareResult = x.compareTo(t.element);

        if (compareResult < 0)
            return contains(x, t.left);
        else if (compareResult > 0)
            return contains(x, t.right);
        else {
            return true;    // Match
        }
    }
    /**
     * Remove all paths from tree that sum to less than given value
     * @param sum: minimum path sum allowed in final tree
     */
    public void pruneK(Integer sum) {
        if(root != null){
            root = pruneK(root, sum);
        }
    }

    /**
     * Removes any paths from a tree that do not sum up to or higher than a given value
     * @param t the current node
     * @param sum the goal sum, minus the sum of the path so far
     * @return the value of the current node to keep it
     */
    private BinaryNode<E> pruneK(BinaryNode<E> t, Integer sum){
        Integer newSum = sum - (Integer) t.element;     //declare the new sum as the old sum minus the current element
        if(t.right != null){
            t.right = pruneK(t.right, newSum);          //if the right node exists recurse with the new sum
        }
        if(t.left != null){
            t.left = pruneK(t.left, newSum);            //if the left node exists recurse with the new sum
        }
        if(t.left == null && t.right == null){
            if(newSum <= 0){
                return t;                               //if the node is a leaf and the target value has been hit,
            }                                           //then return the value of t
            else{
                return null;                            //if the goal has not been hit return null so the node will be removed
            }
        }
        return t;                                       //the value of the current node to keep it
    }


    /**
     * Build tree given inOrder and preOrder traversals.  Each value is unique
     * @param inOrder  List of tree nodes in inorder
     * @param preOrder List of tree nodes in preorder
     */
    public void buildTreeTraversals(E[] inOrder, E[] preOrder) {
        root = (BinaryNode<E>) buildTreeTraversals((Integer[]) inOrder, (Integer[]) preOrder, 0, inOrder.length-1);
    }

    /**
     * Given the inorder and preorder traversals of a tree, builds the tree and returns the root.
     * @param inOrder  the inOrder traversal of a given tree in an integer array
     * @param preOrder the preOrder traversal of a given tree in an integer array
     * @param a starting index to use of preOrder
     * @param b ending index ot use of preOrder
     * @return the new node created
     */
    private BinaryNode<Integer> buildTreeTraversals(Integer[] inOrder, Integer[] preOrder, Integer a, Integer b){
        if(b == a){
            return new BinaryNode<>(preOrder[a]);                   //if there is 1 element return a new node with that
        }                                                           //element
        if(b<a){
            return null;                                            //if it went out of bounds return null
        }

        BinaryNode<Integer> temp = new BinaryNode<>(preOrder[a]);   //create a new node with the highest level element
        Integer pivot = findPivot(inOrder, preOrder[a]);            //get a new pivot point
        Integer[] inLeft = new Integer[pivot];                      //create a new array for the left subtree
        Integer[] inRight = new Integer[inOrder.length-1 - pivot];  //create a new array for the right subtree

        for(int i = 0; i<inOrder.length; i++){
            if(i < pivot){
                inLeft[i] = inOrder[i];             //if it is in the left subtree, fill the left array
            }
            if(i > pivot){
                inRight[i-pivot-1] = inOrder[i];    //if it is in the right subtree, fill the right array
            }
        }

        temp.left = buildTreeTraversals(inLeft, preOrder, a+1, a+inLeft.length);    //call function for left side

        temp.right = buildTreeTraversals(inRight, preOrder, a+1+inLeft.length, b);     //call funciton for right side


        return temp;                                //return the created node
    }

    /**
     * Given an array of elements and a value it returns the index that holds the value
     * @param array the array to be searched
     * @param x the parameter we are looking for
     * @return the index of the parameter 'x'
     */
    private Integer findPivot(Integer[] array, Integer x){
        Integer pivot = -1;
        for(int i = 0; i<array.length ; i++){
            if(array[i] == x){
                pivot = i;          //if the element at index i is equal to x then set the pivot to i
            }
        }
        return pivot;               //return the pivot point
    }


    /**
     * Find the least common ancestor of two nodes
     * @param a first node
     * @param b second node
     * @return String representation of ancestor
     */
    public String lca(E a, E b) {
        if(root == null){
            return "null";                          //if the root is null return error
        }
        if(!(exist(root, a) && exist(root, b))) {
            return "a or b DNE";                    //if a or b are not in the tree return error
        }

        BinaryNode<E> ancestor = null;
        if (a.compareTo(b) < 0) {
            ancestor = lca(root,a,b);               //if a is less than b, call a as lower bound
        } else {
            ancestor = lca(root,b,a);               //if b is less than a, call b as lower bound
        }
        if (ancestor == null){
            return "none";                          //if there is no ancestor return error
        }
        else{
            return ancestor.toString();             //return result as a string
        }
    }

    /**
     * Given two element values it finds their least common ancestor
     * @param t the current node
     * @param a the lower value
     * @param b the higher value
     * @return the first node in between or equal to one of the two values
     */
    private BinaryNode<E> lca(BinaryNode<E> t, E a, E b){
        if((Integer)a < (Integer) t.element && (Integer) b < (Integer) t.element){
            if(t.left != null){
                return lca(t.left, a, b);       //if both bounds are less than the element, and
            }                                   //the left node exists, go to left node
            else{
                return null;                    //if node doesn't exist return error
            }
        }
        if((Integer)a > (Integer) t.element && (Integer) b > (Integer) t.element){
            if(t.right != null){
                return lca(t.right, a, b);      //if both bounds are higher than the element, and
            }                                   //the right bound exists, go to the right node
            else{
                return null;                    //if node doesn't exist return error
            }

        }
        return t;                               //return first node in between or equal to one of the two values
    }

    /**
     * (I created this function not realizing one had been made for me)
     * Determines whether a given value is in a tree
     * @param t the current node
     * @param a the value being looked for
     * @return whether it was found in the left or right tree
     */
    private boolean exist(BinaryNode<E> t, E a){
        boolean left = false, right = false;
        if(t.element == a){
            return true;                        //if node has element a return true
        }
        if(t.left == null && t.right == null){
            return false;                       //if we have reached a leaf without finding it return false
        }
        if(t.left != null){
            left = exist(t.left, a);            //if the left node exists search it for a
        }
        if(t.right != null){
            right = exist(t.right, a);          //if the right node exists search it for a
        }
        return right || left;                   //return whether each side has found it
    }

    /**
     * Balance the tree
     */
    public void balanceTree() {
        if(root != null){
            String inOrder = toString2(root);                //get an inorder string of our elements
            String[] split = inOrder.split(" ");      //create an array of strings that separates each element
            int[] inBetween = new int[split.length];        //create an integer array the size of the string array

            for(int i = 0; i < split.length ; i++){
                inBetween[i] = Integer.parseInt(split[i]);  //fill the integer array with the strings converted to ints
            }

            root = (BinaryNode<E>) balanceTree(inBetween, 0, inBetween.length-1); //set the new root equal to root
        }
    }

    /**
     * Given an unbalanced tree, it reconstructs it to be balanced and returns the new root
     * @param array an inorder array of the elements of the tree
     * @param a the start index
     * @param b the ending index
     * @return the newly created node
     */
    private BinaryNode<Integer> balanceTree(int[] array, int a, int b){

        if(a == b){                                                 //if there is 1 element in the segment
            if(array[a]<0) {
                return null;                                        //if value has already been used return null
            }
            else{
                return new BinaryNode<>(array[a]);                  //return the value in the array as a new node
            }
        }
        int mid = (a+b)/2;                                          //create a pivot to split the array
        if(array[mid]<0){
            return null;                                            //if the value has been used already return null
        }
        BinaryNode<Integer> temp = new BinaryNode<>(array[mid]);    //otherwise, create a new node with the middle value
        array[mid] = -1;                                            //mark the spot as used

        if(b-a >= 1) {
            temp.right = balanceTree(array, mid + 1, b);          //if there are at least two spots left assign the
            temp.left = balanceTree(array, a, mid - 1);           //right and left values by recursing
        }
        return temp;                                                 //return the new node
    }


    /**
     * In a BST, keep only nodes between range
     *
     * @param a lowest value
     * @param b highest value
     */
    public void keepRange(E a, E b) {
        if(root != null){
            root = keepRange(root, a, b);   //if the root isn't null, recurse down the tree and return the new root
        }
    }

    /**
     * Remove nodes from a tree that are not within given bounds
     * @param t the current node
     * @param a the lower bound
     * @param b the upper bound
     * @return the node if it is within the bounds
     */
    private BinaryNode<E> keepRange(BinaryNode<E> t, E a, E b){
        BinaryNode<E> temp = null;
        if((Integer) t.element < (Integer) a){                              //if the node is too small
            temp = t.right;
            while(temp != null && (Integer) temp.element < (Integer) a){    //go on its right branches until it is null
                temp = temp.right;                                          //or until we reach a node greater than a
            }
            return temp;                                                    //return this node as the new position
        }
        if((Integer)t.element > (Integer) b){                               //if the node is too large
            temp = t.left;
            while(temp != null && (Integer)temp.element > (Integer) b){     //go down its left branches until it is null
                temp = temp.left;                                           //or until we reach a node less than b
            }
            return temp;                                                    //return this node as the new position
        }
        if(t.left != null){
            t.left = keepRange(t.left, a, b);                               //if the left node exists check its range
        }
        if(t.right != null){
            t.right = keepRange(t.right, a, b);                             //if the right node exists check its range
        }
        return t;                                                           //if the node is within the bounds return it
    }


    // Basic node stored in unbalanced binary  trees
    private static class BinaryNode<E> {
        E element;            // The data in the node
        BinaryNode<E> left;   // Left child
        BinaryNode<E> right;  // Right child

        // Constructors
        BinaryNode(E theElement) {
            this(theElement, null, null);
        }

        BinaryNode(E theElement, BinaryNode<E> lt, BinaryNode<E> rt) {
            element = theElement;
            left = lt;
            right = rt;
        }

        // toString for BinaryNode
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Node:");
            sb.append(element);
            return sb.toString();
        }

    }

}
