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
 *SIR数据集新的有可能两个测试套件，也可能一个；而Siemens套件固定只有一个，
 *       本来NewGcovCoverage可以直接继承自GcovCoverage，考虑到需要测试，虽然不好，但还是重新做一个NewGcovCoverage，
 *     NewGcovCoverage的代码主要拷贝自GcovCoverage，这样，不要修改GcovCoverage。 
 */
public class NewGcovCoverage {
	private class CovInfo
	{
		private int lineno; //行号
		private int runtimes;   //覆盖次数。
		
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
	private int tc2Index; //fault-matrix.v0.tsl.universe里的测试用例数目，也是fault-matrix.v2.tsl.universe里测试用例
    //在tcMatrixs里索引位置的开始。tcNo-tc2Index等于fault-matrix.v2.tsl.universe里的测试用例数目。
	//注意，从fault-matrix读来，也就是直接从FaultMatrix引用过来，此类指示使用这些数据。
	List<int[]> tcMatrixs = new ArrayList<int[]>();  //所有版本的测试用例通过与否集合。
	String objectName; //object name
	
	public NewGcovCoverage(int ver,int tc,int tcindex,List<int[]> tcList,String object)
	{
		verNo = ver;
		tcNo = tc;
		tc2Index = tcindex;
		tcMatrixs = tcList;
		objectName = object;
	}
	
	
	/** 读入.gcov文件，并将处理结果存入.profile文件。
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
	
	/**readGcovGenProfile调用，处理单个版本。
	 * @param ver  版本号。 from 1 start.
	 * @return true 读文件成功；false:失败
	 */
	private boolean verReadGcovGenProfile(int ver)
	{
		boolean result = true;
		List<List<CovInfo>> covListAry = new ArrayList<List<CovInfo>>(); //按照版本顺序存放。
		
		for( int j=0;j<tcNo;j++ )
		{//读入该版本所有.gcov文件。
			List<CovInfo> covInfos = new ArrayList<CovInfo>();
			//版本号，测试用例号都从1开始。
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
	
	/**供verReadGcovGenProfile调用，读入某测试用例在某版本的覆盖数据
	 *          针对两个测试套件，每个版本有两种文件编排的.gcov
	 * @param ver 版本号
	 * @param tc  测试用例号
	 * @param covInfolst 覆盖数据：行号，执行次数。
	 * @return true 读文件成功；失败
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
		
		boolean noExecutedTimes = true; //所有可执行语句都未被覆盖，方便调试程序。
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
					 * Schedule2的V8的tc1514,tc1536,tc142等，gcov产生了不合理的数据，数值特别大，导致后面
					 * Integer.valueOf(runStr)抛出异常，临时这么处理一下。将来再考虑如何解决。
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
						{//应该只有这三种情况，否则，此处可能抛出错误。
							times = Integer.valueOf(runStr);
							noExecutedTimes = false;//至少一条语句被覆盖。
						}
					}
					if( times>=0 ) 
					{ //>=0 ，才是可执行语句。只收集执行语句的覆盖数据。
						String lineStr = parsed[1].trim(); //行号，若等于0，则前面字符串应该为"-"
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
	 * @param ver // 版本号,从1开始
	 * @param covListAry //存储了该版本所有语句在全部测试用例下的覆盖数据。
	 * @return
	 */
	private boolean writeProfileFile(int ver,List<List<CovInfo>> covListAry)
	{
		boolean result = true;
		List<SpectrumStruct> ssList = new ArrayList<SpectrumStruct>();
		int[] tcLines = tcMatrixs.get(ver-1);
		int passed =  SIRTools.getPassedTc(tcLines);
		int failed = SIRTools.getFailedTc(tcLines);
		int total = covListAry.get(0).size();//同一个版本，可执行语句相同，任意取一个计算即可。
		for( int k=0;k<total;k ++ )  //逐条语句统计程序谱。
		{
			int aep = 0, aef = 0;
			CovInfo cv = covListAry.get(0).get(k);//0可以是1，...tcNo-1中任意一个。
			int lineno = cv.getLineNo(); //所有测试用例对应的List，行号顺序都一致。
			for( int t=0;t<tcNo;t++ )
			{
				 cv = covListAry.get(t).get(k); //t对应测试用例，k对应可执行语句。
				 if( cv.getRunTimes()>0 ) //=0未覆盖；>0覆盖
				 {
					 if( 1==tcLines[t] )
						 aef++;  //1,未通过的测试用例
					 else
						 aep++; //通过的测试用例
				 }
			}
			SpectrumStruct ss = new SpectrumStruct(lineno,aep,aef);
			ssList.add(ss);
		}
		//保存到.profile文件。
		ProfileFile pf = new ProfileFile(objectName,ver,passed,failed,total,ssList);
		result = pf.writeProfileFile();		
		return result;
	}

}
