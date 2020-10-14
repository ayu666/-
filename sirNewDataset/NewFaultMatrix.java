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
 * SIR���ݼ��µĸ�ʽ��Siemens�׼��ɵĸ�ʽ����ͬ��
 *       ����NewFaultMatrix����ֱ�Ӽ̳���FaultMatrix�����ǵ���Ҫ���²��ԣ�����������һ��NewFaultMatrix��
 *     NewFaultMatrix�Ĵ�����Ҫ������FaultMatrix����������Ҫ�޸�FaultMatrix�� 
 */
public class NewFaultMatrix {
	private int verNo; //version  numbers
	private int tcNo;  //test case number;
	private int tc2Index; //fault-matrix.v0.tsl.universe��Ĳ���������Ŀ��Ҳ��fault-matrix.v2.tsl.universe���������
	          //��tcMatrixs������λ�õĿ�ʼ��tcNo-tc2Index����fault-matrix.v2.tsl.universe��Ĳ���������Ŀ��
	private boolean checkSecondMatrix; //����ڶ���fault-matrix�������Ĳ��Լ�¼����Ϊtrue������false;
	/*
	 * һ��Ҫע�⣺tcMatrixs��һ��Ԫ���ǵ�һ���汾V1�Ĳ�������ͨ����񼯺ϣ��ڶ�������ˣ��������� ��
	 * List<int[]>����� int[]Ҳ�����ǰ���˳���ţ���һ����������������ڶ�����...��
	 * ĳЩ���ݼ�����flex,...�ȣ����������������ļ������Ĳ��Խ����
	 *    fault-matrix.v0.tsl.universe �� fault-matrix.v2.tsl.universe
	 */
	private List<int[]> tcMatrixs = new ArrayList<int[]>();  //���а汾�Ĳ�������ͨ����񼯺ϡ�
	private String matrixFilename; //fault-matrix
	private String objectName; //dataset object name
	
	//ʹ�ô���ʱ�����ù��캯����������
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
	 * ���������ɵ�fault-matrix����ͨ���Ƚ��ļ����� 
	 *      ���ݴ��뵱ǰ����
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
	
