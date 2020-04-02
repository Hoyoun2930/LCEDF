import java.io.*;
import java.util.*;
//import java.io.*;

public class Simulator_CEDF {

	// Variables capturing basic parameters of the system
	public static final double error = 0.0000001;
	private int simulationNum;
	private double startTime = 0, endTime = 0;
	private int maxPeriod;
	private int scheduler;
	private Scheduler algorithm;
	private int numberOfProcs;
	private int numberOfTasks;
	private Vector<Task> taskset;
	private Vector<Double> taskUtil; // List of utilizations of individual tasks
	private Vector<Double> taskDen; // List of densities of individual tasks

	private Vector<Double> latestReleaseTime;

	// List of jobs that are currently active
	private Vector<Job> activeJobs; // List of active jobs at current time t
	private Vector<Job> critical_queue; // critical queue at current time t
	

	// Variables for printing job and schedule information at each time instant
	private int printStart = 0;// 0;//-1; // starting time instant for print range
	private int printEnd = -1;// 70;//-1; // ending time instant for print range
	// private PrintStream standard;

	// Variables for printing idle instant information whenever it occurs (only
	// focus on last idle instant in a sequence)
	private boolean atleastOneIdle = false;
	private int printStartIdle = -1; // starting time instant for print range
	private int printEndIdle = -1; // ending time instant for print ranged


	private int[] next_t_array;
	private int[] next_index_array;
	private double[] next_adjust_array;
	public static int postpone=0;
	private int[] Popo;
	public Simulator_CEDF() {}

