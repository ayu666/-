/**
 * 
 */
package performanceEvaluate;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import sbflCommon.FaultFile;
import sbflCommon.IMetricResult;
import sbflCommon.SBFLTradTechnique;
import sbflCommon.SBFLperformance;
import sbflCommon.WorstBestSOS;
import sirDataset.ExcludeVersion;
import sirDataset.ProfileFile;

/**
 * @author Administrator
 *
 */
public class SIRObjectEvaluate {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//flexV4,flexV2,grepV3,grepV2,sedV3
		String objectName = "sedV7";//print_tokens  print_tokens2  schedule schedule2 replace tcas tot_info space
		// TODO Auto-generated method stub
		System.out.println("1, evaluate (one Metric ) SBFL performance of SIR dataset .");
    	System.out.println("2, evaluate (all Metric ) SIR dataset ..");
    	System.out.println("3, evaluate (one Metric ) from txt file.");
    	System.out.println("4, .");
    	System.out.println("Others, exit............ .");
    	System.out.println("Please key your choice.");
    	Scanner sc=new Scanner(System.in);
    	int choice = sc.nextInt();
    	sc.close();
    	switch( choice )
    	{
    	case 1:
    		evaluateSIRdataset(objectName);
    		break;
    	case 2:
    		allAlgorithmSIRdataset(objectName);
    		break;
    	case 3:
    		evaluateFromTxtProfile();//从.txt文件读程序谱，再计算算法性能。
    		break;
    	case 4:
    		break;
    	default:
    	}//end of switch
    	System.out.println("The task is over.");
	}//end of main
	
	//测试，一次只计算一个SBFL技术，一个对象，一种评估测度。
	private static void evaluateSIRdataset(String objectName)
	{
		String strMetric = "Expense";
		String strAlgorihtm = "Tarantula";//Jaccard,Tarantula,Ochiai,Opass
		FaultFile faultFile = new FaultFile(objectName);
		if( false==faultFile.readFaultFile() )
		{
			System.out.println("Read file "+objectName+".fault is error.");
			return;
		}
		int vernum = faultFile.getVerNo();
		List<WorstBestSOS> wbsLst = new ArrayList<WorstBestSOS>();
		for( int ver=1;ver<=vernum; ver++)
		{
			if( true==ExcludeVersion.isExcludeVer(objectName,ver) )
				continue; //该版本不参加计算。
			int[] faultStats = faultFile.getFaultLines().get(ver-1);
			ProfileFile pf = new ProfileFile(ver,objectName);//只能逐个版本测试。
			if( false==pf.readProfileFile() )
			{
				System.out.println("Read file "+objectName+"_"+String.valueOf(ver)+".profile is error.");
				return;
			}
			SBFLperformance sbflm = new SBFLperformance(pf.getVerTh(),pf.getPassed(),pf.getFailed(),
									pf.getTotalExec(),pf.getSpectrumList(),faultStats);
			IMetricResult imr = sbflm.calPerformance(strAlgorihtm,strMetric);
			System.out.println("Version :"+ver);
			imr.showMe();
			wbsLst.add((WorstBestSOS)imr);
		}
		WorstBestSOS wbsTmp = WorstBestSOS.average(wbsLst);
		System.out.println("Average of "+strAlgorihtm+" by "+String.valueOf(wbsLst.size())+" vers:");
		wbsTmp.showMe();
		System.out.println("Object name is "+objectName);
	}//end of evaluateSIRdataset

	//测试，一次计算所有SBFL技术，一个对象，一种评估测度。
	private static void allAlgorithmSIRdataset(String objectName)
	{
		String strMetric = "Expense";
		FaultFile faultFile = new FaultFile(objectName);
		if( false==faultFile.readFaultFile() )
		{
			System.out.println("Read file "+objectName+".fault is error.");
			return;
		}
		
		int vernum = faultFile.getVerNo();

		//逐个技术计算。
		String[] algorithms = SBFLTradTechnique.getAlgorithmNames();
		for( String method : algorithms )
		{
			List<WorstBestSOS> wbsLst = new ArrayList<WorstBestSOS>();
			for( int ver=1;ver<=vernum; ver++)
			{
				if( true==ExcludeVersion.isExcludeVer(objectName,ver) )
					continue; //该版本不参加计算。
				int[] faultStats = faultFile.getFaultLines().get(ver-1);
				ProfileFile pf = new ProfileFile(ver,objectName);//只能逐个版本测试。
				if( false==pf.readProfileFile() )
				{
					System.out.println("Read file "+objectName+"_"+String.valueOf(ver)+".profile is error.");
					return;
				}
				SBFLperformance sbflm = new SBFLperformance(pf.getVerTh(),pf.getPassed(),pf.getFailed(),
										pf.getTotalExec(),pf.getSpectrumList(),faultStats);
				IMetricResult imr = sbflm.calPerformance(method,strMetric);
				wbsLst.add((WorstBestSOS)imr);
			}
			WorstBestSOS wbsTmp = WorstBestSOS.average(wbsLst);
			System.out.println("Average of "+method+" by "+String.valueOf(wbsLst.size())+" vers:");
			wbsTmp.showMe();
			System.out.println("Object name is "+objectName);
		}//end of for...
	}//end of evaluateSIRdataset

	//从.txt文件读程序谱，再计算算法性能。
	//为测试编写此段代码
	private static void evaluateFromTxtProfile()
	{
		String objectName = "schedule";
		String strMetric = "Expense";
		String strAlgorihtm = "Tarantula";//Jaccard,Tarantula,Ochiai,Opass
		int ver = 1;
		
		FaultFile faultFile = new FaultFile(objectName);
		if( false==faultFile.readFaultFile() )
		{
			System.out.println("Read file "+objectName+".fault is error.");
			return;
		}
		
		int[] faultStats = faultFile.getFaultLines().get(ver-1);
		ProfileFile pf = new ProfileFile(ver,objectName);//只能逐个版本测试。
		if( false==pf.readTxtFile() )
		{
			System.out.println("Read file "+objectName+"_"+String.valueOf(ver)+".txt is error.");
			return;
		}
		SBFLperformance sbflm = new SBFLperformance(pf.getVerTh(),pf.getPassed(),pf.getFailed(),
								pf.getTotalExec(),pf.getSpectrumList(),faultStats);
		IMetricResult imr = sbflm.calPerformance(strAlgorihtm,strMetric);
		System.out.println("Version :"+ver);
		imr.showMe();
	}
}//end of class
