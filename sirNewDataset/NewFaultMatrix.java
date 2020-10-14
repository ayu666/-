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

import sbflCommon.TestcaseFile;
import sirDataset.ProjectConfiguration;
import sirDataset.SIRTools;
import sirDataset.SubjectDirectoryMap;

/**
 * @author Administrator
 * SIR数据集新的格式和Siemens套件旧的格式大不相同，
 *       本来NewFaultMatrix可以直接继承自FaultMatrix，考虑到需要重新测试，不如重新做一个NewFaultMatrix，
 *     NewFaultMatrix的代码主要拷贝自FaultMatrix，这样，不要修改FaultMatrix。 
 */
public class NewFaultMatrix {
	private int verNo; //version  numbers
	private int tcNo;  //test case number;
	private int tc2Index; //fault-matrix.v0.tsl.universe里的测试用例数目，也是fault-matrix.v2.tsl.universe里测试用例
	          //在tcMatrixs里索引位置的开始。tcNo-tc2Index等于fault-matrix.v2.tsl.universe里的测试用例数目。
	private boolean checkSecondMatrix; //如果第二个fault-matrix有完整的测试记录，则为true；否则false;
	/*
	 * 一定要注意：tcMatrixs第一个元素是第一个版本V1的测试用例通过与否集合，第二个亦如此，依次类推 。
	 * List<int[]>里面的 int[]也必须是按照顺序存放：第一个测试用例结果，第二个，...。
	 * 某些数据集，如flex,...等，有两个测试用例文件产生的测试结果：
	 *    fault-matrix.v0.tsl.universe 和 fault-matrix.v2.tsl.universe
	 */
	private List<int[]> tcMatrixs = new ArrayList<int[]>();  //所有版本的测试用例通过与否集合。
	private String matrixFilename; //fault-matrix
	private String objectName; //dataset object name
	
