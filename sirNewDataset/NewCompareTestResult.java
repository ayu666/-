/**
 * 
 */
package sirNewDataset;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import sbflTools.SBFLTools;
import sirDataset.ProjectConfiguration;
import sirDataset.SIRTools;
import sirDataset.SubjectDirectoryMap;

/**
 * @author Administrator
 *SIR数据集新的格式和Siemens套件旧的格式大不相同，
 *       本来NewCompareTestResult可以直接继承自CompareTestResult，考虑到需要重新测试，不如重新做一个NewCompareTestResult，
 *     NewFaultMatrix的代码主要拷贝自CompareTestResult，这样，不要修改CompareTestResult。 
 */
/**
 * @author Administrator
 *
 */
public class NewCompareTestResult {
	int verNo; //version  numbers 
	int tcNo;  //test case number;
	private int tc2Index; //fault-matrix.v0.tsl.universe里的测试用例数目，也是fault-matrix.v2.tsl.universe里测试用例
    //在tcMatrixs里索引位置的开始。tcNo-tc2Index等于fault-matrix.v2.tsl.universe里的测试用例数目。
	//如果第二个fault-matrix有完整的测试记录，则此时的tc2Index = tcNo
	/*
	 * 一定要注意：tcMatrixs第一个元素是第一个版本V1的测试用例通过与否集合，第二个亦如此，依次类推 。
	 * List<int[]>里面的 int[]也必须是按照顺序存放：第一个测试用例结果，第二个，...。
	 * * 某些数据集，如flex,...等，有两个测试用例文件产生的测试结果：
	 *    fault-matrix.v0.tsl.universe 和 fault-matrix.v2.tsl.universe
	 */
	List<int[]> tcMatrixs = new ArrayList<int[]>();  //所有版本的测试用例通过与否集合。
	String objectName; //object name
	
	public NewCompareTestResult(int ver,int tc,String object,int tc2)
	{
		verNo = ver;
		tcNo = tc;
		objectName = object;
		tc2Index = tc2;
	}
	
	/** 关于FLEX数据集的测试结果比较。 flex需要比较两个文件。只要有一个不同，则认为测试未通过。
	 * @param tcnum 测试用例号 from 1 start
	 * @param vernum 测试用例号 from 1 start
	 * @return 比较结果 1，两个结果不同，测试失败； 0 ，两个文件输出结果相同，测试用例通过
	 *         -1，读文件出错
	 */
	private int flexCompare(int vernum, int tcnum)
	{
		int nok = -1;
		String dirFault = ProjectConfiguration.DatasetDirectory +
				SubjectDirectoryMap.getDirectoryFromName(objectName) +"/outputs/v" + String.valueOf(vernum);
		String dirOk = ProjectConfiguration.DatasetDirectory +
				SubjectDirectoryMap.getDirectoryFromName(objectName) +"/outputs/vok";
		String faultFile1 = dirFault +"/err0."+String.valueOf(tcnum);
		String okFile1 = dirOk +"/err0."+String.valueOf(tcnum);
		String faultFile2 = dirFault +"/out0."+String.valueOf(tcnum);
		String okFile2 = dirOk + "/out0."+String.valueOf(tcnum);
		if( tcnum>tc2Index )
		{ //第二个测试套件。
			faultFile1 = dirFault+"/err3.n"+String.valueOf(tcnum-tc2Index);
			okFile1 = dirOk + "/err3.n"+String.valueOf(tcnum-tc2Index);
			faultFile2 = dirFault +"/out2.n"+String.valueOf(tcnum-tc2Index);
			okFile2 = dirOk +"/out2.n"+String.valueOf(tcnum-tc2Index);
		}
		nok = SBFLTools.newCompareFile(faultFile1,okFile1);
		if( nok<0 )
			return nok; //读文件失败
		if( nok==0 ) //两个文件相同，才需要继续比较。
			nok = SBFLTools.newCompareFile(faultFile2,okFile2);
		//nok = SBFLTools.compareFile(faultFile2,okFile2);
		return nok;
	}
	
