import java.util.*;

public interface Scheduler {

public Vector<Job> schedule(int t, int numberOfProcs, Vector<Job> activeJobs, Vector<Double> taskUtil, Vector<Double> taskDen, Vector<Task> taskset);

//public Vector<Job> schedule(int t, int numberOfProcs, Vector<Job> activeJobs, Vector<Double> taskUtil, Vector<Double> taskDen, Vector<Task> taskset,long x[]);

}