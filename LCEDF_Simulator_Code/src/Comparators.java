import java.util.*;

public class Comparators {

	public static Comparator<Job> EDF_NComparator(final int t) {
		return new Comparator<Job>() {

			public int compare(Job j1, Job j2) {
				
				int index1 = j1.getTaskIndex();
				int index2 = j2.getTaskIndex();
				double deadline1 = j1.getDeadline() - t;
				double deadline2 = j2.getDeadline() - t;
				
				boolean indexPriority = index1 < index2 ? true : false;

				//boolean sMaxPriority = sMax1 < sMax2 ? true : false;
				
				if (j1.getIsScheduled() && j2.getIsScheduled())
					return index1 < index2 ? -1 : 1;
				else if (j1.getIsScheduled())
					return -1;
				else if (j2.getIsScheduled())
					return 1;
				
			
				
				boolean deadlinePriority = deadline1 < deadline2 ? true
						: (deadline1 > deadline2 ? false : indexPriority);
				
				return (deadlinePriority ? -1 : 1);
			}
		};
	}
	public static Comparator<Job> SMAXComparator(final int t) {
		return new Comparator<Job>() {

			public int compare(Job j1, Job j2) {
				double smax1 = j1.sMax, smax2 = j2.sMax;
				double ftime1 = j1.sMin + j1.getexecTime(), ftime2 = j2.sMin + j2.getexecTime();
				
				return smax1 < smax2 ? -1 : (smax1 > smax2 ? 1 : (ftime1 < ftime2 ? -1 : (ftime1 > ftime2 ? 1 : 0)));
			}
		};
	}
}
