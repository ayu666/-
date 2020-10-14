/**
 * 
 */
package sbflCommon;

import java.util.ArrayList;
import java.util.List;

import sbflTools.SBFLTools;

/**
 * @author Administrator
 * SBFL算法性能的指标。
 */
public class SBFLperformance {
	private int thVer;  //第几个版本，注意不是版本个数verNo。
	private int tcPassed; //测试用例通过数
	private int tcFailed; //测试用例未通过数
	private int execTotal; //该版本代码，可执行语句条数
	private List<SpectrumStruct> spectrumList; //该版本的程序谱
	
	int[] faultStatms;  //该版本的故障语句行号集合。
	
	//fls里元素个数就是版本总个数。
	public SBFLperformance(int v,int pass,int fail,int total,List<SpectrumStruct> ssl,int[] fls)
	{
		thVer = v;
		tcPassed = pass;
		tcFailed = fail;
		execTotal = total;
		spectrumList = ssl;
		faultStatms = fls;
	}
	
	//strAlgorithm : SBFL 传统算法
	//strMetric : expense,exam,T-score,P-score,...
	//返回结果。IMetricResult
	public IMetricResult calPerformance(String strAlgorithm,String strMetric)
	{
		double[] pSuspicious = new double[execTotal]; //suspiciousness of each statement
		int[] pStatement = new int[execTotal]; //the pointer of the number of statement
		IMetricResult mResult = null;
		//calculate the suspiciousness of each code line.
		calculateSuspicious(strAlgorithm,pSuspicious);
		for ( int i=0;i<execTotal;i++ )
		{ //get code line no.
			pStatement[i] = spectrumList.get(i).getLineNo();
		}
		//find the most suspiciousness,return Max(Suspicious) and his code line no.
		double[] maxFaultSuspi = {0.0}; //max suspiciousness in fault code line.
		int noMiniLine = 0; //the mini number of Fault statement which suspiciousness is max.
		noMiniLine = getMaxSuspiFaultLine(pSuspicious,pStatement,maxFaultSuspi);
		if( strMetric.equalsIgnoreCase("Expense") )
		{
			ExpenseScore esMetric = new ExpenseScore(faultStatms);
			esMetric.scoreCalculate(pSuspicious,pStatement,maxFaultSuspi[0],noMiniLine);
			mResult = esMetric.getResult();
		}
		else
		{
			
		}
		return mResult;
	}
	
	//Calculate Suspicious 
	private void calculateSuspicious(String strAlgorithm, double[] pSuspi)
	{
		int nLines = execTotal; //=pSuspi.length
		for ( int i=0;i<nLines;i++ )
		{
			SpectrumStruct spect = spectrumList.get(i);
			ProfileStatement stProfile = new ProfileStatement();
			stProfile.no = spect.getLineNo(); //多余。
			stProfile.Aep = spect.getAep();
			stProfile.Aef = spect.getAef();
			stProfile.Anp = tcPassed-stProfile.Aep;
			stProfile.Anf = tcFailed-stProfile.Aef;
			pSuspi[i] = SBFLTradTechnique.algorithmSuspicious(strAlgorithm, stProfile);
		}
	}
	
	
	/** 故障语句可能有多条，找出他们之中可疑度最大值。
	 * find the most suspiciousness,return Max(Suspicious) and his code line no.
	 * if two fault have same Suspicious,then return the mini line no.
	 * @param pSuspicious 各语句依照顺序，可疑度
	 * @param pStatement  语句号，与pSuspicious顺序相同。
	 * @param maxFaultSuspi 最大可疑度值
	 * @return 故障语句里具有最大可疑度值，行号最小的那个。
	 */
	private int getMaxSuspiFaultLine(double[] pSuspicious,int[] pStatement,double[] maxFaultSuspi)
	{
		//get the most suspiciousness
		double maxSuspi = -9999;
		int lineno = 0;
		for ( int i=0;i<pSuspicious.length;i++ )
		{
			if( false==SBFLTools.isFaultCode(pStatement[i],faultStatms) )//this line'code  is not a fault
				continue;
			if ( pSuspicious[i]>maxSuspi )
			{
				maxSuspi = pSuspicious[i];
				lineno = pStatement[i]; //pStatement按照从小到大的顺序排列，所以最先的一定行号最小
			}
		}

		maxFaultSuspi[0] = maxSuspi;
		return lineno;
	}
	
}
