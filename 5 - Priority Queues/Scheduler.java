import java.util.ArrayList;

public class Scheduler {
    int time = 1;
    leftistHeap<Task> deadline = new leftistHeap<>();
    leftistHeap<Task> startime = new leftistHeap<>();
    leftistHeap<Task> crazy = new leftistHeap<>();

    public Scheduler(){}

    /**
     * Determines which kind of priority to use and routes to the correct schedule
     * @author Jacob Adams
     * @param x The identifying string
     * @param y An ArrayList containing all the tasks
     */
    void makeSchedule(String x, ArrayList<Task> y){
        time = 1;                   //reset time
        if(x.charAt(0) == 'D'){
            deadline.timeLate = 0;  //reset late variables
            deadline.tasksLate = 0;
            System.out.println(x);  //print the type of solution
            makeDeadline(y);        //go to the correct scheduler
        }
        else if(x.charAt(0) == 'S'){
            startime.timeLate = 0;  //reset late variables
            startime.tasksLate = 0;
            System.out.println(x);  //print the type of solution
            makeStart(y);           //go to the correct scheduler
        }
        else if(x.charAt(0) == 'W'){
            crazy.timeLate = 0;     //reset late variables
            crazy.tasksLate = 0;
            System.out.println(x);  //print the type of solution
            makeCrazy(y);           //go to the correct scheduler
        }
        else{                       //handle error
            System.out.println("Error in makeSchedule");
        }
    }

    /**
     * Pulls from the priority queue and simulates 'solving' the task for 1 time unit
     * @author Jacob Adams
     * @param x The current ArrayList
     * @param time The current time
     */
    private void solve(leftistHeap<Task> x, int time){
        leftistHeap<Task>.Node<Task> temp = x.deleteMin();              //pull the highest priority node off queue
        temp.data.duration--;                                           //remove 1 from duration
        System.out.print("Time: " + time + " "+ temp.data.toString());  //record working on the node
        if(temp.data.duration <= 0){
            System.out.print(" **");            //if the node is completed print **
            if(time > temp.data.deadline){      //if it was late print how much and update the late variables
                System.out.println(" Late " + (time-temp.data.deadline));
                x.tasksLate++;
                x.timeLate += time-temp.data.deadline;
            }
            else{
                System.out.println();   //new line
            }
            return;
        }
        x.insert(temp);             //if the node is not done put it back in the heap
        System.out.println();       //new line
    }

    /**
     * Adds deadline priority tasks to the queue as they reach their start time and sends the queue to be solved
     * @author Jacob Adams
     * @param y The ArrayList with tasks
     */
    private void makeDeadline(ArrayList<Task> y){
        while (deadline.isEmpty() || !y.isEmpty()) {        //while there are still tasks to complete
            for(int i = 0; i < y.size() ; i++){
                Task temp = y.get(0);                       //check the tasks to see which can be added to the queue
                if(temp.start <= time){                     //if the start time has been hit, add the task
                    deadline.insert(temp, temp.deadline);   //insert using deadline as priority
                    y.remove(0);                      //if the data was inserted remove it from the list
                }
            }
            if(deadline.isEmpty()) {
                solve(deadline, time);      //if there are data entries in the queue, solve them
            }
            time++;         //add to the time
        }
        //print the results of how late the schedule was
        System.out.println("Tasks Late: " + deadline.tasksLate + ", Total Minutes Late: " + deadline.timeLate + "\n");
    }

    /**
     * Adds start time priority tasks to the queue as they reach their start time and sends the queue to be solved
     * @author Jacob Adams
     * @param y The ArrayList with tasks
     */
    private void makeStart(ArrayList<Task> y){
        while (startime.isEmpty() || !y.isEmpty()) {        //while there are still tasks to complete
            for(int i = 0; i < y.size() ; i++){
                Task temp = y.get(0);                       //check the tasks to see which can be added to the queue
                if(temp.start <= time){                     //if the start time has been hit, add the task
                    startime.insert(temp, temp.start);      //insert using start time as the priority
                    y.remove(0);                      //if the data was inserted remove it from the list
                }
            }
            if(startime.isEmpty()) {
                solve(startime, time);  //if there are data entries in the queue, solve them
            }
            time++;     //add to the time
        }
        //print the results of how late the schedule was
        System.out.println("Tasks Late: " + startime.tasksLate + ", Total Minutes Late: " + startime.timeLate + "\n");
    }

