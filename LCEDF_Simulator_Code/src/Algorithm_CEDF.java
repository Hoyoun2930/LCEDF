import java.util.*;
import java.lang.Math;
import java.lang.reflect.Array;

public class Algorithm_CEDF implements Scheduler {
	
	private boolean isPostponed(Job rJob, Job cJob) {
	        if((rJob.sMin + rJob.getexecTime() > cJob.sMax) //it occurs deadline miss of cJob? 
	        		&&(rJob != cJob) //only consider different task
	        		&&(cJob.sMin <= cJob.sMax) //cJob already missed deadline
	        		)
	            return true;
	        return false;
	}
	
	private int isPostponed(Vector<Job> activeJobs,Vector<Job> critical_queue,Job cJob, int np, int critical_index, int numberOfProcs,int t,int[] Popo) {
        Job rJob;
        int cnt=Math.max(0, np-activeJobs.size());
		for(int i=0;i<np && i<activeJobs.size();i++)
        {
        	rJob=activeJobs.elementAt(i);
        	if(!isPostponed(rJob,cJob))cnt++; 	
        }
		if(cnt>0)return cnt;
		for(int i=0;i<critical_index && i<numberOfProcs&&i<critical_queue.size();i++)
		{
			if(i==critical_index)continue;
			rJob=critical_queue.elementAt(i);
			if((!isPostponed(cJob,rJob)||!isPostponed(rJob,cJob))&&cnt==0) {
				cnt=-1;
				break;
			}
		}
        return cnt;
	}
	
