import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

public class MaxMin {
    private int[][] start;
    private int[][] residual;
    private boolean[] visited;
    private int[] prev;
    private final int count;
    private final int sink;
    private int flow;

    //MaxFlow constructor
    public MaxMin(Graph start){
        this.start = start.capacity;
        this.count = start.vertexCt;
        this.residual = new int[count][count];
        this.visited = new boolean[count];
        this.prev = new int[count];
        this.sink = count - 1;
        this.flow = 0;
        for(int i = 0 ; i < count ; i++){
            System.arraycopy(start.capacity[i], 0, this.residual[i], 0, count);
            visited[i] = false;
            prev[i] = -1;
        }
    }

    /**
     * Prints how much each edge is transporting
     * @author Jacob Adams
     */
    private void printEdges(){
        int temp;
        for(int i = 0 ; i < count ; i++){
            for(int j = 0 ; j < count ; j++){               //for all edges
                if(residual[i][j] < start[i][j]){           //if the edge transports anything
                    temp = start[i][j] - residual[i][j];    //calculate the quantity
                    System.out.println("Edge("+i+","+j+") transports "+temp+" cases");  //print it in a structure
                }
            }
        }
        System.out.println();   //print a new line after all the edges have been printed
    }

    /**
     * Resets the visited array to all false
     * @author Jacob Adams
     */
    private void resetVisited(){
        for(int i = 0 ; i < count ; i++){
            visited[i] = false; //reset all values in the visited array
        }
    }

    /**
     * Public method of getMinCut
     * @author Jacob Adams
     * @return whether the result is valid
     */
    public boolean getMinCut(){
        if(residual != start){      //if the max flow has already been run
            System.out.println("MIN CUT:"); //print the start
            getMinCut(count);               //run the min cut algorithm
            return true;
        }else{
            System.out.println("Run getMaxFlow before running getMinCut");  //print error message
            return false;
        }
    }

    /**
     * Finds, and prints the min cut edges
     * @author Jacob Adams
     * @param count the number of nodes
     */
    private void getMinCut(int count){
        ArrayList<Integer> r = new ArrayList<>();   //make a new arraylist
        int temp;
        r.add(0);           //add the first node to r
        int size = -1;
        while(size != r.size()){    //while there are more nodes added to r
            size = r.size();        //set size to the current size of the array
            for(int x = 0 ; x < r.size() ; x++){    //for all values in r
                for(int i = 0 ; i < count ; i++){   //for all nodes
                    if(residual[r.get(x)][i] > 0 && !r.contains(i)){    //if there is a path in the residual
                        r.add(i);                                       //to a node not in r, add it to r
                    }
                }
            }
        }
        ArrayList<Integer> pullFrom = (ArrayList<Integer>) r.clone();   //clone r
        int total = 0;  //initial cost of cuts is zero
        while(!pullFrom.isEmpty()){     //while the second arraylist is not empty
            temp = pullFrom.remove(0);      //pull the next node
            for(int i = 0 ; i < count ; i++){     //for all other nodes
                if(start[temp][i] > 0 && !r.contains(i)){  //if there is an edge to a node not in r
                    int moves = start[temp][i]-residual[temp][i];
                    total += start[temp][i];
                    System.out.println("Edge("+temp+","+i+") transports "+moves+" cases");  //print the edge
                }
            }
        }
        System.out.println("Total Cost of Cuts: " + total); //print total cost
    }

    /**
     * Public method of getMaxFlow
     * @author Jacob Adams
     * @return the flow of the graph
     */
    public int getMaxFlow(){
        System.out.println("MAX FLOW:");    //print initial line
        if(getMaxFlow(sink)){   //if it succesfully ran
            return flow;        //return the flow
        }
        return -1;              //otherwise return an error
    }

    /**
     * Calculates any possible flow available in a max flow graph
     * @author Jacob Adams
     * @param sink The highest node
     * @return if it was able to complete successfully
     */
    private boolean getMaxFlow(int sink){
        while(hasPath(sink)){               //while there is an unused path
            int available = 9999999;        //set available to infinity
            for(int i = sink ; i != 0 ; i = prev[i]){
                available = Math.min(available, residual[prev[i]][i]);  //find the lowest edge weight along the path
            }
            System.out.print("Found Flow " + available + ": "+ sink);   //print the flow that was found
            for(int i = sink ; i != 0 ; i = prev[i]){
                System.out.print(" <- " + prev[i]); //print the path that it takes
                residual[prev[i]][i] -= available;  //update the residual graph
                residual[i][prev[i]] += available;
                if(residual[prev[i]][i] < 0){
                    System.out.println("Error in removing capacity");   //catch errors
                    return false;
                }
            }
            System.out.println();   //print new line
            this.flow += available; //update flow
        }
        System.out.println("Flow Produced: "+flow+"\n");    //print total flow produced
        printEdges();   //call print edges
        return true;    //return a successful run
    }

    /**
     * Finds if there is a possible path with flow left
     * @author Jacob Adams
     * @param sink the highest node
     * @return if there is a path left to find
     */
    private boolean hasPath(int sink){
        Queue<Integer> queue = new PriorityQueue<>();   //declare new queue
        int temp;
        queue.add(0);   //add the first node
        resetVisited(); //reset the visited array
        visited[0] = true;  //mark the first node as visited
        while(!queue.isEmpty() && !visited[sink]){  //while a path has not been found and the queue is not empty
            temp = queue.poll();    //pull from the queue
            for(int i = 0 ; i < count ; i++){   //for all adjacent nodes
                if(residual[temp][i] > 0 && !visited[i]){   //if there is an available path
                    prev[i] = temp;     //update the previous array
                    visited[i] = true;  //mark it as visited
                    queue.add(i);       //add it to the queue
                    if (sink == i){     //if we are at the last node
                        return true;    //return that there is a path
                    }
                }
            }
        }
        return false;   //if no path was found return false
    }
}