	public static void main(String[] args) {
		String A[]= {"0","1000","24","2","3","0","202", "22", "202", "312", "17", "312","81","74","81"};
		System.out.println(main2(A));
		
		/*String A1[]= {"4", "1000", "24","2","4","0","55", "43", "55", "270", "12", "270", "647", "115", "647", "65", "13", "65" };
		String A2[]= {"4", "1000", "24","2","5","0","152", "12", "152", "172", "70", "172", "108", "11", "108", "43", "3", "43", "80", "46", "80"};
		String A3[]= {"4", "1000", "24","2","4","0","915", "105", "915", "782", "160", "782", "279", "124", "279", "449", "321", "449"};
		String A4[]= {"4", "1000", "24","4","5","0","24", "6", "24", "28", "22", "28", "983", "612", "983", "205", "118", "205", "253", "242", "253"};
		
	
		System.out.println(main2(A1));
		System.out.println(main2(A2));
		System.out.println(main2(A3));
		System.out.println(main2(A4));*/
	}
	public static String main2(String[] args) {

		Simulator_CEDF s = new Simulator_CEDF();

		s.simulationNum = (Integer.valueOf(args[0])).intValue();
		s.maxPeriod = (Integer.valueOf(args[1])).intValue();
		s.scheduler = (Integer.valueOf(args[2])).intValue();
		
		s.algorithm = new Algorithm_CEDF();
		//System.out.println("NEW SIMULATION: CEDF");
		
		s.numberOfProcs = (Integer.valueOf(args[3])).intValue();
		s.numberOfTasks = (Integer.valueOf(args[4])).intValue();

		s.taskset = new Vector<Task>();
		s.taskUtil = new Vector<Double>();
		s.taskDen = new Vector<Double>();
		

		s.latestReleaseTime = new Vector<Double>();
		
		s.activeJobs = new Vector<Job>();
		s.critical_queue = new Vector<Job>();
		// s.prev_execJobs = new Vector<Job>();

		// Vector of task periods for computing lcmOfPeriods
		Vector<Double> periods = new Vector<Double>();
		// Fill out variables with data from command line input
		// Initialize variables for sporadics and preemptions
		int tmp_l=s.numberOfTasks;
		for (int i = 0, flag=1; i < tmp_l && s.numberOfProcs>1; i++)
		{
			double D1=Double.valueOf(args[8 + 3 * i]), C1=Double.valueOf(args[7 + 3 * i]);
			if(D1==C1)
			{
				
				args[6 + 3 * i]="0";
				args[7 + 3 * i]="0";
				args[8 + 3 * i]="0";
				s.numberOfProcs-=1;
				
			}
		}
		
		s.next_t_array = new int[s.numberOfProcs];
		s.next_index_array = new int[s.numberOfProcs];
		s.next_adjust_array = new double[s.numberOfProcs];
		
		
		for (int i = 0; i < s.numberOfProcs; i++) {
			s.next_t_array[i] = -1;
			s.next_index_array[i] = -1;
			s.next_adjust_array[i] = -1;
		}
		int tmp=0;
		for (int i = 0; i < s.numberOfTasks;i++) {
			// Check if execution time of task is greater than 0
			if ((Double.valueOf(args[7 + 3 * i])).doubleValue() > 0) {
				s.taskset.add(new Task((Double.valueOf(args[6 + 3 * i])).doubleValue(), (Double.valueOf(args[7 + 3 * i])).doubleValue(), (Double.valueOf(args[8 + 3 * i])).doubleValue()));
				periods.add(Double.valueOf(args[6 + 3 * i]));
				s.taskUtil.add(Double.valueOf((Double.valueOf(args[7 + 3 * i])).doubleValue() / (Double.valueOf(args[6 + 3 * i])).doubleValue()));
				s.taskDen.add(Double.valueOf((Double.valueOf(args[7 + 3 * i])).doubleValue() / (Double.valueOf(args[8 + 3 * i])).doubleValue()));					
				tmp++;
				
			}
		}
		s.numberOfTasks=tmp;
		s.Popo = new int[s.numberOfTasks];
		int T2[] = new int[s.numberOfTasks];
		int C2[] = new int[s.numberOfTasks];
		int D2[] = new int[s.numberOfTasks];
		for (int i = 0; i < s.numberOfTasks; i++) {
			Task temp = s.taskset.get(i);
			T2[i] = (int) (temp.getPeriod() + error);
			C2[i] = (int) (temp.getexecTime() + error);
			D2[i] = (int) (temp.getDeadline() + error);
			//System.out.println(T2[i]+" "+C2[i]+" "+D2[i]);
		}
		for(int i=0;i < s.numberOfTasks;i++)
		{
			int cnt=0;
			for(int j=0;j<s.numberOfTasks;j++)
			{
				if(i==j || s.Popo[j]==1)continue;
				if(C2[j]-1 > D2[i]-C2[i] && C2[i]-1 <= D2[j]-C2[j])cnt++;
			}
			if(cnt>=s.numberOfProcs)s.Popo[i]=1;
		}
		
		//for(int i=0;i<s.numberOfTasks;i++)System.out.println(s.Popo[i]);
		
		for (int i = 0; i < s.numberOfTasks; i++) {
			if (C2[i] > 0) {
				Job j = new Job(C2[i],new Double(0.0),D2[i],i);
				j.setInitialS();
				s.activeJobs.add(j);
				s.latestReleaseTime.add(0.0);
				
				if(s.Popo[i]==1) {
					s.critical_queue.add(j);
				}
				
			}
		}
		s.numberOfTasks = s.taskset.size();
		s.endTime = LCM.generateLCM(periods) + 1;
		s.postpone=0;
		return s.Simulate();
	}

	public String Simulate() {
		int t = 0;
		int schedulable = 0;
		int finish = 1;
		//*************************************************************
		
		int T2[] = new int[numberOfTasks];
		int C2[] = new int[numberOfTasks];
		int D2[] = new int[numberOfTasks];
		for (int i = 0; i <numberOfTasks; i++) {
			Task temp = taskset.get(i);
			T2[i] = (int) (temp.getPeriod() + error);
			C2[i] = (int) (temp.getexecTime() + error);
			D2[i] = (int) (temp.getDeadline() + error);
			//System.out.println(T2[i]+" "+C2[i]+" "+D2[i]);
		}
		
		//make critical queue first for Clairvoyant & schedulability check
		//for (t = (int) startTime; t < (int) endTime; t++) UpdateActiveJobs(t);
		//critical_queue = new Vector<Job>(activeJobs);
		//release_queue = new Vector<Job>(activeJobs);
		//activeJobs.clear();
		Comparator<Job> smax_comp = Comparators.SMAXComparator(t);
		Collections.sort(critical_queue, smax_comp);
		//Collections.sort(release_queue, Release_comp);
		//schedulable = ((Algorithm_CEDF)algorithm).isSchedulable(T2,C2,D2,numberOfTasks,numberOfTasks);
		
		//*************************************************************
		for (t = (int) startTime; t < (int) endTime; t++) {
			if (!CheckDeadline(t)) {
				finish = 0;
				break;
			}
			UpdateActiveJobs(t);
			/*if (!UpdateActiveJobsFromCQ(t)) {
				finish = 0;
				break;
			}*/
			Schedule(t);
		}
		// Print a summary of the number of context switches
		String s = "schedulable: " + schedulable + " finish: " + finish + " numberOfProcs: " + numberOfProcs + " numberOfTasks: " + numberOfTasks+" Postpone: "+postpone;
		s+=" Response Time: ";
		for (int i = 0; i < numberOfTasks; i++) {
			s += " " + taskset.get(i).getResponseTime();// +"("+taskset.get(i).getDeadline()+")";
		}
//		s+=" total_time / endTime";
//		s += " " + total_time / endTime + " " + total_time2 / endTime;
		return s;
		
	}

