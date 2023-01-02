
public class Task1 extends Task {
    public Task1(int ID, int start, int deadline, int duration) {
        super(ID, start, deadline, duration);
    }

    // Prioirity is deadline
    @Override
    public int compareTo(Task t2) {
        return deadline - t2.deadline;
    }
}