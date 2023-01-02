import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TestSched {

    /**
     * Gets tasks from the files and inserts them in array lists
     * @param filename the file being read
     * @param task1 the first ArrayList
     * @param task2 the second ArrayList
     * @param task3 the third ArrayList
     */
    public static void readTasks(String filename, ArrayList<Task> task1, ArrayList<Task> task2, ArrayList<Task> task3) {
        try {
            File file = new File(filename);     //create new file for the given file name
            Scanner input = new Scanner(file);  //open the file for reading
            int i = 1;  //initialize iterator variable
            while (input.hasNextInt()) {    //while there are still integers to be read
                int start = input.nextInt(), deadline = input.nextInt(), duration = input.nextInt();    //read from the file
                Task1 temp1 = new Task1(i, start, deadline, duration);  //create a task for tasks 1, 2 and 3
                Task1 temp2 = new Task1(i, start, deadline, duration);
                Task1 temp3 = new Task1(i, start, deadline, duration);

                task1.add(temp1);   //insert the tasks to their respective ArrayLists
                task2.add(temp2);
                task3.add(temp3);
                i++;    //add 1 to i
            }
        }
        catch (java.io.FileNotFoundException e){   //handle if the file is not found
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Scheduler s = new Scheduler();
        String [] files = {"taskset1.txt","taskset2.txt","taskset3.txt","taskset4.txt","taskset5.txt" };
        for (String f : files) {
            ArrayList<Task> t1 = new ArrayList();    // elements are Task1
            ArrayList<Task> t2 = new ArrayList();    // elements are Task2
            ArrayList<Task> t3 = new ArrayList();    // elements are Task3
            readTasks(f, t1, t2, t3);
            s.makeSchedule("Deadline Priority "+ f, t1);
            s.makeSchedule("Startime Priority " + f, t2);
            s.makeSchedule("Wild and Crazy priority " + f, t3);
       }

    }
}