	/** һЩSIR���¸�ʽ object ��������fault-matrix�����Ҹ����������Ҳ��ͬ��
	 * @return ��һ��
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
	
	/** һЩSIR���¸�ʽ object ��������fault-matrix�����Ҹ����������Ҳ��ͬ��
	 * @return ��һ��
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
	
	/**��tcsFault�ϲ���tcMatrixs
	 * @param tcsFault �ڶ��ζ����fault-matrix�ļ�
	 */
	private void mergeFaultMatrixList(List<int[]> tcsFault)
	{
		int v1 = tcMatrixs.size();
		int v2 = tcsFault.size();
		if( v1!=v2 )
		{ //��������İ汾��һ�£����ܵ��ô˷�����
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
	 * ����SIR������fault-matrix.v0.tsl.universe �� fault-matrix.v2.tsl.universe�� 
	 *      ���ݴ��뵱ǰ����
	 *      
	 * @return true: read file ok.
	 */
	public boolean readSIRfaultMatrix()
	{
		verNo = 0;
		tcNo = 0;
		tcMatrixs.clear();
		checkSecondMatrix = false;//�ڶ���fault-matrix�������Ĳ��Լ�¼����Ϊtrue������false;
		boolean result = true;
		int verFromSecondMatrixFile = 0;//�ӵڶ���fault-matrix�������İ汾����
		
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
			{//����һ�����Խ���ļ��ɹ���
				verNo = versRef[0];
				tcNo = tcsRef[0];
				tc2Index = tcNo;
				for( int[] tcsLine : tcsFault)
					tcMatrixs.add(tcsLine);
				tcsFault.clear();//����ϴζ�������ݡ�
				faultMatrixFilename = ProjectConfiguration.DatasetDirectory +
						SubjectDirectoryMap.getDirectoryFromName(objectName) +"/info/"+getSecondFaultMatrix();
				result = readTcMatrixFile(faultMatrixFilename,versRef,tcsRef,tcsFault,false);
				verFromSecondMatrixFile = versRef[0];
			}
			if( true==result )
			{//���ڶ������Խ���ļ��ɹ���
				//verNo += versRef[0]; //�汾��Ŀ���䡣
				tcNo += tcsRef[0];
				//verFromSecondMatrixFile�ǵڶ���fault-matrix�������İ汾����
				//verNo�ǵ�1��fault-matrix�������İ汾����������ͬ���Űѵڶ��ζ�����tcsFault�ϲ���tcMatrixs��
				if( verFromSecondMatrixFile==verNo )
				{
					mergeFaultMatrixList(tcsFault); //�ϲ������ļ������Ľ����
					checkSecondMatrix = true; //���в����������Ƚϡ�
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
			{//����һ�����Խ���ļ��ɹ���
				verNo = versRef[0];
				tcNo = tcsRef[0];
				tc2Index = tcNo;
			}
		}
		else
			System.out.println("Error: readSIRfaultMatrix");
		return result;
	}
	/* �������а汾�����в����������Խ��ͨ�����Ľ����
	 * @return true: read file ok.
	 * faultMatrixFilenameҪ�����ļ���������fault-matrix.v0.tsl.universe �� fault-matrix.v2.tsl.universe
	 * versRef[0]������ļ���Ӧ�İ汾����
	 * tcsRef[0]������ļ���Ӧ�Ĳ�����������
	 * tcsFault ��¼���ļ�����ĸ�����������Ӧ���Խ����pass or fail).���ô˷����ǣ��ȴ���tcsFault����
	 * readTc2Index=true,�¸�ʽ��fault-matrix��Ҫ�����tc2Index; =false,�ɸ�ʽ����Ҫ����
	 */
	private boolean readTcMatrixFile(String faultMatrixFilename,int[] versRef,int[] tcsRef,
			List<int[]> tcsFault,boolean readTc2Index)
	{
		int versMatrix = 0; //��fault-matrix.v(?)...�ļ������İ汾��
		int tcsMatrix = 0;  //��fault-matrix.v(?)...�ļ������Ĳ���������
		boolean result = true;
		try {
			File file = new File(faultMatrixFilename);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file));
				BufferedReader br = new BufferedReader(read);
				String lineTXT = null;
				lineTXT = br.readLine(); //Version total
				lineTXT = lineTXT.trim(); //ȥ����β�ո�
				String[]  strAry = lineTXT.split("\\s+"); //�������ո�ָ��ַ���
				versMatrix = Integer.valueOf(strAry[0]); //�汾��
				
				lineTXT = br.readLine();  //test case total
				lineTXT = lineTXT.trim(); //ȥ����β�ո�
				strAry = lineTXT.split("\\s+");
				tcsMatrix = Integer.valueOf(strAry[0]); //����������Ŀ
				
				//readTc2Index=true,�¸�ʽ��fault-matrix��Ҫ�����tc2Index; =false,�ɸ�ʽ����Ҫ����
				if( true==readTc2Index )
				{
					lineTXT = br.readLine();  //test case total
					lineTXT = lineTXT.trim(); //ȥ����β�ո�
					strAry = lineTXT.split("\\s+");
					tc2Index = Integer.valueOf(strAry[0]); //��һ�������׼���Ĳ���������Ŀ
				}
				
				for( int i=0;i<tcsMatrix;i++ ) //���� nTcs �У��ǲ�������������
					br.readLine(); 
				
				//��ʼ����Ų������������matrix,-1��ʾû�н����
				for( int p=0;p<versMatrix;p++ )
				{
					int[] tcLines = new int[tcsMatrix];//һ���汾�����в��Խ����
					for( int q=0;q<tcsMatrix;q++  )
						tcLines[q] = -1;
					tcsFault.add(tcLines);
				}
				//���ж���
				for( int i=0;i<tcsMatrix;i++ )
				{
					lineTXT = br.readLine();  //unitestXXX,from 0 start
					lineTXT = lineTXT.substring(7, lineTXT.length()-1);//"unitest"=7,���һ����":");
					int tcno = Integer.valueOf(lineTXT);
					for( int j=0;j<versMatrix;j++ )
					{
						lineTXT = br.readLine();  //vXXX,from 1 start
						lineTXT = lineTXT.substring(1, lineTXT.length()-1);//"v"=1,���һ����":");
						int vno = Integer.valueOf(lineTXT);
						lineTXT = br.readLine();  //���Խ��,0 or 1
						lineTXT = lineTXT.trim(); //ȥ����β�ո�
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
	
	//����ĳ�汾��������ͨ����Ŀ
	//ver from 1 start 
	public int getPassedTestcase(int ver)
	{
		int[] tcLines = tcMatrixs.get(ver-1);
		return SIRTools.getPassedTc(tcLines);
	}
	
	//����ĳ�汾��������ʧ����Ŀ
	//ver from 1 start 
	public int getFailedTestcase(int ver)
	{
		int[] tcLines = tcMatrixs.get(ver-1);
		return SIRTools.getFailedTc(tcLines);
	}
	
		
	//�ҵ���������SIR�ṩ��ͬ���Ƚ�fault-matrix��fault-matrix.v?.tsl.universe
	public void compareFaultMatrixSIR()
	{
		if( false==readFaultMatrix() ) //�����ļ�fault-matrix
			return;
		List<int[]>  myMatrixs= tcMatrixs; //��ʱfault-matrix����ҵıȽϽ����
		tcMatrixs = new ArrayList<int[]>();  //���SIR�ṩ�ıȽϽ����
		if( false==readSIRfaultMatrix() ) //���ļ�fault-matrix.v?.tsl.universe
		{
			System.out.println("Read file fault-matrix.v?.tsl.universe is error.");
			return;
		}
		//������Ҫ�ȽϵĲ���������������ͬ���ݼ���ֵ��ͬ��
		//ע�⣺�����Ƕ��ҵģ�����SIR�����ṩ�ģ�verNo��tcNoֵ��ͬ��
		int compareTcs = tc2Index;
		//�ر�ע�⣬�����ȵ���readFaultMatrix���ٵ���readSIRfaultMatrix�����ܱ�֤checkSecondMatrix����Ч�ԡ�
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
	 * ���Դ���
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