    /**
     * Adds my priority tasks to the queue as they reach their start time and sends the queue to be solved
     * @author Jacob Adams
     * @param y The ArrayList with tasks
     */
    private void makeCrazy(ArrayList<Task> y){
        while (crazy.isEmpty() || !y.isEmpty()) {           //while there are still tasks to complete
            for(int i = 0; i < y.size() ; i++){
                Task temp = y.get(0);                       //check the tasks to see which can be added to the queue
                if(temp.start <= time){                     //if the start time has been hit, add the task
                    crazy.insert(temp, temp.duration);      //insert using duration as the priority
                    y.remove(0);                      //if the data was inserted remove it from the list
                }
            }
            if(crazy.isEmpty()) {
                solve(crazy, time);     //if there are data entries in the queue, solve them
            }
            time++;     //add to the time
        }
        //print the results of how late the schedule was
        System.out.println("Tasks Late: " + crazy.tasksLate + ", Total Minutes Late: " + crazy.timeLate + "\n");
    }
}

class leftistHeap<E extends Comparable<E>>{
     public static void main(String[] args) {}

     Node<E> root;
     int heapSize;
     int tasksLate;
     int timeLate;

     //default constructor
     public leftistHeap(){
        this.root = null;
        this.heapSize = 0;
        this.tasksLate = 0;
        this.timeLate = 0;
     }

    /**
     * Returns the top element of the queue (aka the smallest)
     * @author Jacob Adams
     * @return The smallest object of the queue
     */
     public Node<E> deleteMin(){
         heapSize--;                //subtract from the size of the heap
         Node<E> temp = root;
         root = merge(root.left, root.right);   //merge the previous root's children and set as the new root
         return temp;               //return the previous root
     }

    /**
     * Returns true if the heap is not empty
     * @author Jacob Adams
     * @return If the heap is not empty
     */
     public boolean isEmpty(){
        return root != null;    //if the tree isn't empty return true
     }

    /**
     * Inserts a node into the heap
     * @author Jacob Adams
     * @param x the node to be inserted
     * @return the new root
     */
     public Node<E> insert(Node<E> x){
         return insert(x.data,x.priority);  //call to other insert function
     }

    /**
     * Inserts a node to the heap given data and a priority
     * @author Jacob Adams
     * @param data the data to insert
     * @param priority the priority level of the data
     * @return the new root
     */
     public Node<E> insert(E data, int priority){
         Node<E> next = new Node<>(data, priority); //create new node based on inputs
         heapSize++;                //add to heap size
         if(root == null){          //if the heap is empty
             root = next;           //put the new node as the root
             return root;           //return the new root
         }
         root = merge(root,next);   //if it is not empty, merge the new node with the existing tree
         return root;               //return the new root;
     }

    /**
     * Switches the children of a given node
     * @author Jacob Adams
     * @param top the top node whose children will be switched
     * @return whether the function was able to complete
     */
     private boolean swap(Node<E> top){
        if(top == null) {
            return false;           //if the base node is null return false
        }
        Node<E> temp = top.left;    //otherwise, set the left node to a temporary value
        top.left = top.right;       //set the left node to the right node
        top.right = temp;           //set the right node to the previous left node
        return true;                //return true
     }

    /**
     * Merges two heaps using the leftist heap rules
     * @author Jacob Adams
     * @param r1 the root of the first tree
     * @param r2 the root of the second tree
     * @return the new root
     */
     private Node<E> merge(Node<E> r1, Node<E> r2){
        if(r1 == null){
            return r2;      //if the first root is null, return the second
        }
        if(r2 == null){
            return r1;      //if the second root is null, return the first
        }
        if(r1.priority < r2.priority){          //if the first root has a higher priority (lower value)
            r1.right = merge(r1.right, r2);     //set root1's right child to the merge of the right child and the second root
            if(newNullPath(r1.right) > newNullPath(r1.left)){   //if the new tree is not leftist
                if(!swap(r1)){                              //swap the children
                    System.out.println("Error swapping");   //print error if swap did not work
                    return null;                            //return null
                }
            }
            return r1;                                      //return root 1
        }
        else{
            r2.right = merge(r2.right, r1);     //otherwise, set root2's right child to the merge of the right child and the first root
            if(newNullPath(r2.right) > newNullPath(r2.left)){   //if the new tree is not leftist
                if(!swap(r2)){                              //swap the children
                    System.out.println("Error swapping");   //print error if swap did not work
                    return null;                            //return null
                }
            }
            return r2;                                      //return r2
        }
     }

    /**
     * Calculates the null path of a given node
     * @author Jacob Adams
     * @param x the node
     * @return the number of moves to reach null
     */
     private int newNullPath(Node<E> x){
        if(x == null){
            return -1;          //if the node is null return -1
        }
        if(x.right == null || x.left == null){
            return 0;           //if the node is 1 step away from null return 0
        }
        int right = newNullPath(x.right) + 1;   //find the right null path length
        int left = newNullPath(x.left) + 1;     //find the left null path length
        return Math.min(right,left);            //return the smaller of the two
     }

    public class Node<E>{
        E data;
        int priority;
        Node<E> right;
        Node<E> left;

        //constructor given data and a priority
        private Node(E data, int priority){
            this.data = data;
            this.priority = priority;
            this.left = null;
            this.right = null;
        }
     }
}
