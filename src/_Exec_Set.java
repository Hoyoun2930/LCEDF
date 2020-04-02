import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class _Exec_Set {

	public static final int proc[] = { 1, 2, 4, 6,8 };
	public static final int max_num_task = 1000;
	public static final String outputFile = "./output/";
	public static final String inputFile = "./taskset/";
	// public static final String outputFile="./outputFiles";
	// public static final String inputFile="./inputFiles";

	public static final int startlogm = 0;
	public static final int endlogm = 7;

	public static void main(String[] args) {
		int type = 0;
		exec(10, 80 ,100, 0,2, type);
	}
	
	public static void exec(int period, int seedFrom, int seedTo, int logm_from, int logm_to, int type) {

		// long seed= System.currentTimeMillis();
		for (int seed = seedFrom; seed <= seedTo; seed++) {
			// for (int seed=0; seed<=0; seed++) {
			for (int logm = logm_from; logm <= logm_to; logm++) {
				int m = proc[logm];
				for (int dist = 0; dist < 10; dist++) {
					try {
						String inputFileName = inputFile + period + "/" + type + "/" + period + "_" + m + "_" + dist + "_" + type + "_" + seed + ".in";
						FileReader fr = new FileReader(inputFileName);
						BufferedReader br = new BufferedReader(fr);
						br.close();
						String outputFileName = outputFile + period + "/" + m + "/" + dist + "_" + type + "_" + seed + ".out";
						File file = new File(outputFileName);
						file.getParentFile().mkdirs();
						PrintWriter outputPw = new PrintWriter(new FileWriter(file));
						outputPw.close();
					} catch (IOException e) {
						System.out.println("io error1");
					}
					try {
						// String inputFileName =
						// inputFile+period+"/"+type+"/"+period+"_"+m+"_"+dist+"_"+type+"_"+"10009"+".in";
						String inputFileName = inputFile + period + "/" + type + "/" + period + "_" + m + "_" + dist
								+ "_" + type + "_" + seed + ".in";
						FileReader fr = new FileReader(inputFileName);
						BufferedReader br = new BufferedReader(fr);

						String outputFileName = outputFile + period + "/" + m + "/" + dist + "_" + type + "_" + seed + ".out";
						PrintWriter outputPw = new PrintWriter(new FileWriter(outputFileName));

						System.out.println(outputFileName);

						for (int sim = 0; sim < 1000; sim++) {

							String s = br.readLine();
							StringTokenizer st = new StringTokenizer(s);

							int mm = Integer.parseInt(st.nextToken());
							if (mm != m) {
								System.out.println("ERROR");
								// System.exit(0);
							}
							m = mm;
							int num_task = Integer.parseInt(st.nextToken());
							double util = Double.parseDouble(st.nextToken());
							double den = Double.parseDouble(st.nextToken());
							String SS[] = new String[num_task * 3 + 6];
							SS[0] = sim + "";
							SS[1] = period + "";
							SS[2] = 0 + "";
							SS[3] = m + "";
							SS[4] = num_task + "";
							SS[5] = 0 + "";
							for (int i = 0; i < num_task; i++) {
								SS[6 + 3 * i] = Double.parseDouble(st.nextToken()) + "";
								SS[7 + 3 * i] = Double.parseDouble(st.nextToken()) + "";
								SS[8 + 3 * i] = Double.parseDouble(st.nextToken()) + "";
							}
							if (seed >= 10000) {
								SS[5] = "" + (seed % 10);
							}
							outputPw.println(Simulator_CEDF.main2(SS));
						}
						outputPw.close();
						br.close();
					} catch (IOException e) {
						System.out.println("io error");
					}

				}
			}
			(Runtime.getRuntime()).gc();
		}
		System.out.println("DONE");
	}
}