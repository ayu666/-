/**
 * 
 */
package sbflCommon;

import sbflTools.SBFLTools;

/**
 * @author Administrator
 *
 */
public class ExpenseScore implements IMetricMethod {
	private WorstBestSOS wbsResult; //存储对应算法、测度的结果。
	int[] faultStatms;  //某版本的故障语句行号集合。计算时需要用到。
	
	public ExpenseScore(int[] fss)
	{
		faultStatms = fss;
		wbsResult = new WorstBestSOS(); 
	}
	
	//接口的方法
	public IMetricResult getResult()
	{
		return wbsResult;
	}
	
	//接口的方法
	public String getMetricName()
	{
		return "Expense";
	}
	
	public void scoreCalculate(double[] pSuspicious,int[] pStatement, double maxFaultSuspi,int noMiniLine)
	{
		//calculate Best localization performance
		calBestStrategy(pSuspicious,maxFaultSuspi);
		//calculate  Worst  localization performance
		calWorstStrategy(pSuspicious,pStatement,maxFaultSuspi);
		//calculate  SOS  localization performance
		calSOSStrategy(pSuspicious,pStatement,maxFaultSuspi,noMiniLine);
	}
	
	/************************************************************************
    cal best strategy
	We assumed that programmer can find bug when they inspect any fault code which
	have same suppiousious.
	************************************************************************/
	private void calBestStrategy(double[] pSuspicious,double maxFaultSuspi)
	{
		int nIpspected = 0;//the inspected code line
		for ( double item :  pSuspicious)
		{
			if ( item > maxFaultSuspi )//this line'code must be inspected
				nIpspected++;
		}
		nIpspected++;//You must inspect the fault statement.
		//maxFaultSuspi is most suspiou,so > it ,will fault-free code.
		wbsResult.nBestIcl = nIpspected;
		wbsResult.fBestIcp = (float)nIpspected/pSuspicious.length;
	}

	/************************************************************************
	         cal Worst strategy
	We assumed that programmer can find bug when they locate locate any fault which
	have same suppiousious.
	************************************************************************/
	private void calWorstStrategy(double[] pSuspicious,int[] pStatement,double maxFaultSuspi)
	{
		int nIpspected = 0;//the inspected code line
		for ( int i=0;i<pSuspicious.length;i++ )
		{
			if( true==SBFLTools.isFaultCode(pStatement[i],faultStatms) )//this line'code  is  a fault
				continue; //may be two fault have same maxFaultSuspi
			if ( pSuspicious[i]>=maxFaultSuspi )//this line'code must be inspected
				nIpspected++;
		}
		nIpspected++;//You must inspect the fault statement.
		wbsResult.nWorstIcl = nIpspected;
		wbsResult.fWorstIcp = (float)nIpspected/pSuspicious.length;
	}
	
	//cal SOS strategy
	private void calSOSStrategy(double[] pSuspicious,int[] pStatement,double maxFaultSuspi,int nMinLineOfFault)
	{
		int nIpspected = 0;//the inspected code line
		int nCTies = 0;//the critical tie lines.
		int nLines = pSuspicious.length;
		for ( int i=0;i<nLines;i++ )
		{
			if ( pSuspicious[i]>maxFaultSuspi )//this line'code must be inspected
				nIpspected++;
			else if ( pSuspicious[i]==maxFaultSuspi )//ties or fault
			{
				if( true==SBFLTools.isFaultCode(pStatement[i],faultStatms) )//this line'code  is  a fault
					continue;
				nCTies++; //ties
				if( pStatement[i]<nMinLineOfFault ) //SOS strategy
					nIpspected++;
			}
			else //pSuspicious[i]<maxFaultSuspi
			{}
		}
		nIpspected++;//You must inspect the fault statement.
		wbsResult.nCTieCl = nCTies;
		wbsResult.nSOSIcl = nIpspected;
		wbsResult.fSOSIcp = (float)nIpspected/nLines;
	}

}
