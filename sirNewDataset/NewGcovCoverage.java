/**
 * 
 */
package sirNewDataset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import sbflCommon.SpectrumStruct;
import sirDataset.ProfileFile;
import sirDataset.ProjectConfiguration;
import sirDataset.SIRTools;
import sirDataset.SubjectDirectoryMap;

/**
 * @author Administrator
 *SIR���ݼ��µ��п������������׼���Ҳ����һ������Siemens�׼��̶�ֻ��һ����
 *       ����NewGcovCoverage����ֱ�Ӽ̳���GcovCoverage�����ǵ���Ҫ���ԣ���Ȼ���ã�������������һ��NewGcovCoverage��
 *     NewGcovCoverage�Ĵ�����Ҫ������GcovCoverage����������Ҫ�޸�GcovCoverage�� 
 */
public class NewGcovCoverage {
	private class CovInfo
	{
		private int lineno; //�к�
		private int runtimes;   //���Ǵ�����
		
		public CovInfo(int line,int run)
		{
			lineno = line;
			runtimes = run;
		}
		
		public int getLineNo()
		{
			return lineno;
		}
		public int getRunTimes()
		{
			return runtimes;
		}
	}//end of class CovInfo
	
	int verNo; //version  numbers
	int tcNo;  //test case number;
	private int tc2Index; //fault-matrix.v0.tsl.universe��Ĳ���������Ŀ��Ҳ��fault-matrix.v2.tsl.universe���������
    //��tcMatrixs������λ�õĿ�ʼ��tcNo-tc2Index����fault-matrix.v2.tsl.universe��Ĳ���������Ŀ��
	//ע�⣬��fault-matrix������Ҳ����ֱ�Ӵ�FaultMatrix���ù���������ָʾʹ����Щ���ݡ�
	List<int[]> tcMatrixs = new ArrayList<int[]>();  //���а汾�Ĳ�������ͨ����񼯺ϡ�
	String objectName; //object name
	
	public NewGcovCoverage(int ver,int tc,int tcindex,List<int[]> tcList,String object)
	{
		verNo = ver;
		tcNo = tc;
		tc2Index = tcindex;
		tcMatrixs = tcList;
		objectName = object;
	}
	
	
	/** ����.gcov�ļ�����������������.profile�ļ���
	 * @return
	 */
	public boolean readGcovGenProfile()
	{
		System.out.println("Read .gcov file and generate .profile file: ");
		boolean result = true;
		for( int k=0;k<verNo;k++  ) 
		{
			if( false==verReadGcovGenProfile(k+1) )
			{
				result = false;
				break;
			}
			System.out.println("      v"+String.valueOf(k+1)+" has been processed.");
		}
		return result;
	}
	
	/**readGcovGenProfile���ã��������汾��
	 * @param ver  �汾�š� from 1 start.
	 * @return true ���ļ��ɹ���false:ʧ��
	 */
	private boolean verReadGcovGenProfile(int ver)
	{
		boolean result = true;
		List<List<CovInfo>> covListAry = new ArrayList<List<CovInfo>>(); //���հ汾˳���š�
		
		for( int j=0;j<tcNo;j++ )
		{//����ð汾����.gcov�ļ���
			List<CovInfo> covInfos = new ArrayList<CovInfo>();
			//�汾�ţ����������Ŷ���1��ʼ��
			if( false==readTestcaseGcov(ver,j+1,covInfos) )
			{
				result = false;
				break;
			}
			else
				covListAry.add(covInfos);
		}
		if( true==result )
		{
			result = writeProfileFile(ver,covListAry);
		}
		return result;
	}
	
