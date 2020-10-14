/**
 *  读入SIR提供的文件info\fault-matrix
 */
package sirDataset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import sbflCommon.TestcaseFile;

/**
 * @author Administrator
 *
 */
public class FaultMatrix {
	int verNo; //version  numbers
	int tcNo;  //test case number;
	/*
	 * 一定要注意：tcMatrixs第一个元素是第一个版本V1的测试用例通过与否集合，第二个亦如此，依次类推 。
	 * List<int[]>里面的 int[]也必须是按照顺序存放：第一个测试用例结果，第二个，...。
	 */
	List<int[]> tcMatrixs = new ArrayList<int[]>();  //所有版本的测试用例通过与否集合。
	String matrixFilename; //fault-matrix
	String objectName; //dataset object name
	/*
	 * objectName:对象名字。
	 */
	public FaultMatrix(String strObject)
	{
		objectName = strObject;
		matrixFilename = ProjectConfiguration.DatasetDirectory +
				SubjectDirectoryMap.getDirectoryFromName(objectName) +"/info/fault-matrix";
	}
	
	public boolean writeTestCaseFile()
	{
		boolean result = true;
		List<int[]> tcs = new ArrayList<int[]>(); 
		for( int i=0;i<verNo;i++ )
		{
			int[] tcLines = tcMatrixs.get(i);
			int[] pfAry = new int[2];
			pfAry[0] = SIRTools.getPassedTc(tcLines);
			pfAry[1] = SIRTools.getFailedTc(tcLines);
			tcs.add(pfAry);
		}
		TestcaseFile tcf = new TestcaseFile(objectName,verNo,tcNo,tcs);
		result =  tcf.writeTestcaseFile();
		return result;
	}
	/* 读入所有版本，所有测试用例测试结果通过与否的结果。
	 * @return true: read file ok.
	 */
	public boolean readTcMatrixFile()
	{
		boolean result = true;
		try {
			File file = new File(matrixFilename);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file));
				BufferedReader br = new BufferedReader(read);
				String lineTXT = null;
				lineTXT = br.readLine(); //Version total
				lineTXT = lineTXT.trim(); //去掉首尾空格
				String[]  strAry = lineTXT.split("\\s+"); //允许多个空格分割字符串
				verNo = Integer.valueOf(strAry[0]); //版本数
				
				lineTXT = br.readLine();  //test case total
				lineTXT = lineTXT.trim(); //去掉首尾空格
				strAry = lineTXT.split("\\s+");
				tcNo = Integer.valueOf(strAry[0]); //测试用例数目
				
				for( int i=0;i<tcNo;i++ ) //跳过 nTcs 行，是测试用例的输入
					br.readLine(); 
				
				//初始化存放测试用例结果的matrix,-1表示没有结果。
				for( int p=0;p<verNo;p++ )
				{
					int[] tcLines = new int[tcNo];//一个版本的所有测试结果。
					for( int q=0;q<tcNo;q++  )
						tcLines[q] = -1;
					tcMatrixs.add(tcLines);
				}
				//逐行读入
				for( int i=0;i<tcNo;i++ )
				{
					lineTXT = br.readLine();  //unitestXXX,from 0 start
					lineTXT = lineTXT.substring(7, lineTXT.length()-1);//"unitest"=7,最后一个是":");
					int tcno = Integer.valueOf(lineTXT);
					for( int j=0;j<verNo;j++ )
					{
						lineTXT = br.readLine();  //vXXX,from 1 start
						lineTXT = lineTXT.substring(1, lineTXT.length()-1);//"v"=1,最后一个是":");
						int vno = Integer.valueOf(lineTXT);
						lineTXT = br.readLine();  //测试结果,0 or 1
						lineTXT = lineTXT.trim(); //去掉首尾空格
						int tcoutcome = Integer.valueOf(lineTXT);
						tcMatrixs.get(vno-1)[tcno] = tcoutcome;
					}
				}//end of for( int i
				read.close();
			}
			else
				result = false;
		}//end of try. 
		catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	
	public int getVerNo()
	{
		return this.verNo;
	}
	
	public int getTesecaseNo()
	{
		return this.tcNo;
	}
	
	public List<int[]> getTestcaseMatrixs()
	{
		return this.tcMatrixs;
	}
	
	//计算某版本测试用例通过数目
	//ver from 1 start 
	public int getPassedTestcase(int ver)
	{
		int[] tcLines = tcMatrixs.get(ver-1);
		return SIRTools.getPassedTc(tcLines);
	}
	
	//计算某版本测试用例失败数目
	//ver from 1 start 
	public int getFailedTestcase(int ver)
	{
		int[] tcLines = tcMatrixs.get(ver-1);
		return SIRTools.getFailedTc(tcLines);
	}
	
	//我的输出结果和SIR提供不同，比较fault-matrix和fault-matrix.sir
	public void compareFaultMatrixSIR()
	{
		if( false==readTcMatrixFile() )
			return;
		List<int[]>  myMatrixs= tcMatrixs; //此时fault-matrix存放我的比较结果。
		tcMatrixs = new ArrayList<int[]>();  //存放SIR提供的比较结果。
		matrixFilename = matrixFilename+".sir";
		if( false==readTcMatrixFile() ) //读文件fault-matrix.sir
		{
			System.out.println("Read file is error:  "+matrixFilename);
			return;
		}
		
		int[] diffs = new int[verNo]; //记录每个版本的差异个数。
		boolean identify = true;
		for( int p=0;p<verNo;p++ )
		{
			int[] myTc = myMatrixs.get(p);
			int[] sirTc = tcMatrixs.get(p);
			int totalDiff = 0; //该版本我的比较结果与SIR结果不同的个数。
			for( int q=0;q<tcNo;q++ )
			{
				if( myTc[q]!=sirTc[q] )
				{
					identify = false;
					System.out.print("For test case "+String.valueOf(q+1)+":");
					System.out.print("  v"+String.valueOf(p+1));
					System.out.print(",  my="+myTc[q]);
					System.out.print(",SIR="+sirTc[q]);
					System.out.println(".");
					totalDiff++;
				}
			}
			diffs[p] = totalDiff;
		}
		if( true==identify )
			System.out.println("same.");
		//解决太多不同，无法看显示结果的问题。
		for( int k=0;k<verNo;k++ )
		{
			if( diffs[k]>0 )
				System.out.println("Version  "+String.valueOf(k+1)+"  have "+String.valueOf(diffs[k])+" differents");
		}
	}
	
	/**
	 * 测试代码
	 */
	public void testMe()
	{
		int verno = getVerNo();
		System.out.println("verno := "+verno);
		int tcno = getTesecaseNo();
		System.out.println("test case no := "+tcno);
		
		System.out.println("Version, Test case  passed or failed: ");
		for( int k=0;k<verno;k++ )
		{
			System.out.print("V");
			System.out.print(k+1);
			System.out.print(":  passed= ");
			System.out.print(getPassedTestcase(k+1));
			System.out.print(",  failed= ");
			System.out.print(getFailedTestcase(k+1));
			System.out.println(" ");
		}
	} //end of testMe.
}
