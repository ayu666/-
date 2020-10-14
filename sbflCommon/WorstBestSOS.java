/**
 * 
 */
package sbflCommon;

import java.util.List;

/**
 * @author Administrator
 *
 */
public class WorstBestSOS implements IMetricResult{
	public int nBestIcl; //the inspected code lines of Best strategy.
	public float fBestIcp; //the percentage of inspected code lines of Best strategy.
	public int nWorstIcl; //the inspected code lines of Worst strategy.
	public float fWorstIcp; //the percentage of inspected code lines of Worst strategy.
	public int nCTieCl; //the Critical tie code lines.
	public int nSOSIcl; //the inspected code lines of SOS strategy.
	public float fSOSIcp; //the percentage of inspected code lines of SOS strategy.
	
	//性能结果的名字
	public String getName()
	{
		return "WorstBestSOS";
	}
	
	//显示结果。
	public void showMe()
	{
		System.out.print("inspected code lines(%) of Best strategy:  ");
		System.out.println("("+nBestIcl+","+fBestIcp*100+")");
		System.out.print("        Worst:  ");
		System.out.println("("+nWorstIcl+","+fWorstIcp*100+")");
		System.out.print("        SOS strategy:   ");
		System.out.println("("+nSOSIcl+","+fSOSIcp*100+")");
	}
	
	//求平均值。
	public static WorstBestSOS average(WorstBestSOS[] wbsAry)
	{
		final WorstBestSOS wbsTmp = new  WorstBestSOS();
		int bestIcls = 0;
		float bestIcp = 0.0f;
		int worstIcl  = 0;
		float worstIcp = 0.0f;
		int cTieCl = 0;
		int SOSIcl = 0;
		float SOSIcp = 0.0f;
		for( WorstBestSOS wbs : wbsAry)
		{
			bestIcls += wbs.nBestIcl;
			bestIcp += wbs.fBestIcp;
			worstIcl += wbs.nWorstIcl;
			worstIcp += wbs.fWorstIcp;
			cTieCl += wbs.nCTieCl;
			SOSIcl += wbs.nSOSIcl;
			SOSIcp += wbs.fSOSIcp;
		}
		int number = wbsAry.length;
		wbsTmp.nBestIcl = bestIcls/number;
		wbsTmp.fBestIcp = bestIcp/number;
		wbsTmp.nWorstIcl = worstIcl/number;
		wbsTmp.fWorstIcp = worstIcp/number;
		wbsTmp.nSOSIcl = SOSIcl/number;
		wbsTmp.nCTieCl = cTieCl/number;
		wbsTmp.fSOSIcp= SOSIcp/number;
		return wbsTmp;
	}
	
	//求平均值。
	public static WorstBestSOS average(List<WorstBestSOS> wbsList)
	{
		final WorstBestSOS wbsTmp = new  WorstBestSOS();
		int bestIcls = 0;
		float bestIcp = 0.0f;
		int worstIcl  = 0;
		float worstIcp = 0.0f;
		int cTieCl = 0;
		int SOSIcl = 0;
		float SOSIcp = 0.0f;
		for( WorstBestSOS wbs : wbsList)
		{
			bestIcls += wbs.nBestIcl;
			bestIcp += wbs.fBestIcp;
			worstIcl += wbs.nWorstIcl;
			worstIcp += wbs.fWorstIcp;
			cTieCl += wbs.nCTieCl;
			SOSIcl += wbs.nSOSIcl;
			SOSIcp += wbs.fSOSIcp;
		}
		int number = wbsList.size();
		wbsTmp.nBestIcl = bestIcls/number;
		wbsTmp.fBestIcp = bestIcp/number;
		wbsTmp.nWorstIcl = worstIcl/number;
		wbsTmp.fWorstIcp = worstIcp/number;
		wbsTmp.nSOSIcl = SOSIcl/number;
		wbsTmp.nCTieCl = cTieCl/number;
		wbsTmp.fSOSIcp= SOSIcp/number;
		return wbsTmp;
	}
}
