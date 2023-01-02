public class Dynamic {
    int[][] dynamic;
    int[][] memoize;
    String bestSequence;
    int bestValue;
    public int[] costs;
    int[] costs1 = {-1, 1, 3, 5, 9, 10, 15, 17, 18, 19, 22, 25, 27};
    int[] costs2 = {-1, 2, 5, 8, 9, 10, 15, 19, 23, 24, 29, 30, 32};
    int maxSets = 12;
    int maxCups = 24;

    //default constructor
    Dynamic(){
        this.dynamic = new int[13][25];
        this.memoize = new int[13][25];
        this.costs = costs1;
        this.bestSequence = "";
        this.bestValue = 0;
        for(int i = 0 ; i < 13 ; i++){
            for(int j = 0 ; j < 25 ; j++){
                if(j == 0){
                    this.dynamic[i][j] = i;
                    this.memoize[i][j] = i;
                }else if(i == 0){
                    this.dynamic[0][j] = j;
                    this.memoize[0][j] = j;
                }else{
                    this.dynamic[i][j] = -1;
                    this.memoize[i][j] = -1;
                }
            }
        }
    }

    /**
     * Calls the printAll function with the given quantity of tea cups
     * @author Jacob Adams
     * @param quant the number of teacups to divide
     */
    public void callPrint(int quant){
        System.out.println("Teaset Size = " + quant);  //Print the tea set size
        printAll(quant, "", 1);          //Initial call to print all
        System.out.println();                          //Print new line
    }

    /**
     * Recursively finds and prints all possible ways to split up teacups
     * @author Jacob Adams
     * @param amt the current amount of teacups left
     * @param soFar the ways the teacups have been split up to this point
     * @param curSize the current maximum group we can split into
     */
    private void printAll(int amt, String soFar, int curSize){
        if(amt < 0 || curSize < 0 || curSize > maxSets){
            System.out.println("Error");    //if there is an invalid value print an error message
            return;
        }
        if(amt == 0){
            System.out.println(soFar);  //if the amount is zero print the path
            return;
        }

        String temp = soFar;
        if(amt >= curSize){
            if(soFar.compareTo("") == 0){
                temp += curSize;    //if there is not already something in soFar do not add a space
            }else{
                temp += " " + curSize;
            }
            printAll(amt-curSize, temp,curSize);   //if this set is used, properly ajust the values and call again
        }
        if(curSize < maxSets) {
            printAll(amt, soFar, curSize + 1);  //if the current set is not used move to the next largest set
        }

    }

    /**
     * Resets values and tests all three ways of solving the teacups problem
     * @author Jacob Adams
     */
    public void allThree(){
        resetValues();          //set values to original state
        dynamicSolution();      //run dynamic solution
        recursionSolution();    //run recursive solution
        memoizeSolution();      //run memoized solution
    }

    /**
     * Resets values to their original value
     * @author Jacob Adams
     */
    private void resetValues(){
        this.bestSequence = "";     //reset bestSequence
        this.bestValue = 0;         //reset bestValue
        for(int i = 0 ; i < 13 ; i++){
            for(int j = 0 ; j < 25 ; j++){      //reset both arrays
                if(j == 0){
                    this.dynamic[i][j] = i;
                    this.memoize[i][j] = i;
                }else if(i == 0){
                    this.dynamic[0][j] = j;
                    this.memoize[0][j] = j;
                }else{
                    this.dynamic[i][j] = -1;
                    this.memoize[i][j] = -1;
                }
            }
        }
    }

    /**
     * Returns the best way to split up teacups in order to get the best profit
     * @author Jacob Adams
     * @param values the array to be referencing
     * @param cups the starting number of cups
     * @param set the starting set
     * @return a string of how to split up the cups for maximum profit
     */
    private String getPath(int[][] values, int cups, int set){
        String path = "";
        int price = values[set][cups];  //set price to array value
        while(price > 0 && set > 0 && cups > 0){    //while you have not reached an end point
            if(price == values[set-1][cups] && set > 1){
                set--;          //if the price is the same as the row above, move up a row
            }else if(cups%set == 0 && price == (cups/set)*costs[set]){  //if the price is a multiple of the same value
                if(path.compareTo("") == 0) {
                    path += set;
                }else{
                    path += " " +set;
                }
                for(int i = 1 ; i < cups/set ; i++){
                    path += " " + set;
                }
                break;          //add the correct amount of values to the path and break the loop
            }else if(cups > set && price == costs[set]+values[set][cups-set] ){ //if the current set was used
                cups -= set;
                price -= costs[set];        //update values accordingly
                if(path.compareTo("") == 0){
                    path += set;
                }else{
                    path += " " + set;  //add the set to the path
                }
            }
        }
        return path;    //return the final path
    }

    /**
     * Solves the teacup problem using a dynamic programming approach
     * @author Jacob Adams
     */
    public void dynamicSolution(){
        System.out.println("Dynamic:");
        long start = System.nanoTime();     //start measuring time
        for(int i = 1 ; i <= maxCups ; i++){    //input initial values
            dynamic[1][i] = i*costs[1];
            if(i < 13){
                dynamic[i][1] = costs[1];
            }
        }
        for(int i = 2 ; i <= maxSets ; i++){
            for(int j = 2 ; j <= maxCups ; j++){
                int dont = dynamic[i-1][j];     //pull the maxium from the row above (not using set)
                int use = 0;
                int newBest = 0;
                if(j%i == 0){
                    newBest = (j/i)*costs[i];   //if it can be evenly divided, multiply its cost by how many times it can be used
                }
                if(j>i){
                    use = costs[i]+dynamic[i][j-i]; //add the current cost to the value of cups-set (using set)
                }
                int top = Math.max(use, dont);
                dynamic[i][j] = Math.max(top, newBest); //use the top of the 3 values in the array
            }
        }
        for(int i = 1 ; i <= maxCups ; i++){
            System.out.println("Best sum for ("+i+" teacups): $"+dynamic[12][i]+"; "+getPath(dynamic,i, 12)); //print solution
        }
        long end = System.nanoTime();   //stop measuring time
        System.out.println("Dynamic solution run time: " + (end-start)/1000000.0 + " ms\n");    //print time taken
    }

    /**
     * Solves the teacup problem using a recursive approach
     * @author Jacob Adams
     */
    public void recursionSolution(){
        System.out.println("Recursion:");
        long start = System.nanoTime(); //start measuring time
        for(int i = 1 ; i <= maxCups ; i++){
            System.out.println("Best sum for ("+i+" teacups): $"+doRecursion(12,"", i)+ "; "+ bestSequence);    //print solution
        }
        long end = System.nanoTime();   //stop measuring time
        System.out.println("Recursive solution run time: " + (end-start)/1000000.0 + " ms\n");  //print time taken
    }

    /**
     * The recursive function that is referenced to solve the teacups problem
     * @author Jacob Adams
     * @param itemSize current maximum group size
     * @param soFar string containing what has been done to this point
     * @param amt number of teacups left
     * @return the maximum profit
     */
    private int doRecursion(int itemSize,String soFar ,int amt){
        if(itemSize < 1 || amt < 0){
            return -1;      //if an illegal value occurs, return a negative value
        }
        if(amt == 0){
            if(stringSum(soFar)>bestValue){ //poll for best sequence
                bestValue = stringSum(soFar);
                bestSequence = soFar;
            }
            return 0;
        }
        String temp = soFar;
        int dont = doRecursion(itemSize-1,soFar,amt);   //if not using current set, move to the next lowest
        if(amt < itemSize){
            return dont;    //if you cannot use the set return the dont value
        }else{
            if (soFar.compareTo("") == 0) {
                temp += itemSize;
            } else {
                temp += " " + itemSize;
            }
        }
        int use = costs[itemSize]+doRecursion(itemSize,temp,amt-itemSize);  //if using the set, update soFar and amount
        return Math.max(use,dont);  //return the highest of the 2 values
    }

    /**
     * Takes a soFar string and calculates its value
     * @author Jacob Adams
     * @param x the string
     * @return the value
     */
    private int stringSum(String x){
        if(x.compareTo("") != 0) {
            String[] array = x.split(" ");      //convert the string to an array of strings
            int sum = 0;
            for (String s : array) {    //for all sets in the string
                sum += costs[Integer.parseInt(s)];  //add the cost of the given set to the sum
            }
            return sum; //return the sum
        }
        return 0;   //if the string is null, return zero
    }

    /**
     * Solves the teacups problem using memoization
     * @author Jacob Adams
     */
    public void memoizeSolution(){
        System.out.println("Memoization:");
        long start = System.nanoTime(); //start measuring time
        for(int j = 1 ; j <=maxCups ; j++){
            this.memoize[12][j] = doMemoization(12,j);  //find the required values
        }
        for(int i = 1 ; i <= maxCups ; i++){
            System.out.println("Best sum for ("+i+" teacups): $"+dynamic[12][i]+"; "+getPath(memoize,i,12));    //print the solution
        }
        long end = System.nanoTime();   //stop measuring time
        System.out.println("Memoizing solution run time: " + (end-start)/1000000.0 + " ms\n");  //print time taken
    }

    /**
     * The program that does the actual memoization
     * @author Jacob Adams
     * @param itemSize current maximum group size
     * @param amt number of teacups left
     * @return the maximum profit
     */
    private int doMemoization(int itemSize, int amt){
        if(itemSize < 1 || amt < 0){
            return -1;      //if an illegal value occurs, return a negative number
        }
        if(amt == 0){
            return 0;       //if amount is zero, return zero
        }
        if(amt < itemSize ){    //if you cannot use the set
            if(memoize[itemSize-1][amt] < 0) {
                memoize[itemSize-1][amt] = doMemoization(itemSize - 1, amt);    //if the value has not been found, find it
            }
            return memoize[itemSize-1][amt];    //return the value for not using the current set
        }
        int use = costs[itemSize];
        if(memoize[itemSize][amt-itemSize] < 0){
            memoize[itemSize][amt-itemSize] = doMemoization(itemSize,amt-itemSize); //if the value has not been found, find it
        }
        if(amt-itemSize > 0) {
            use += memoize[itemSize][amt - itemSize];   //if the amount is greater than the set value, add the value from the table
        }

        if(memoize[itemSize-1][amt] < 0) {
            memoize[itemSize-1][amt] = doMemoization(itemSize - 1, amt);    //if the value has not been found, find it
        }
        int dont = memoize[itemSize-1][amt];    //assign the value to dont

        return Math.max(use,dont);  //return the maximum of the 2 values
    }

    public static void main(String[] args) {
        Dynamic test = new Dynamic();
        System.out.println("PART 1: PRINT ALL");
        test.callPrint(10);

        System.out.println("PART 2: DYNAMIC PROGRAMING (INCLUDING BONUS)");
        System.out.println("Set 1");
        test.allThree();

        test.costs = test.costs2;
        System.out.println("Set 2");
        test.allThree();
    }
}
