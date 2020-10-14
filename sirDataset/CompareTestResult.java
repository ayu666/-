/**
 * 比较v(xxx)和v(ok)之间的差别，输出结果相同，则认为测试用例通过；否则失败。
 */
package sirDataset;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import sbflTools.SBFLTools;

/**
 * @author Administrator
 *
 */
public class CompareTestResult {
	int verNo; //version  numbers 
	int tcNo;  //test case number;
	/*
	 * 一定要注意：tcMatrixs第一个元素是第一个版本V1的测试用例通过与否集合，第二个亦如此，依次类推 。
	 * List<int[]>里面的 int[]也必须是按照顺序存放：第一个测试用例结果，第二个，...。
	 */
	List<int[]> tcMatrixs = new ArrayList<int[]>();  //所有版本的测试用例通过与否集合。
	String objectName; //object name
	
	public CompareTestResult(int ver,int tc,String object)
	{
		verNo = ver;
		tcNo = tc;
		objectName = object;
	}
	
	//编写了Junit测试模块，调试问题，先这么做。
	//int前面加public,private都通不过编译。
	int jUnitCompareFile(String faultOutput,String okOutput)
	{
		return SBFLTools.compareFile(faultOutput, okOutput);
	}
	
	
	
	//读SIR对象程序结果输出文件，并比较文件内容。将比较结果存入tcMatrixs
	public boolean readFile()
	{
		//初始化存放测试用例结果的matrix,-1表示没有结果。
		for( int p=0;p<verNo;p++ )
		{
			int[] tcLines = new int[tcNo];//一个版本的所有测试结果。
			for( int q=0;q<tcNo;q++  )
				tcLines[q] = -1;
			tcMatrixs.add(tcLines);
		}
		
		//比较v(xxx)和v(ok)之间的差别，输出结果相同，则认为测试用例通过；否则失败。
		for( int i=0;i<verNo;i++ )
		{
			for( int j=0;j<tcNo;j++ )
			{
				String faultFile = ProjectConfiguration.DatasetDirectory +
					SubjectDirectoryMap.getDirectoryFromName(objectName) +"/outputs/v" +
					String.valueOf(i+1)+"/t"+String.valueOf(j+1);
				String okFile = ProjectConfiguration.DatasetDirectory +
						SubjectDirectoryMap.getDirectoryFromName(objectName) +"/outputs/vok" +
						"/t"+String.valueOf(j+1);
				int nok = SBFLTools.compareFile(faultFile,okFile);
				if( nok<0 )
					return false;
				tcMatrixs.get(i)[j] = nok;
			}
			System.out.println("      v"+String.valueOf(i+1)+" 's output has been read.");
		}//end of for(int i
		return true;
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
		int passed =SIRTools.getPassedTc(tcLines);
		return passed;
	}
	
	//计算某版本测试用例失败数目
	//ver from 1 start 
	public int getFailedTestcase(int ver)
	{
		int[] tcLines = tcMatrixs.get(ver-1);
		int failed = SIRTools.getFailedTc(tcLines);
		return failed;
	}
	
	//my result is not same to SIR fault-matrix
	//replace fault_matrix with fault-matrix.sir and generate fault-matrix by using my result.
	public void replaceSIRfaultMatrix()
	{
		String matrixFilename = ProjectConfiguration.DatasetDirectory +
				SubjectDirectoryMap.getDirectoryFromName(objectName) +"/info/fault-matrix";
		String matrixReplaced = matrixFilename+".sir";
		File fileReplaced = new File(matrixReplaced);
		if (fileReplaced.isFile() && fileReplaced.exists()) 
		{
			System.out.println("To continue? The work has been finished.");
			return;
		}
		File oldFile = new File(matrixFilename);
		oldFile.renameTo(fileReplaced);
		
		//write new content to file fault-matrix
		OutputStreamWriter ops = null;
	    BufferedWriter bw = null; 
	    File file;
	    try {
	        file = new File(matrixFilename);
	        //if( file.isFile()&& file.exists() )
	        	//file.delete();
	        file.createNewFile();
	        ops = new OutputStreamWriter(new FileOutputStream(file));
	        bw = new BufferedWriter(ops);
	        ops.write("      "+String.valueOf(verNo)+"  listversions\r\n");
	        ops.write("   "+String.valueOf(tcNo)+"  listtests\r\n");
	        for( int t=1;t<=tcNo;t++)
	        	ops.write("testcase  "+String.valueOf(t)+"\r\n");
	        //逐条信息写入。
	        for( int t=1;t<=tcNo;t++)
	        {	
	        	ops.write("unitest"+String.valueOf(t-1)+":\r\n");
		        for( int s=1;s<=verNo;s++ )
		        {
		        	ops.write("v"+String.valueOf(s)+":\r\n");
		        	ops.write("      "+String.valueOf(tcMatrixs.get(s-1)[t-1])+"\r\n");
		        }
	        }
	        bw.close();
        	ops.close();
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    } 
	}
	
	/**
	 * 测试代码
	 */
	public void testMe()
	{
		System.out.println("Version, Test case  passed or failed: ");
		for( int k=0;k<verNo;k++ )
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
	
	//检查SIR提供的fault-matrix与我的结果是否相同。
	public void checkPassFail(List<int[]> tcResult)
	{
		boolean identify = true;
		int[] diffs = new int[verNo]; //记录每个版本的差异个数。
		for( int p=0;p<verNo;p++ )
		{
			int[] myTc = tcMatrixs.get(p);
			int[] sirTc = tcResult.get(p);
			int totalDiff = 0; //该版本我的比较结果与SIR结果不同的个数。
			for( int q=0;q<tcNo;q++ )
			{
				if( myTc[q]!=sirTc[q] )
				{
					identify = false;
					System.out.print("For test case "+String.valueOf(q+1)+":");
					System.out.print("  v");
					System.out.print(p+1);
					System.out.print(",my="+myTc[q]);
					System.out.print(",SIR");
					System.out.print("="+sirTc[q]);
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
}//end of class.
