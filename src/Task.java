
public class Task {

	private double Period;
	private double execTime;
	private double Deadline;
	private double responseTime;
	private int numPrem;//total preemption
	private int numPreempting; // total preemption incurred by this this task
	private int maxPrem;//max preemption prejob
	private int priority=0;
	private int taskNumber=0;
	
	
	
	public Task(double period, double exectime, double deadline, int priority,int taskNumber){
		this.Period = period;
		this.execTime = exectime;
		this.Deadline = deadline;
		this.responseTime = execTime;
		this.numPrem = 0;
		this.maxPrem = 0;
		this.numPreempting=0;
		this.priority = priority;
		this.taskNumber= taskNumber;
		
	}	
	
	public Task(double period, double exectime, double deadline,  int priority){
		this.Period = period;
		this.execTime = exectime;
		this.Deadline = deadline;
		this.responseTime = execTime;
		this.numPrem = 0;
		this.maxPrem = 0;
		this.numPreempting=0;
		this.priority = priority;
		
	}
	

	

	public Task(double period, double exectime, double deadline){
		this.Period = period;
		this.execTime = exectime;
		this.Deadline = deadline;
		this.responseTime = execTime;
		this.numPrem = 0;
		this.maxPrem = 0;

		
	}
		

	
	public Task(Task task){
		
		this.Period = task.Period;
		this.execTime = task.execTime;
		this.Deadline = task.Deadline;
		this.responseTime = task.execTime;
		this.numPrem = task.numPrem;
		this.maxPrem = 0;
	}

	public double getPeriod(){
		
		return this.Period;
		
	}
	
	public void adjustDeadline(double d) {
		this.Deadline=this.Deadline+d;
	}
	
	public double getexecTime(){
		
		return this.execTime;
	}
	
	public int getPrem(){
		return numPrem;
	}
	
	public int getPreempting() {
		return numPreempting;
	}
	
	public void incPrem() {
		numPrem++;
	}
	
	public void incPreempting() {
		numPreempting++;
	}
	
	
	public int getTaskNumber() {
		return taskNumber;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public int getMaxPrem(){
		return maxPrem;
	}
	
	public void setMaxPrem(int prem) {
		if (prem>maxPrem)
			maxPrem=prem;
	}
	
	
	public void setPriority(int priority) {
		this.priority=priority;
	}
	
	
	public	double getDeadline(){
		
		return this.Deadline;
	}
	
	public double getResponseTime() {
		return this.responseTime;
	}
	public void setResponseTime(double responseTime) {
		if (responseTime > this.responseTime) {
			this.responseTime = responseTime;
		}
	}
	
}