	public Vector<Job> schedule(int t, int numberOfProcs, Vector<Job> activeJobs, Vector<Job> critical_queue, Vector<Double> taskUtil,
			Vector<Double> taskDen, Vector<Task> taskset, int[] Popo) {
		
		//*******************************************************
		//get number of active process for multicore 
		int critical_index = 0;//Math.max(0, numberOfProcs - activeJobs.size());
		int cnt,np=numberOfProcs,flag=0;
		//*******************************************************
		Job tmp_job;
		Vector<Job> sortedJobs = new Vector<Job>();
		Vector<Job> tempJobs = new Vector<Job>();
		Comparator<Job> dead_comp = Comparators.EDF_NComparator(t);
		Comparator<Job> smax_comp = Comparators.SMAXComparator(t);
		Collections.sort(critical_queue, smax_comp);
		Collections.sort(activeJobs, dead_comp);
		int check[]=new int[numberOfProcs];
		
		for(int i=0;i<numberOfProcs;i++)check[i]=0;


		if(critical_queue.size()==0)return activeJobs;
		for(int i=0;i<activeJobs.size();i++)
		{
			tmp_job=activeJobs.elementAt(i);
			if(tmp_job.sMin<t)tmp_job.sMin=t;
			if(tmp_job.getIsScheduled())
			{
				sortedJobs.add(tmp_job);
				activeJobs.remove(tmp_job);
				np--;
			}
		}
		
		
		while(!activeJobs.isEmpty())
		{
			
			boolean early_dispath = false;
			Job rJob = activeJobs.elementAt(0); //rJob: first job of ready queue
			cnt=0;
			Job cJob; 
			if(critical_index>=numberOfProcs || np<=0 )
			{
				while(!activeJobs.isEmpty())
				{
					rJob = activeJobs.elementAt(0);
					if(!rJob.getIsScheduled())rJob.sMin=t+1;
					sortedJobs.add(rJob);
					activeJobs.removeElementAt(0);
				}
				while(!tempJobs.isEmpty())
				{
					sortedJobs.add(tempJobs.elementAt(0));
					tempJobs.removeElementAt(0);
				}
				
				return sortedJobs;
			}
			
			if(rJob.sMin>t)
			{
				activeJobs.remove(rJob);
				tempJobs.add(rJob);
				continue;
			}
			if(rJob.sMin<t)rJob.sMin=t;
			if(rJob.getIsScheduled() || Popo[rJob.getTaskIndex()]==1 || critical_index>=critical_queue.size())early_dispath=true;
			if(!early_dispath)cnt=isPostponed(activeJobs, critical_queue,cJob=critical_queue.elementAt(critical_index), np,critical_index,numberOfProcs,t,Popo);
			

			//check postpone condition
			if(!early_dispath && cnt==0)//isPostponed(rJob,cJob=critical_queue.elementAt(critical_index)))//&&t<cJob.getReleaseTime()) 
			{
						
					cJob=critical_queue.elementAt(critical_index);
					for(int i=0;i<sortedJobs.size();i++) {
						if(!isPostponed(sortedJobs.elementAt(i),cJob) && check[i]==0)
						{
							
							check[i]=1;
							sortedJobs.add(rJob);
							activeJobs.remove(rJob);
							critical_index++;
							np--;
							break;
						}
					}
					if(activeJobs.size()==0 || rJob != activeJobs.elementAt(0))continue;
					
					if(Popo[cJob.getTaskIndex()]==1 && cJob.sMin <=t)
					{
						sortedJobs.add(cJob);
						critical_queue.remove(cJob);
						activeJobs.remove(cJob);
						Task task = taskset.get(cJob.getTaskIndex());
						Job job1 = new Job((int)(task.getexecTime()*Math.random())+1,cJob.getDeadline(),cJob.getDeadline()+task.getDeadline(),cJob.getTaskIndex());
						job1.setInitialS();				
						critical_queue.add(job1);
						
					}
					else if(activeJobs.size()+critical_index <= numberOfProcs)critical_index++;
					
					for(int i=Math.min(np, activeJobs.size())-1;i>=0;i--)//i=0;i<np && i<activeJobs.size();i++)
					{
						rJob=activeJobs.elementAt(i);
						if(Popo[rJob.getTaskIndex()]==1)continue;
						if(isPostponed(rJob, cJob))
						{
							activeJobs.remove(rJob);
							break;
						}
					}
					
					double mn=rJob.sMin+1;
					for(int i=0;i<sortedJobs.size();i++)//sMin Update And Reinsert
					{
						if(i==0)mn=Math.min(t+sortedJobs.elementAt(i).getexecTime(),cJob.sMin+cJob.getexecTime());
						if(t+sortedJobs.elementAt(i).getexecTime()<mn)mn=t+sortedJobs.elementAt(i).getexecTime();
					}	
					rJob.sMin=Math.max(mn, cJob.sMin+1);
					tempJobs.add(rJob);
					np--;					
					Simulator_CEDF.postpone++;									
			}
			else {
				//just dispatch
				if(early_dispath)
				{
					sortedJobs.add(rJob);
					activeJobs.remove(rJob);
				}
				else if(cnt==-1)
				{
					rJob=activeJobs.elementAt(0);
					//check[sortedJobs.size()]=1;
					sortedJobs.add(rJob);
					activeJobs.remove(rJob);
					critical_index++;
					
				}
				else 
				{
					cJob=critical_queue.elementAt(critical_index);
					for(int i=0;i<np && i<activeJobs.size();i++)
					{
						rJob=activeJobs.elementAt(i);
						if(!isPostponed(rJob, cJob))
						{
							sortedJobs.add(rJob);
							activeJobs.remove(rJob);
							break;
						}
					}
					critical_index++;
				}
				
				if(Popo[rJob.getTaskIndex()]==1 && !rJob.getIsScheduled()) {
					//System.out.println(t+" 123");
					critical_queue.remove(rJob);
					Task task = taskset.get(rJob.getTaskIndex());
					Job job1 = new Job((int)(task.getexecTime()*Math.random())+1,rJob.getDeadline(),rJob.getDeadline()+task.getDeadline(),rJob.getTaskIndex());
					job1.setInitialS();
					//System.out.println("plus "+job1.sMin+" "+job1.sMax+" "+" @ "+job1.getTaskIndex() + " "+job1.getexecTime());
					critical_queue.add(job1);
				}
				np--;
				
				
			}
			
		}
		while(!tempJobs.isEmpty())
		{
			sortedJobs.add(tempJobs.elementAt(0));
			tempJobs.removeElementAt(0);
		}
		return sortedJobs;
	}

	@Override
	public Vector<Job> schedule(int t, int numberOfProcs, Vector<Job> activeJobs, Vector<Double> taskUtil,
			Vector<Double> taskDen, Vector<Task> taskset) {
		// TODO Auto-generated method stub
		return null;
	}
}