	/**��verReadGcovGenProfile���ã�����ĳ����������ĳ�汾�ĸ�������
	 *          ������������׼���ÿ���汾�������ļ����ŵ�.gcov
	 * @param ver �汾��
	 * @param tc  ����������
	 * @param covInfolst �������ݣ��кţ�ִ�д�����
	 * @return true ���ļ��ɹ���ʧ��
	 */
	private boolean readTestcaseGcov(int ver,int tc,List<CovInfo> covInfolst)
	{
		//if( ver!=5 || tc!=2534 )
		//	return true;
		boolean result = true;
		//flex & grep : t0_?.gcov and t2_?.gcov
		String gcovFile = ProjectConfiguration.DatasetDirectory +
				SubjectDirectoryMap.getDirectoryFromName(objectName) +"/traces/";
		if( (true==objectName.equalsIgnoreCase("flex")) ||
				(true==objectName.equalsIgnoreCase("grep")) )
		{
			if( tc>tc2Index )
				gcovFile = gcovFile+"v"+String.valueOf(ver)+"/"+"t2_"+String.valueOf(tc-tc2Index)+".gcov";
			else
				gcovFile = gcovFile+"v"+String.valueOf(ver)+"/"+"t0_"+String.valueOf(tc)+".gcov";
		}
		else if( (true==objectName.equalsIgnoreCase("gzip")) 
		      || (true==objectName.equalsIgnoreCase("sed")) )
			gcovFile = gcovFile+"v"+String.valueOf(ver)+"/"+"t"+String.valueOf(tc)+".gcov";
		else {}
		
		boolean noExecutedTimes = true; //���п�ִ����䶼δ�����ǣ�������Գ���
		try {
			File file = new File(gcovFile);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file));
				BufferedReader br = new BufferedReader(read);
				String lineTXT = null;
				while ((lineTXT = br.readLine()) != null) 
				{
					String[] parsed = lineTXT.split(":");
					String runStr = parsed[0].trim();
					int times = -1;
					/*
					 * Schedule2��V8��tc1514,tc1536,tc142�ȣ�gcov�����˲���������ݣ���ֵ�ر�󣬵��º���
					 * Integer.valueOf(runStr)�׳��쳣����ʱ��ô����һ�¡������ٿ�����ν����
					 */
					if( runStr.length()>=7 )
					{
						times = 999999;
						System.out.println("   v"+String.valueOf(ver)+",tc"+String.valueOf(tc)+"  have: ");
						System.out.println("       "+lineTXT);
					}
					else
					{
						if( true==runStr.equals("#####") )
							times = 0;
						else if( true==runStr.equals("-") )
							times = -1; 
						else
						{//Ӧ��ֻ����������������򣬴˴������׳�����
							times = Integer.valueOf(runStr);
							noExecutedTimes = false;//����һ����䱻���ǡ�
						}
					}
					if( times>=0 ) 
					{ //>=0 �����ǿ�ִ����䡣ֻ�ռ�ִ�����ĸ������ݡ�
						String lineStr = parsed[1].trim(); //�кţ�������0����ǰ���ַ���Ӧ��Ϊ"-"
						int line = Integer.valueOf(lineStr);
						CovInfo ci = new CovInfo(line,times);
						covInfolst.add(ci);
					}
				}
				read.close();
			}
			else
				result = false;
		}//end of try. 
		catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		if( true==noExecutedTimes )
			System.out.println("Check file "+gcovFile);
		return result;
	}
	
	/**
	 * @param ver // �汾��,��1��ʼ
	 * @param covListAry //�洢�˸ð汾���������ȫ�����������µĸ������ݡ�
	 * @return
	 */
	private boolean writeProfileFile(int ver,List<List<CovInfo>> covListAry)
	{
		boolean result = true;
		List<SpectrumStruct> ssList = new ArrayList<SpectrumStruct>();
		int[] tcLines = tcMatrixs.get(ver-1);
		int passed =  SIRTools.getPassedTc(tcLines);
		int failed = SIRTools.getFailedTc(tcLines);
		int total = covListAry.get(0).size();//ͬһ���汾����ִ�������ͬ������ȡһ�����㼴�ɡ�
		for( int k=0;k<total;k ++ )  //�������ͳ�Ƴ����ס�
		{
			int aep = 0, aef = 0;
			CovInfo cv = covListAry.get(0).get(k);//0������1��...tcNo-1������һ����
			int lineno = cv.getLineNo(); //���в���������Ӧ��List���к�˳��һ�¡�
			for( int t=0;t<tcNo;t++ )
			{
				 cv = covListAry.get(t).get(k); //t��Ӧ����������k��Ӧ��ִ����䡣
				 if( cv.getRunTimes()>0 ) //=0δ���ǣ�>0����
				 {
					 if( 1==tcLines[t] )
						 aef++;  //1,δͨ���Ĳ�������
					 else
						 aep++; //ͨ���Ĳ�������
				 }
			}
			SpectrumStruct ss = new SpectrumStruct(lineno,aep,aef);
			ssList.add(ss);
		}
		//���浽.profile�ļ���
		ProfileFile pf = new ProfileFile(objectName,ver,passed,failed,total,ssList);
		result = pf.writeProfileFile();		
		return result;
	}

}