	/** 关于grep数据集的测试结果比较。 grep比较一个文件。
	 * @param tcnum 测试用例号 from 1 start
	 * @param vernum 测试用例号 from 1 start
	 * @return 比较结果 1，两个结果不同，测试失败； 0 ，两个文件输出结果相同，测试用例通过
	 *         -1，读文件出错
	 */
	private int grepCompare(int vernum, int tcnum)
	{
		int nok = -1;
		String dirFault = ProjectConfiguration.DatasetDirectory +
				SubjectDirectoryMap.getDirectoryFromName(objectName) +"/outputs/v" + String.valueOf(vernum);
		String dirOk = ProjectConfiguration.DatasetDirectory +
				SubjectDirectoryMap.getDirectoryFromName(objectName) +"/outputs/vok";
		String faultFile = dirFault +"/t0_"+String.valueOf(tcnum);
		String okFile = dirOk +"/t0_"+String.valueOf(tcnum);
		if( tcnum>tc2Index )
		{ //第二个测试套件。
			faultFile = dirFault+"/t2_"+String.valueOf(tcnum-tc2Index);
			okFile = dirOk + "/t2_"+String.valueOf(tcnum-tc2Index);
		}
		nok = SBFLTools.compareFile(faultFile,okFile); //not newCompareFile
		return nok;
	}
	
	
	/** 关于gzip数据集的测试结果比较。 gzip比较一个文件。
	 * @param tcnum 测试用例号 from 1 start
	 * @param vernum 测试用例号 from 1 start
	 * @return 比较结果 1，两个结果不同，测试失败； 0 ，两个文件输出结果相同，测试用例通过
	 *         -1，读文件出错
	 */
	private int gzipCompare(int vernum, int tcnum)
	{
		int nok = -1;
		String dirFault = ProjectConfiguration.DatasetDirectory +
				SubjectDirectoryMap.getDirectoryFromName(objectName) +"/outputs/v" + String.valueOf(vernum);
		String dirOk = ProjectConfiguration.DatasetDirectory +
				SubjectDirectoryMap.getDirectoryFromName(objectName) +"/outputs/vok";
		String faultFile = dirFault +"/t"+String.valueOf(tcnum);
		String okFile = dirOk +"/t"+String.valueOf(tcnum);
		nok = SBFLTools.compareFile(faultFile,okFile); //not newCompareFile
		return nok;
	}
	
	/** 关于SED数据集的测试结果比较。 sed需要比较两个文件。只要有一个不同，则认为测试未通过。
	 * @param tcnum 测试用例号 from 1 start
	 * @param vernum 测试用例号 from 1 start
	 * @return 比较结果 1，两个结果不同，测试失败； 0 ，两个文件输出结果相同，测试用例通过
	 *         -1，读文件出错
	 */
	private int sedCompare(int vernum, int tcnum)
	{
		int nok = -1;
		String dirFault = ProjectConfiguration.DatasetDirectory +
				SubjectDirectoryMap.getDirectoryFromName(objectName) +"/outputs/v" + String.valueOf(vernum);
		String dirOk = ProjectConfiguration.DatasetDirectory +
				SubjectDirectoryMap.getDirectoryFromName(objectName) +"/outputs/vok";
		String faultFile1 = dirFault +"/t"+String.valueOf(tcnum);
		String okFile1 = dirOk +"/t"+String.valueOf(tcnum);
		String faultFile2 = dirFault +"/s"+String.valueOf(tcnum)+".wout";
		String okFile2 = dirOk + "/s"+String.valueOf(tcnum)+".wout";
		//t??两个文件夹都有，每个测试用例都有；但是s??.wout，有些测试用例无，并且可能只存在于一个文件夹。
		nok = SBFLTools.newCompareFile(faultFile2,okFile2);
		if( nok<0 )
			return nok; //读文件失败
		if( nok==0 ) //两个文件相同，才需要继续比较。
			nok = SBFLTools.newCompareFile(faultFile1,okFile1);
		return nok;
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
				int nok=-1;
				if( true==objectName.equalsIgnoreCase("flex") )
					nok = flexCompare(i+1,j+1);
				else if( true==objectName.equalsIgnoreCase("grep") )
					nok = grepCompare(i+1,j+1);
				else if( true==objectName.equalsIgnoreCase("gzip") )
					nok = gzipCompare(i+1,j+1);
				else if( true==objectName.equalsIgnoreCase("sed") )
					nok = sedCompare(i+1,j+1);
				else {
					System.out.println("error, it is not found."+objectName);
				}
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
	
	//my result is maybe not same to SIR fault-matrix
	public void writeMyFaultMatrix()
	{
		String matrixFilename = ProjectConfiguration.DatasetDirectory +
				SubjectDirectoryMap.getDirectoryFromName(objectName) +"/info/fault-matrix";
		
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
	        //特别注意：不想硬编码，将tc2Index写入文件fault-matrix，Siemens套件没有该数据。
	        ops.write("   "+String.valueOf(tc2Index)+"  inV0.tsl.universe\r\n");
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
	
}
