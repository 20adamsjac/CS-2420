import java.util.ArrayList;

public class Hex {
    private final int rows;
    private final int cols;
    private int moves;
    private String[] color;
    private UnionFind board;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_BLUE = "\u001B[34m";

    //Default constructor for standard sized board
    Hex(){
        this.rows = 11;
        this.cols = 11;
        this.moves = 0;
        this.color = new String[126];
        this.board = new UnionFind(126);
        for(int i = 1 ; i < 122 ; i++){
            color[i] = "None";
        }
        color[122] = "Red";
        color[123] = "Red";
        color[124] = "Blue";
        color[125] = "Blue";
    }

    //BONUS
    //Constructor for any sized board
    Hex(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        this.moves = 0;
        this.color = new String[rows*cols + 5];
        this.board = new UnionFind(rows*cols + 5);
        for(int i = 1 ; i < rows*cols + 1 ; i++){
            color[i] = "None";
        }
        color[rows*cols +1] = "Red";
        color[rows*cols +2] = "Red";
        color[rows*cols +3] = "Blue";
        color[rows*cols +4] = "Blue";
    }

    /**
     * Clears the game to be played again
     * @author Jacob Adams
     */
    public void reset(){
        board.clear();  //clear the board array
        moves = 0;      //reset the number of moves
        for(int i = 1 ; i < rows*cols + 1 ; i++){
            color[i] = "None";  //set the color array to all "None"
        }
    }

    /**
     * Plays a move for a given team
     * @author Jacob Adams
     * @param team the color of the team as a string
     * @param index the position they would like to take
     */
    public void play(String team, int index){
        if(color[index].compareTo("None") != 0){
            System.out.println(team + " tried to use position " + index + " again.");
            return;     //if the index is already being used print an error and return
        }
        color[index] = team;        //otherwise, set the color to the new value
        ArrayList<Integer> neighbors = getNeighbors(index); //find the neighbors of the index
        if(neighbors != null) {
            for (Integer x : neighbors) {
                if (team.compareTo(color[x]) == 0) {
                    board.union(index, x);  //if any of the neighbors are the same color, union them
                }
            }
        }
        isWinner();     //check if this move made a color win the game
    }

    /**
     * Returns a boolean whether the game has been won yet
     * @author Jacob Adams
     * @return if the game is not yet over
     */
    public boolean gameNotOver() {
        if (board.find(rows * cols + 1) == board.find(rows * cols + 2)) {
            return false;   //if red won return false
        }
        if(board.find(rows*cols +3) == board.find(rows*cols +4)) {
            return false;   //if blue won return false
        }
        return true;        //otherwise, return true
    }

    /**
     * Prints who the winner is and the final board if the game is over
     * @author Jacob Adams
     */
    private void isWinner(){
        if(board.find(rows*cols+1) == board.find(rows*cols +2)) {     //red win
            moves++;
            System.out.println("Red has won after " + moves + " moves! Here is the final board.");
            printBoard();
            return;
        }
        if(board.find(rows*cols +3) == board.find(rows*cols +4)){   //blue win
            moves++;
            System.out.println("Blue has won after " + moves + " moves! Here is the final board.");
            printBoard();
            return;
        }
        moves++;
    }

    /**
     * Generates and returns an array of indices of the neighbors for the given index
     * @author Jacob Adams
     * @param index the center index
     * @return an ArrayList containing all the neighboring indices
     */
    //TOP = +1, BOTTOM = +2, LEFT = +3, RIGHT = +4
    private ArrayList<Integer> getNeighbors(int index){
        ArrayList<Integer> neighbors= new ArrayList<Integer>(); //create a new ArrayList of Integers

        if(index <= 0 || index > (rows*cols+4)){     //check for invalid values
            System.out.println("Invalid value passed into getNeighbors");
            return null;    //print an error and return null
        }

        if(index > cols){       //neighbors above
            neighbors.add(index-cols);      //if the index isn't on the top add the NW neighbor
            if(index % cols != 0){
                neighbors.add(index-cols+1);    //if it is not touching the
            }                                   //right wall add the NE neighbor
        }
        else{
            neighbors.add(rows * cols+1);   //add the top to the neighbor list if it is touching the top
        }

        if((index-1) % cols != 0){      //left neighbor
            neighbors.add(index-1);     //if the index is not touching the left wall, add the left neighbor
        }
        else{
            neighbors.add(rows * cols + 3); //add left wall as neighbor if it is touching
        }

        if(index % cols != 0){      //right neighbor
            neighbors.add(index+1); //if the index is not touching the right wall, add the right neighbor
        }
        else{
            neighbors.add(rows * cols + 4); //add right wall as neighbor if it is touching
        }

        if(index <= (rows * (cols-1))){         //neighbors below
            neighbors.add(index+cols);          //if the index is not on the bottom row add the SE neighbor
            if((index-1) % cols != 0){
                neighbors.add(index+cols-1);    //if it is not touching the left wall add the SW neighbor
            }
        }
        else{
            neighbors.add(rows * cols +2);      //add the bottom if the index is on the bottom row
        }

        neighbors.removeIf(x -> x > rows * cols + 4 || x < 1);

        return neighbors;   //return the ArrayList with all the neighbors
    }

    /**
     * Prints the contents of the game board in color code
     * @author Jacob Adams
     */
    public void printBoard(){
        for(int i = 0 ; i < rows ; i++){        //go through all rows and columns
            indent(i);  //indent the number of rows we have gone down
            for(int j = 1 ; j <= cols ; j++){
                if(color[(i*cols)+j].compareTo("Blue") == 0) {
                    System.out.print(ANSI_BLUE + "B " + ANSI_RESET);    //if it is a blue slot, print B in blue
                }
                else if(color[(i*cols)+j].compareTo("Red") == 0){
                    System.out.print(ANSI_RED + "R " + ANSI_RESET);     //if it is a red slot, print R in red
                }
                else{
                    System.out.print("0 ");         //if it is neither print a 0
                }
            }
            System.out.println();       //print a new line per row
        }
    }

    /**
     * Prints an indentation a certain number of times
     * @author Jacob Adams
     * @param times the quantity of times to print the indentation
     */
    private void indent(int times){
        for(int i = 0 ; i < times ; i++){
            System.out.print(" ");  //print a space the number of times given
        }
    }
}
