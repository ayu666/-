/**
 * 
 */
package sbflCommon;

import java.util.ArrayList;
import java.util.List;

import sbflTools.SBFLTools;

/**
 * @author Administrator
 * SBFL�㷨���ܵ�ָ�ꡣ
 */
public class SBFLperformance {
	private int thVer;  //�ڼ����汾��ע�ⲻ�ǰ汾����verNo��
	private int tcPassed; //��������ͨ����
	private int tcFailed; //��������δͨ����
	private int execTotal; //�ð汾���룬��ִ���������
	private List<SpectrumStruct> spectrumList; //�ð汾�ĳ�����
	
	int[] faultStatms;  //�ð汾�Ĺ�������кż��ϡ�
	
	//fls��Ԫ�ظ������ǰ汾�ܸ�����
	public SBFLperformance(int v,int pass,int fail,int total,List<SpectrumStruct> ssl,int[] fls)
	{
		thVer = v;
		tcPassed = pass;
		tcFailed = fail;
		execTotal = total;
		spectrumList = ssl;
		faultStatms = fls;
	}
	
	//strAlgorithm : SBFL ��ͳ�㷨
	//strMetric : expense,exam,T-score,P-score,...
	//���ؽ����IMetricResult
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
			stProfile.no = spect.getLineNo(); //���ࡣ
			stProfile.Aep = spect.getAep();
			stProfile.Aef = spect.getAef();
			stProfile.Anp = tcPassed-stProfile.Aep;
			stProfile.Anf = tcFailed-stProfile.Aef;
			pSuspi[i] = SBFLTradTechnique.algorithmSuspicious(strAlgorithm, stProfile);
		}
	}
	
	
	/** �����������ж������ҳ�����֮�п��ɶ����ֵ��
	 * find the most suspiciousness,return Max(Suspicious) and his code line no.
	 * if two fault have same Suspicious,then return the mini line no.
	 * @param pSuspicious ���������˳�򣬿��ɶ�
	 * @param pStatement  ���ţ���pSuspicious˳����ͬ��
	 * @param maxFaultSuspi �����ɶ�ֵ
	 * @return �����������������ɶ�ֵ���к���С���Ǹ���
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
				lineno = pStatement[i]; //pStatement���մ�С�����˳�����У��������ȵ�һ���к���С
			}
		}

		maxFaultSuspi[0] = maxSuspi;
		return lineno;
	}
	
}
