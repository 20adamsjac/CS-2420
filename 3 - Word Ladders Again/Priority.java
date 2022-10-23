import java.util.Scanner;
import java.util.Random;
import java.io.File;
import java.util.ArrayList;

import static java.lang.System.out;

public class Priority {
    static int MaxWordSize = 15;
    ArrayList<String>[] allList;
    Random random;

    /**
     * Creates the lists required to play the wordladder game
     * @param file the dictionary file that contains the usable words
     */
    public Priority(String file) {
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
        AVLTree<LadderInfo> solve = new AVLTree<LadderInfo>();  //create a new priority queue
        MyQueue<LadderInfo> partial = new MyQueue<>();          //create a new queue for type

        LadderInfo avl = null, queue = null;
        queue = myQueuePlay(a,b,list, partial);                 //run the word ladder with the normal queue
        avl = avlPlay(a,b,list, solve);                         //run the word ladder with the priority queue

        if(avl != null && queue != null) {
            System.out.println("The priority queue did " + (solve.enqueues*100)/ partial.enqueues + "%"+" the work of MyQueue");
            if(avl.moves == queue.moves){
                System.out.print("Same length;");
            }
            else{
                System.out.print("DIFFERENT SIZES;");                           //if there is a solution, compare the
            }                                                                   //performances and display all information
            if(avl.ladder.compareTo(queue.ladder) == 0){
                System.out.println(" Generated the same ladder\n");
            }
            else{
                System.out.println(" Generated different ladders\n");
            }
        }
    }


    /**
     * Plays the word ladder game using the priority queue
     *
     * @param a the starting word
     * @param b the finishing word
     * @param list the list containing the dictionary
     * @return the number of enqueues
     */
    private LadderInfo avlPlay(String a, String b, ArrayList list, AVLTree<LadderInfo> solve){
        String words[] = new String[list.size()];                   //Create a list for both the words and positions of
        int pos[] = new int[list.size()];                           //the words that have been used so far
        words[0] = a;
        pos[0] = 0;


        int len = a.length();
        LadderInfo start = new LadderInfo(a);
        start.score = score(start.word ,b, start.moves);
        solve.insert(start);                                        //add the first word to the priority queue

        while(!solve.isEmpty()){
            LadderInfo curr = solve.deleteMin();                    //pull the minium score value to test

            for (int i = 0; i < len; i++) {
                for (char j = 'a'; j <= 'z'; j++) {
                    int last = curr.moves*len;                      //take the last word from the current word ladder

                    String temp = curr.ladder.substring(last, last+i) + j + curr.ladder.substring(last+i + 1);  //create a word that is
                                                                                                                //1 letter off of the last
                    if (list.contains(temp)) {
                        if( notUsed(words, pos, temp, curr.moves +1) ) {
                            LadderInfo nextLadder = new LadderInfo(temp, curr.moves + 1, curr.ladder + temp);
                            nextLadder.score = score(nextLadder.word, b, nextLadder.moves);
                            solve.insert(nextLadder);                                                           //if the word is the earliest instance
                                                                                                                //of it being used, insert it
                            if (temp.equals(b)) {
                                System.out.print(nextLadder.moves + " Steps: [");                               //If a successful word ladder was
                                for (int k = 0; k <= nextLadder.moves; k++) {                                   //found, print out the ladder and
                                    System.out.print(nextLadder.ladder.substring(k * len, (k + 1) * len) + " ");//any relevant data from the queue
                                }
                                System.out.println("]" + " AVL Tree Total enqueues: " + solve.enqueues + "\n");
                                return nextLadder;                                                              //print and return enqueues
                            }
                        }
                    }
                }
            }
        }
        System.out.println("No ladder found\n");    //If there are no nodes left in the queue, we failed to find a ladder
        return null;
    }

    /**
     * Determines whether the position provided is the earliest use of the word
     *
     * @param x array containing words that have been used
     * @param y corresponding array to x that holds the earliest spot the word was used
     * @param word the current word
     * @param pos the currrent position
     * @return whether the current position is optimal
     */
    boolean notUsed(String[] x, int[] y, String word, int pos){
        for( int i = 0 ; i < x.length ; i++ ){      //For, the size of the list
            if( x[i] == null ){
                x[i] = word;                        //if the space is unused, put the new word and position
                y[i] = pos;                         //into that index and return that it can be used
                return true;
            }
            else if( x[i].compareTo(word) == 0 ){   //if the word has already been used
                if(y[i] <= pos){
                    return false;                   //and if the position it was used at was lower, return false
                }
                else{
                    y[i] = pos;                     //otherwise update the lower position and return true
                    return true;
                }
            }
        }
        return true;
    }

    /**
     * Returns the best case number of moves it would take to solve the given word ladder
     *
     * @param word the last word of the word ladder
     * @param b the goal word (last word in word ladder)
     * @param height the current number of moves it has taken to get to word
     * @return addition of the difference in letters and the moves taken
     */
    private int score(String word, String b, int height){
        int score = height;
        if(word.length() == b.length()){
            for (int i = 0; i<word.length(); i++){
                if(word.charAt(i) != b.charAt(i)){
                    score++;
                }
            }
        }
        return score;
    }

    /**
     * Plays the wordladder game using the normal queue
     *
     * @param a the starting word
     * @param b the finishing word
     * @param given the original copy of the dictionary list
     * @return the number of enqueues
     */
    private LadderInfo myQueuePlay(String a, String b, ArrayList given, MyQueue<LadderInfo> partial){
        ArrayList list = new ArrayList();
        list = (ArrayList) given.clone();
        int len = a.length();
        LadderInfo start = new LadderInfo(a);           //and then remove it from the list we cloned

        partial.enqueue(start);
        list.remove(a);
        while(partial.size > 0){

            LadderInfo curr = partial.dequeue();

            for (int i = 0; i < len; i++) {             //this iterates through all letters and all spaces in a word to
                for (char j = 'a'; j <= 'z'; j++){      //generate unique words 1 letter off from the original word

                    int last = curr.moves*len;

                    String temp = curr.ladder.substring(last, last+i) + j + curr.ladder.substring(last+i + 1);      //create a new word that is
                                                                                                                    //1 letter off of the last word
                    if (list.contains(temp)) {

                        list.remove(temp);  //Bonus from program 1

                        LadderInfo nextLadder = new LadderInfo(temp, curr.moves+1, curr.ladder+temp);
                        partial.enqueue(nextLadder);

                        if (temp.equals(b)) {
                            System.out.print(nextLadder.moves + " Steps: [" );                          //If a successful word ladder was
                            for (int k = 0; k <= nextLadder.moves;k++){                                 //found, print out the ladder and
                                System.out.print(nextLadder.ladder.substring(k * len, (k+1) * len)+" ");//any relevant data from the queue
                            }
                            System.out.println("]" + " My Queue Total enqueues: " + partial.enqueues + "\n");
                            return nextLadder;
                        }
                    }
                }
            }
        }
        System.out.println("No ladder found\n");    //If there are no nodes left in the queue, we failed to find
        return null;                                   //a word ladder from a to b, so print an error message and return 0
    }

    /**
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


    /**
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