	// Check if some job has missed its deadline at time t. If yes, then stop the
	// scheduling and print details.
	public boolean CheckDeadline(int t) {
		for (int i = 0; i < activeJobs.size(); i++) {
			Job job = activeJobs.get(i);
			if (((job.getDeadline() <= t) && (job.getexecTime() > 0))) {
				//System.out.println(job.getDeadline()+" "+job.getexecTime());
				// System.out.println("DEADLINE MISS: "+"TIME="+t+", TASKINDEX="+job.getTaskIndex());
				// System.out.print("0 ");
				// System.out.println(t);
				//System.out.println(job.getReleaseTime()+" "+job.getDeadline()+" "+t);
				if ((t >= printStart) && (t <= printEnd)) {
					System.out.println("DEADLINE MISS: " + "TIME=" + t + ", TASKINDEX=" + job.getTaskIndex());
				}
				return false;
			}
		}
		return true;
	}
	// If a new job is released at time t, then update the list of active jobs
	// Here is where the random logic for sporadics is implemented
	public void UpdateActiveJobs(int t) {
		for (int i = 0; i < taskset.size(); i++) {
			Task task = taskset.get(i);

			if (latestReleaseTime.get(i) + task.getPeriod() <= t) {
				if(Popo[i]==1)
				{
					for(int j=0;j<critical_queue.size();j++)
					{
						if(critical_queue.elementAt(j).getTaskIndex()==i)
						{
							activeJobs.add(critical_queue.elementAt(j));
							break;
						}
					}
				}
				else
				{
					Job job = new Job((int)(task.getexecTime()*Math.random())+1, t, t + task.getDeadline(), i);
					job.setInitialS();
					activeJobs.add(job);
				}
				
				
				latestReleaseTime.set(i, Double.valueOf(t));
				
			}
		}
	}
	public boolean UpdateActiveJobsFromCQ(int t) {
		for (int i = 0; i < critical_queue.size(); i++) {
			Job j = critical_queue.elementAt(i);
			if(j.sMin > j.sMax) {
				//System.out.println(j.getReleaseTime()+" "+j.getDeadline()+" "+t);
				return false; //already missed by postpone
			}
			if(j.sMin <= t) {
				if(activeJobs.indexOf(j)==-1) {
					activeJobs.add(j);
					
					int index = j.getTaskIndex();
					Task task = taskset.get(index);
					Job job= new Job(task.getexecTime(),latestReleaseTime.get(index)+task.getPeriod() , latestReleaseTime.get(index)+task.getPeriod()+task.getDeadline(),index);
					
					
					job.setInitialS();
					
					critical_queue.addElement(job);
					
					latestReleaseTime.set(index, latestReleaseTime.get(index)+task.getPeriod());
					//critical_queue.remove(j);
				}
			}
		}
		
		return true;
	}
	// Calls the appropriate scheduler and updates all variables once scheduling
	// decisions are made
	public void Schedule(int t) {
		// Print job parameters
		if (((t >= printStart) && (t <= printEnd)) || ((t >= printStartIdle) && (t <= printEndIdle))) {
			// for print useful information
			for (int i = 0; i < activeJobs.size(); i++) {
				Job j = activeJobs.get(i);
				double deadline = j.getDeadline() - t;
				int index = j.getTaskIndex();
				double den = taskDen.get(index);
				double delta = j.getexecTime() - (den * (deadline - 0));
				double delta2 = j.getexecTime() - (den * (deadline - 1));
				double dynamic_den = j.getexecTime() / deadline;
				Double info[] = new Double[3];
				info[0] = Double.valueOf(delta);
				info[1] = Double.valueOf(delta2);
				info[2] = Double.valueOf(dynamic_den);
				j.setPrintData(info);
				activeJobs.set(i, j);
				if (delta > 3) {
					System.out.println("MARK");
				}
			}
			System.out.println("\n\nAt Time " + t);
			System.out.println("----------------------------------------------------------");
			System.out.println("----------------------------------------------------------");
		}
		if (((t >= printStart) && (t <= printEnd)) || ((t >= printStartIdle) && (t <= printEndIdle))) {
			System.out.println("TIME: [" + t + "," + (t + 1) + ")");
			System.out.print("SCHEDULED TASK INDICES: ");
		}
		activeJobs = ((Algorithm_CEDF)algorithm).schedule(t, numberOfProcs, activeJobs, critical_queue, taskUtil, taskDen, taskset, Popo);
		// Variable keeping track of how many processors have been used so far
		int m = 0;

		for (int i = 0; i < activeJobs.size(); i++) {
			if (m >= numberOfProcs) {
				Job job = activeJobs.get(i);
				if (job.getIsScheduled()) {
					Task task = taskset.get(job.getTaskIndex());
					task.incPrem();
					job.incPrem();
				}
				job.unsetIsScheduled();
			} else {
				
				Job job = activeJobs.get(i);
				if(job.sMin>t)continue;
				m = m + 1;
				int index = job.getTaskIndex();
				// long time1=System.currentTimeMillis();
				long start = System.nanoTime();
				job.setexecTime(job.getexecTime() - 1);
				// long time2=System.currentTimeMillis();
				long end = System.nanoTime();
				job.setIsScheduled();
				activeJobs.set(i, job);
				// measuring preemptions incurred by task start
				
				
				//System.out.println("p: "+m+" t: "+t+" "+job.getReleaseTime()+" "+job.getDeadline()+" "+job.getexecTime()+" "+job.getTaskIndex());
				//System.out.println("min: "+job.sMin+" max: "+job.sMax);
				
				
				if (((t >= printStart) && (t <= printEnd)) || ((t >= printStartIdle) && (t <= printEndIdle)))
					System.out.print(index + ", ");
			}
		}
		
		// Prepare variables for printing idle slots
		if (m < numberOfProcs) {
			atleastOneIdle = true;
		}
		else {
			atleastOneIdle = false;
		}
		if (m > numberOfProcs) {
			System.out.println(t);
			System.out.println("ERROR: SCHEDULED MORE THAN M");
			System.exit(0);
		}
		for (int i = 0; i < activeJobs.size(); i++) {
			/* test start */
			 //System.out.print(activeJobs.get(i).getIsScheduled());
			/* test end */
			if (activeJobs.get(i).getexecTime() == 0) {
				Job job = activeJobs.get(i);
				Task task = taskset.get(job.getTaskIndex());
				task.setMaxPrem(job.getPrem());
				/* response time start */
				double responseTime = (int) ((t + 1.0 + error) - activeJobs.get(i).getReleaseTime());
				int index = activeJobs.get(i).getTaskIndex();
				taskset.get(index).setResponseTime(responseTime);
				/* response time end */
				/* test start */
				// if (index==2 && taskset.get(index).getResponseTime()>544) {
				// System.out.println("t="+t);
				// }
				/* test end */
				activeJobs.remove(i);
				i = i - 1;
			}
		}
		 //System.out.println();
	}

	public void printJobs(double t, int m) {

		if ((t >= this.printStartIdle) && (t <= this.printEndIdle) && (atleastOneIdle) && (m == numberOfProcs))
			System.out.println("(IDLE INSTANT AT t-1 AND NONE AT t)");

		for (int i = 0; i < activeJobs.size(); i++)
			activeJobs.get(i).print();
	}
}
