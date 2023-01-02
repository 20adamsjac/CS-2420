import java.util.Arrays;

public class UnionFind {
    private int[] data;

    //Constructor for UnionFind using size
    UnionFind(int size){
        this.data = new int[size];
        for(int i = 0 ; i < size ; i++){
            data[i] = -1;
        }
    }

    /**
     * Resets the array to its initial value
     * @author Jacob Adams
     */
    public void clear(){
        //reset to every node in its own group
        Arrays.fill(data, -1);
    }

    /**
     * Finds the root of the tree a particular node is in recursively and points those along the path to the root
     * @author Jacob Adams
     * @param item the initial node
     * @return the index of the root node
     */
    public int find(int item){
        if(data[item] < 0){     //if the item contains a negative number it
            return item;        //means it is the root, so return that index
        }
        return data[item] = find(data[item]);   //otherwise, recursively find the root and set
    }                                           //all nodes on the way to point to the root

    /**
     * Unions the two groups of the two items passed in
     * @author Jacob Adams
     * @param item1 the first item
     * @param item2 the second item
     */
    public void union(int item1, int item2){
            item1 = find(item1);    //find the root node of item1
            item2 = find(item2);    //find the root node of item2

        if(item1 != item2) {    //if the roots are the same they are already in the same tree
            if (data[item1] < data[item2]) {    //if the first tree is larger than the second
                data[item1] += data[item2];     //add the size of the trees into the first root
                data[item2] = item1;            //make the second root a child of the first
            } else {
                data[item2] += data[item1];     //otherwise, add the size of the trees into the second root
                data[item1] = item2;            //make the first root a child of the second root
            }
        }
    }

    /**
     * Prints the contents of the array showing each group and indents subgroups
     * @author Jacob Adams
     */
    public void printStruct(){
        System.out.println("Groups: ");
        for(int i = 0 ; i < data.length ; i++){
            if(data[i] < 0){                    //if the data is negative (root)
                System.out.print(i + " <- ");   //print the node
                for(int j = 0 ; j < data.length ; j++){
                    if(data[j] == i){
                        System.out.print(j + " ");  //print all items that point to the root
                    }
                }
                System.out.println();
            }
        }
        System.out.println("Subgroups: ");
        for(int x = 0 ; x < data.length ; x++){
            if(data[x] >= 0 && data[data[x]] >= 0){     //check if the data has already occured
                int z = 0;
                for(z = 0 ; z <= x ; z++){
                    if(data[x] == data[z]){
                        break;
                    }
                }
                if(z == x) {        //if the data has not already occured
                    System.out.print(data[x] + " <- "); //print subgroup root
                    for (int k = 0; k < data.length; k++) {
                        if (data[k] == data[x]) {
                            System.out.print(k + " ");  //print all data that points to subgroup root
                        }
                    }
                    System.out.println();
                }
            }
        }
        System.out.println();
    }
}
