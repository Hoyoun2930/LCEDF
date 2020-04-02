import java.util.*;


public class Job {

	private double execTime;
	private double releaseTime;
	private double Deadline;
	private int TaskIndex;
	private Vector<Object> printData;
	private boolean scheduled;
	private boolean isScheduled;
	private boolean preemptive;
	private int numPreemption;
	private int priority=0;
	public  long time_stamp=0;
	
	public double sMax=0; //for LCEDF
	public double sMin=0;
	
	
	
	public Job() {
		
	}

	public Job(double exectime, double releaseTime, double deadline, int taskIndex){

		this.execTime = exectime;
		this.releaseTime = releaseTime;
		this.Deadline = deadline;
		this.TaskIndex = taskIndex;
		this.isScheduled=false;
		this.preemptive=true;
		this.numPreemption=0;
	}
	
	public Job(double exectime, double releaseTime, double deadline, int taskIndex, int x, int y){

		this.execTime = exectime;
		this.releaseTime = releaseTime;
		this.Deadline = deadline;
		this.TaskIndex = taskIndex;
		this.isScheduled=false;
		this.preemptive=true;
		this.numPreemption=0;
	}
	
	public Job(double exectime, double releaseTime, double deadline, int taskIndex, int priority){

		this.execTime = exectime;
		this.releaseTime = releaseTime;
		this.Deadline = deadline;
		this.TaskIndex = taskIndex;
		this.isScheduled=false;
		this.preemptive=true;
		this.numPreemption=0;

		this.priority= priority;
	}
	
	
	public Job(double exectime, double releaseTime, double deadline, int taskIndex, boolean preemptive){

		this.execTime = exectime;
		this.releaseTime = releaseTime;
		this.Deadline = deadline;
		this.TaskIndex = taskIndex;
		this.isScheduled=false;
		this.preemptive=preemptive;
		this.numPreemption=0;
	}
	

	public Job(Job job){

		this.execTime = job.execTime;
		this.releaseTime = job.releaseTime;
		this.Deadline = job.Deadline;
		this.TaskIndex = job.TaskIndex;
		this.printData = job.printData;
		this.isScheduled=job.isScheduled;
	}

	public void setIsScheduled(){
		this.isScheduled=true;
	}
	
	public void unsetIsScheduled(){
		this.isScheduled=false;
	}
	
	public boolean getIsScheduled(){
		return this.isScheduled;
	}
	
	public void adjustDeadline(double d) {
		this.Deadline=this.Deadline+d;
	}

	
	public boolean getPreemptive(){
		return this.preemptive;
	}
	
	public void setPrintData(Object obj){

		this.printData = new Vector<Object>();
		this.printData.add(obj);
	}
	
	public void incPrem(){
		numPreemption++;
	}
	
	public int getPrem(){
		return numPreemption;
	}
	

	public int getPriority() {
		return priority;
	}
	
	public double getexecTime(){

		return this.execTime;
	}

	public void setexecTime(double execTime){

		this.execTime = execTime;
		this.scheduled = true;
	}
	


	public Vector<Object> getPrintData(){

		return this.printData;
	}

	public	double getReleaseTime(){

		return this.releaseTime;
	}

	public	double getDeadline(){

		return this.Deadline;
	}

	public int getTaskIndex(){

		return this.TaskIndex;
	}

	public void printData(){
		
		System.out.println("EXECTIME="+this.execTime+",RELEASE="+this.releaseTime+",DEADLINE="+this.Deadline+",TASKINDEX="+this.TaskIndex);		
	}
	
	public void print(){

		//double d = ((Double)(this.printData.get(0))).doubleValue();
		double d=0;
//		d = ((double)Math.round(d*100))/100;
		double e;
		if(this.scheduled)
			e = d-1;
		else
			e = d;
		//System.out.println("EXECTIME="+this.execTime+",RELEASE="+this.releaseTime+",DEADLINE="+this.Deadline+",TASKINDEX="+this.TaskIndex+",DELTA_BEFORE_SCHEDULING="+d+",DELTA_AFTER_SCHEDULING="+e+" "+nc[0]);
		this.scheduled = false;
	}
	
	
	
	public void setInitialS() {
		this.sMin = this.releaseTime;
		this.sMax = this.Deadline-this.execTime;
	}
}