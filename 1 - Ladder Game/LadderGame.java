import java.util.Scanner;
import java.util.Random;
import java.io.File;
import java.util.ArrayList;

import static java.lang.System.out;

public class LadderGame {
    static int MaxWordSize = 15;
    ArrayList<String>[] allList;
    Random random;

    /*
     * Function Title: LadderGame
     *
     * @author: Vicki Allan
     *
     * Summary:Creates separate ArrayLists for words of different lengths
     *
     * Inputs: file name as a String
     * Outputs: None
     */
    public LadderGame(String file) {
        random = new Random();
        allList = new ArrayList[MaxWordSize];
        for (int i = 0; i < MaxWordSize; i++) {
            allList[i] = new ArrayList<>();
        }
        try {
            Scanner reader = new Scanner(new File(file));
            while (reader.hasNext()) {
                String word = reader.next();
                if (word.length() < MaxWordSize) {
                    allList[word.length()].add(word);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Function Title: play
     *
     * @author: Jacob Adams and Vicki Allan
     *
     * @param: len              the length of the words being used in the word ladder
     * @param: list             an arraylist that is a clone of the original copy that has all words sorted by size
     * @param: l                the original arraylist that contains all possible words sorted by length
     * @param: partial          the queue that contains all partial solutions to the word ladder
     * @param: start            the first ladderinfo item containing only the start word
     * @param: curr             the current ladderinfo item being worked on
     * @param: i                an iterator to go through all letters of a given word
     * @param: j                an iterator to go through all letters of the alphabet
     * @param: last             holds the position of the last word in the ladderinfo item
     * @param: temp             holds words that are 1 letter off of a given word
     * @param: nextLadder       New ladder which is enqueued when a new word can be added
     * @param: k                iterator to go through all words in final solution
     *
     * Summary: Solves the word ladder between 2 words
     *
     * Inputs: 2 Strings of the same length that are the start and end words of a word ladder
     * Outputs: None
     */
    public void play(String a, String b) {
        int len = a.length();

        if (a.length() != b.length()) {
            out.println("Words are not the same length");
            return;
        }
        if (len >= MaxWordSize) {
            out.println("Words are too long");
            return;
        }
        ArrayList list = new ArrayList();
        ArrayList<String> l = allList[len];
        list = (ArrayList) l.clone();
        out.println("Seeking a solution from: " + a + " -> " + b + "    Size of List: " + list.size());

        // Solve the word ladder problem

        MyQueue<LadderInfo> partial = new MyQueue<>();  //create a new queue for type, add the first word of the ladder
        LadderInfo start = new LadderInfo(a);           //and then remove it from the list we cloned
        list.remove(a); //BONUS
        partial.enqueue(start);

        while(partial.size > 0){

            LadderInfo curr = partial.dequeue();

            for (int i = 0; i < len; i++) {             //this iterates through all letters and all spaces in a word to
                for (char j = 'a'; j <= 'z'; j++){      //generate unique words 1 letter off from the original word

                    int last = curr.moves*len;

                    String temp = curr.ladder.substring(last, last+i) + j + curr.ladder.substring(last+i + 1);      //create a new word that is
                                                                                                                    //1 letter off of the last word
                    if (list.contains(temp)) {

                        list.remove(temp);  //BONUS 

                        LadderInfo nextLadder = new LadderInfo(temp, curr.moves+1, curr.ladder+temp);
                        partial.enqueue(nextLadder);

                        if (temp.equals(b)) {
                            System.out.print(nextLadder.moves + " Steps: [" );                          //If a successful word ladder was
                            for (int k = 0; k <= nextLadder.moves;k++){                                 //found, print out the ladder and
                                System.out.print(nextLadder.ladder.substring(k * len, (k+1) * len)+" ");//any relevant data from the queue
                            }
                            System.out.println("]" + " Total enqueues: " + partial.enqueues + "\n");
                            return;
                        }
                    }
                }
            }
        }
        System.out.println("No ladder found\n");    //If there are no nodes left in the queue, we failed to find
        return;                                     //a word ladder from a to b, so print an error message and return
    }

    /*
     * Function Title: play
     *
     * @author: Vicki Allan
     *
     * @param: list     list of all possible words to use
     * @param: a        the first word of the word ladder
     * @param: b        the goal word to be reached in the word ladder
     *
     * Summary: solves a word ladder of two random words of the same length
     *
     * Inputs: An int representing the length the function will be using
     * Outputs: None
     */
    public void play(int len) {
        if (len >= MaxWordSize) {
            out.println("Words are longer than the max length");
            return;
        }
        ArrayList<String> list = allList[len];
        String a = list.get(random.nextInt(list.size()));
        String b = list.get(random.nextInt(list.size()));
        play(a, b);
    }


    /*
     * Class Title: MyQueue
     *
     * @author: Jacob Adams
     *
     * @param: size         the current size of the queue
     * @param: enqueues     the total number of items queued
     * @param: head         points to the head spot in the queue
     * @param: tail         points to the last spot in the queue
     *
     * Summary: This is a generic queue used to store the various attempts at
     * solving the word ladder problem
     */
    public static class MyQueue<T>{

        public int size;
        public int enqueues;
        public LadderInfo head;
        public LadderInfo tail;


        /*
         * Function Title: dequeue
         *
         * @author: Jacob Adams
         *
         * @param: temp     holds the value of head so that it can be returned
         *
         * @return: temp
         *
         * Summary: Removes the head element from the queue and returns it
         *
         * Inputs: None
         * Outputs: LadderInfo object that was removed from the queue
         */
        public LadderInfo dequeue() {
            if (size == 0) {
                out.println("Queue is already empty.");
                return null;
            }

            LadderInfo temp = head;

            if (size == 1){
                head = null;
                tail = null;
            }

            else{
                head = head.next;
            }
            size--;
            return temp;
        }

        /*
         * Function Title: enqueue
         *
         * @author: Jacob Adams
         *
         * Summary: Adds a new node to the end of the queue and assigns tail to it
         *
         * Inputs: Generic data for the node to be added
         * Outputs: None
         */
        public void enqueue(T x){
            if(tail == null){
                tail = (LadderInfo) x;
                head = tail;
            }
            else{
                tail.next = (LadderInfo) x;
                tail = tail.next;
            }
            size++;
            enqueues++;
        }

        public MyQueue(){ //default constructor for MyQueue
            this.size = 0;
            this.enqueues = 0;
            this.head = null;
            this.tail = null;
        }

    }
}