	//使用此类时，先用构造函数创建对象。
	public NewFaultMatrix(String strObject)
	{
		checkSecondMatrix = false;
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
	
	/**
	 * 读入新生成的fault-matrix（我通过比较文件）， 
	 *      数据存入当前对象。
	 *      
	 * @return true: read file ok.
	 */
	public boolean readFaultMatrix()
	{
		boolean result = true;
		int[] tcsRef = new int[1];
		int[] versRef = new int[1];
		//List<int[]> tcsFault = new ArrayList<int[]>();
		result = readTcMatrixFile(matrixFilename,versRef,tcsRef,tcMatrixs,true);
		verNo = versRef[0];
		tcNo = tcsRef[0];
		return result;
	}
	
	/** 一些SIR的新格式 object 带有两个fault-matrix，而且各对象的名字也不同。
	 * @return 第一个
	 */
	private String getFirstFaultMatrix()
	{
		String firstMatrixName="";
		if( true==objectName.equalsIgnoreCase("flex") )
			firstMatrixName = "fault-matrix.v0.tsl.universe";
		else if( true==objectName.equalsIgnoreCase("grep") )
			firstMatrixName = "fault-matrix.v0_1.tsl.universe";
		else if( true==objectName.equalsIgnoreCase("gzip") )
			firstMatrixName = "fault-matrix.v0.tsl.universe";
		else if( true==objectName.equalsIgnoreCase("sed") )
			firstMatrixName = "fault-matrix.tsl";
		else 
			System.out.println("Error: getFirstFaultMatrix");
		return firstMatrixName;
	}
	
	/** 一些SIR的新格式 object 带有两个fault-matrix，而且各对象的名字也不同。
	 * @return 第一个
	 */
	private String getSecondFaultMatrix()
	{
		String secondMatrixName="";
		if( true==objectName.equalsIgnoreCase("flex") )
			secondMatrixName = "fault-matrix.v2.tsl.universe";
		else if( true==objectName.equalsIgnoreCase("grep") )
			secondMatrixName = "fault-matrix.v0_2.tsl.universe";
		else 
			System.out.println("Error: getSecondFaultMatrix");
		return secondMatrixName;
	}
	
	/**将tcsFault合并到tcMatrixs
	 * @param tcsFault 第二次读入的fault-matrix文件
	 */
	private void mergeFaultMatrixList(List<int[]> tcsFault)
	{
		int v1 = tcMatrixs.size();
		int v2 = tcsFault.size();
		if( v1!=v2 )
		{ //两个数组的版本数一致，才能调用此方法。
			System.out.println("Error: mergeFaultMatrixList");
			return;
		}
		List<int[]> newTcMatrix = new ArrayList<int[]>();
		
		for( int i=0;i<v1;i++ )
		{
			int[] a1s = tcMatrixs.get(i);
			int[] a2s = tcsFault.get(i);
			int len1 = a1s.length;
			int len2 = a2s.length;
			int[] adds = new int[len1+len2];
			for( int j=0;j<len1;j++ )
				adds[j] = a1s[j];
			for( int j=0;j<len2;j++ )
				adds[j+len1] = a2s[j];
			newTcMatrix.add(adds);
		}
		tcMatrixs = newTcMatrix;
	}
	
	/**
	 * 读入SIR官网的fault-matrix.v0.tsl.universe 和 fault-matrix.v2.tsl.universe， 
	 *      数据存入当前对象。
	 *      
	 * @return true: read file ok.
	 */
	public boolean readSIRfaultMatrix()
	{
		verNo = 0;
		tcNo = 0;
		tcMatrixs.clear();
		checkSecondMatrix = false;//第二个fault-matrix有完整的测试记录，则为true；否则false;
		boolean result = true;
		int verFromSecondMatrixFile = 0;//从第二个fault-matrix读出来的版本数。
		
		if( (true==objectName.equalsIgnoreCase("flex"))
				|| (true==objectName.equalsIgnoreCase("grep"))	)
		{
			int[] tcsRef = new int[1];
			int[] versRef = new int[1];
			List<int[]> tcsFault = new ArrayList<int[]>();
			String faultMatrixFilename = ProjectConfiguration.DatasetDirectory +
					SubjectDirectoryMap.getDirectoryFromName(objectName) +"/info/"+getFirstFaultMatrix();
			result = readTcMatrixFile(faultMatrixFilename,versRef,tcsRef,tcsFault,false);
			if( true==result )
			{//读第一个测试结果文件成功。
				verNo = versRef[0];
				tcNo = tcsRef[0];
				tc2Index = tcNo;
				for( int[] tcsLine : tcsFault)
					tcMatrixs.add(tcsLine);
				tcsFault.clear();//清除上次读入的数据。
				faultMatrixFilename = ProjectConfiguration.DatasetDirectory +
						SubjectDirectoryMap.getDirectoryFromName(objectName) +"/info/"+getSecondFaultMatrix();
				result = readTcMatrixFile(faultMatrixFilename,versRef,tcsRef,tcsFault,false);
				verFromSecondMatrixFile = versRef[0];
			}
			if( true==result )
			{//读第二个测试结果文件成功。
				//verNo += versRef[0]; //版本数目不变。
				tcNo += tcsRef[0];
				//verFromSecondMatrixFile是第二个fault-matrix读出来的版本数。
				//verNo是第1个fault-matrix读出来的版本数。两者相同，才把第二次读到的tcsFault合并到tcMatrixs。
				if( verFromSecondMatrixFile==verNo )
				{
					mergeFaultMatrixList(tcsFault); //合并两个文件读到的结果。
					checkSecondMatrix = true; //所有测试用例都比较。
				}
			}//end of if( rue==result)
		}
		else if( (true==objectName.equalsIgnoreCase("gzip"))
				|| (true==objectName.equalsIgnoreCase("sed"))	)
		{
			int[] tcsRef = new int[1];
			int[] versRef = new int[1];
			String faultMatrixFilename = ProjectConfiguration.DatasetDirectory +
					SubjectDirectoryMap.getDirectoryFromName(objectName) +"/info/"+getFirstFaultMatrix();
			result = readTcMatrixFile(faultMatrixFilename,versRef,tcsRef,tcMatrixs,false);
			if( true==result )
			{//读第一个测试结果文件成功。
				verNo = versRef[0];
				tcNo = tcsRef[0];
				tc2Index = tcNo;
			}
		}
		else
			System.out.println("Error: readSIRfaultMatrix");
		return result;
	}
	/* 读入所有版本，所有测试用例测试结果通过与否的结果。
	 * @return true: read file ok.
	 * faultMatrixFilename要读的文件名，比如fault-matrix.v0.tsl.universe 和 fault-matrix.v2.tsl.universe
	 * versRef[0]读入该文件对应的版本数。
	 * tcsRef[0]读入该文件对应的测试用例数。
	 * tcsFault 记录该文件读入的各测试用例对应测试结果（pass or fail).调用此方法是，先创建tcsFault对象。
	 * readTc2Index=true,新格式的fault-matrix，要求读入tc2Index; =false,旧格式，不要读。
	 */
	private boolean readTcMatrixFile(String faultMatrixFilename,int[] versRef,int[] tcsRef,
			List<int[]> tcsFault,boolean readTc2Index)
	{
		int versMatrix = 0; //该fault-matrix.v(?)...文件包含的版本数
		int tcsMatrix = 0;  //该fault-matrix.v(?)...文件包含的测试用例数
		boolean result = true;
		try {
			File file = new File(faultMatrixFilename);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file));
				BufferedReader br = new BufferedReader(read);
				String lineTXT = null;
				lineTXT = br.readLine(); //Version total
				lineTXT = lineTXT.trim(); //去掉首尾空格
				String[]  strAry = lineTXT.split("\\s+"); //允许多个空格分割字符串
				versMatrix = Integer.valueOf(strAry[0]); //版本数
				
				lineTXT = br.readLine();  //test case total
				lineTXT = lineTXT.trim(); //去掉首尾空格
				strAry = lineTXT.split("\\s+");
				tcsMatrix = Integer.valueOf(strAry[0]); //测试用例数目
				
				//readTc2Index=true,新格式的fault-matrix，要求读入tc2Index; =false,旧格式，不要读。
				if( true==readTc2Index )
				{
					lineTXT = br.readLine();  //test case total
					lineTXT = lineTXT.trim(); //去掉首尾空格
					strAry = lineTXT.split("\\s+");
					tc2Index = Integer.valueOf(strAry[0]); //第一个测试套件里的测试用例数目
				}
				
				for( int i=0;i<tcsMatrix;i++ ) //跳过 nTcs 行，是测试用例的输入
					br.readLine(); 
				
				//初始化存放测试用例结果的matrix,-1表示没有结果。
				for( int p=0;p<versMatrix;p++ )
				{
					int[] tcLines = new int[tcsMatrix];//一个版本的所有测试结果。
					for( int q=0;q<tcsMatrix;q++  )
						tcLines[q] = -1;
					tcsFault.add(tcLines);
				}
				//逐行读入
				for( int i=0;i<tcsMatrix;i++ )
				{
					lineTXT = br.readLine();  //unitestXXX,from 0 start
					lineTXT = lineTXT.substring(7, lineTXT.length()-1);//"unitest"=7,最后一个是":");
					int tcno = Integer.valueOf(lineTXT);
					for( int j=0;j<versMatrix;j++ )
					{
						lineTXT = br.readLine();  //vXXX,from 1 start
						lineTXT = lineTXT.substring(1, lineTXT.length()-1);//"v"=1,最后一个是":");
						int vno = Integer.valueOf(lineTXT);
						lineTXT = br.readLine();  //测试结果,0 or 1
						lineTXT = lineTXT.trim(); //去掉首尾空格
						int tcoutcome = Integer.valueOf(lineTXT);
						tcsFault.get(vno-1)[tcno] = tcoutcome;
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
		versRef[0] = versMatrix;
		tcsRef[0] = tcsMatrix;
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
	
	//tc2Index
	public int getTc2Index()
	{
		return this.tc2Index;
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
	
		
	//我的输出结果和SIR提供不同，比较fault-matrix和fault-matrix.v?.tsl.universe
	public void compareFaultMatrixSIR()
	{
		if( false==readFaultMatrix() ) //读入文件fault-matrix
			return;
		List<int[]>  myMatrixs= tcMatrixs; //此时fault-matrix存放我的比较结果。
		tcMatrixs = new ArrayList<int[]>();  //存放SIR提供的比较结果。
		if( false==readSIRfaultMatrix() ) //读文件fault-matrix.v?.tsl.universe
		{
			System.out.println("Read file fault-matrix.v?.tsl.universe is error.");
			return;
		}
		//定义需要比较的测试用例个数。不同数据集此值不同。
		//注意：不管是读我的，还是SIR官网提供的，verNo和tcNo值相同。
		int compareTcs = tc2Index;
		//特别注意，必须先调用readFaultMatrix，再调用readSIRfaultMatrix，才能保证checkSecondMatrix的有效性。
		if( true==checkSecondMatrix )
			compareTcs = tcNo;

		boolean identify = true;
		for( int p=0;p<verNo;p++ )
		{
			int[] myTc = myMatrixs.get(p);
			int[] sirTc = tcMatrixs.get(p);
			for( int q=0;q<compareTcs;q++ )
			{
				if( myTc[q]!=sirTc[q] )
				{
					identify = false;
					System.out.print("For test case "+String.valueOf(q+1)+":");
					System.out.print("  v"+String.valueOf(p+1));
					System.out.print(",  my="+myTc[q]);
					System.out.print(",SIR="+sirTc[q]);
					System.out.println(".");
				}
			}
		}
		if( true==identify )
			System.out.println("same.");
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
