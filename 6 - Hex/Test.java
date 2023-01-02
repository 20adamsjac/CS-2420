import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Test {
    public static void main(String[] args){
        testUnionFind();
        testHex();
    }

    /**
     * Tests the functionality of the UnionFind.java file
     */
    static void testUnionFind(){
        UnionFind test = new UnionFind(10);
        test.union(1,4);
        test.union(1,6);    //create group {1,4,6}
        System.out.println("Group {1,4,6}");
        test.printStruct();
        test.union(0,2);
        test.union(3,2);
        test.union(3,5);    //create group {0,2,3,5}
        System.out.println("Group {0,2,3,5}");
        test.printStruct();
        test.union(5,6);    //merge groups
        System.out.println("Merge Groups");
        test.printStruct();
        test.union(7,8);
        test.union(8,9);    //create group {7,8,9}
        System.out.println("Group {7,8,9}");
        test.printStruct();
        test.union(0,9);    //merge groups
        System.out.println("Merge Groups");
        test.printStruct();
        test.find(7); //find on 7 to show path compression
        System.out.println("Find on 7");
        test.printStruct();
    }

    /**
     * Tests the functionality of the Hex.java file
     */
    static void testHex(){
        try{
            File moves = new File("moves.txt"), moves2 = new File("moves2.txt");
            Scanner test1 = new Scanner(moves), test2 = new Scanner(moves2);
            Hex game = new Hex();
            while(test1.hasNextInt() && game.gameNotOver()){    //while there are moves and the game is not over
                if(test1.hasNextInt()) {
                    game.play("Blue", test1.nextInt()); //while there are still moves play a blue move
                }
                if(test1.hasNextInt() && game.gameNotOver()){
                    game.play("Red", test1.nextInt());  //while there are still moves and
                }                                             //the game is not over play a red move
            }
            if(game.gameNotOver()){
                System.out.println("Game did not finish");  //if the game did not finish print the board
                game.printBoard();
            }
            System.out.println();
            game.reset();       //reset the game board
            while(test2.hasNextInt() && game.gameNotOver()){  //repeat game for test2 file
                if(test2.hasNextInt()) {
                    game.play("Blue", test2.nextInt());
                }
                if(test2.hasNextInt() && game.gameNotOver()){
                    game.play("Red", test2.nextInt());
                }
            }
            if(game.gameNotOver()){
                System.out.println("Game did not finish");
                game.printBoard();
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
}

