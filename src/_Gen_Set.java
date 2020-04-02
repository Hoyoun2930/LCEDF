import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Vector;


public class _Gen_Set {

	public static final int period = 1000;
	public static final int max_num_task = 1000;
	public static final int isSporadic = 0;
	public static final double error = 0.0000001;
	public static final int proc[]={1,2,4,6,8};
	public static final String inputFile="./taskset2/";
	public static void main(String[] args) {
		int type=1; // 0 D=T,  1 D<=T,   2 D<>T (D<=2T),   3 D<>T (D<=4T)
		run(10,80,100,0);
		//run(10,80,100,0);
	}
	public static void run (int period, int seedFrom, int seedTo, int type) {
		for (int seed=seedFrom; seed<=seedTo; seed++) {
			Random ran = new Random(seed);
			for (int logm=0; logm<=2; logm++) {
				int m=proc[logm];
				for (int dist=0; dist<10; dist++) {
					try{
						String inputFileName = inputFile+period+"/"+type+"/"+period+"_"+m+"_"+ dist+"_"+type+"_"+seed+".in";
						File file = new File(inputFileName);
						file.getParentFile().mkdirs();
						PrintWriter inputPw = new PrintWriter(new FileWriter(file));
						inputPw.close();
					} catch (IOException e) { 
						System.out.println("io error first");
					}
					try{
						String inputFileName = inputFile+period+"/"+type+"/"+period+"_"+m+"_"+ dist+"_"+type+"_"+seed+".in";
						PrintWriter inputPw = new PrintWriter(new FileWriter(inputFileName,true));
						System.out.println(inputFileName);
						double T[]=new double[max_num_task];
						double C[]=new double[max_num_task];
						double D[]=new double[max_num_task];
						int n=0;
						boolean isValid=false;
						for (int sim=0; sim<1000; sim++) {
							if (isValid) {
								double ranU=getRanU(dist, ran);
								T[n] = 1+(int)((ran.nextDouble())*(period-1)+1);
								C[n] = 1+(int)(T[n]*ranU);
								if (type==0) {
									D[n]=T[n];
								} else if (type==1) {
									D[n] = C[n]+(int)((T[n]-C[n]+1)*ran.nextDouble());
								} else if (type==2) {
									D[n] = C[n]+(int)((2*T[n]-C[n]+1)*ran.nextDouble());
								} else if (type==3) {
									D[n] = C[n]+(int)((4*T[n]-C[n]+1)*ran.nextDouble());
								}
								n++;

							} else {
								T=new double[max_num_task];
								C=new double[max_num_task];
								D=new double[max_num_task];
								for (int i=0; i<m+1; i++) {
									double ranU=getRanU(dist, ran);
									T[i] = 1+(int)((ran.nextDouble())*(period-1)+1);
									C[i] = 1+(int)(T[i]*ranU);
									D[i] = C[i]+(int)((T[i]-C[i]+1)*ran.nextDouble());
									if (type==0) {
										D[i]=T[i];
									} else if (type==1) {
										D[i] = C[i]+(int)((T[i]-C[i]+1)*ran.nextDouble());
									} else if (type==2) {
										D[i] = C[i]+(int)((2*T[i]-C[i]+1)*ran.nextDouble());
									} else if (type==3) {
										D[i] = C[i]+(int)((4*T[i]-C[i]+1)*ran.nextDouble());
									}
								}
								n=m+1;
							}
							if (validTaskSet(T,C,D,n,m)) {
								double density=0;
								double util=0;
								for (int i=0; i<n; i++) {
									density+=C[i]/Math.min(T[i],D[i]);
									util+=C[i]/T[i];
								}
								isValid=true;
								String output2 = m+" "+n+" "+((int)(util*100.0)/100.0)+" "+((int)(density*100.0)/100.0)+" ";
								for (int i=0; i<n; i++) {
									output2+=T[i]+" "+C[i]+ " "+D[i]+ " ";


								}
								inputPw.println(output2);
							} else {
								sim--;
								isValid=false;
							}
						}

						inputPw.close();
					} catch (IOException e) { 
						System.out.println("io error");
					}
				}
			}
		}
	}

	public static double getRanU(int dist, Random ran) {
		double ranU=0;
		if (dist<5) {
			double ratio = dist*0.2+0.1;
			ranU= (ran.nextDouble() <= ratio) ? ran.nextDouble()*0.5+0.5 : ran.nextDouble()*0.5;
		} else {
			ranU=2;
			while (ranU>1) {
				double lambda = 1.0/((dist-5)*0.2+0.1);
				ranU = -Math.log(1-ran.nextDouble())/lambda;

			}
		}
		//System.out.println(ranU);
		return ranU;
	}

	public static boolean validTaskSet(double T[], double C[], double D[], int n, int m) {
		double density=0;
		double util=0;
		for (int i=0; i<n; i++) {
			density+=C[i]/Math.min(T[i],D[i]);
			util+=C[i]/T[i];
		}
		int t= 0;
		Vector<Double> periods = new Vector<Double>();
		for (int j=0; j<n; j++) {
			periods.add(T[j]);
		}
		double lcm = LCM.generateLCM(periods);
		if (util > m) return false; 
		else if (density > m) { 
			for (t=0; t<=lcm; t++) {
				double curdbf=0;
				for (int j=0; j<n; j++) {
					double a = ffdbf(t,(int)(T[j]+error),(int)(C[j]+error),(int)(D[j]+error));
					curdbf += a;
				}
				if (curdbf > (double)t*(double)m) {
					return false;
				}
			}
		}
		return true;
	}

	public static int ffdbf(int t, int T, int C, int D) {
		int dbfvalue = (t+T-D)/T;
		dbfvalue *= C;
		int r = t % T;
		if (r>D-C && r<D) dbfvalue+=(C-D+r);

		return dbfvalue;
	}

	/*
	public static int dbf(int t, int T, int C, int D) {
		int dbfvalue = (t+T-D)/T;
		dbfvalue *= C;
		return dbfvalue;
	}
	 */


}
