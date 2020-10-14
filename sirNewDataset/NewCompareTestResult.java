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
 *SIR���ݼ��µĸ�ʽ��Siemens�׼��ɵĸ�ʽ����ͬ��
 *       ����NewCompareTestResult����ֱ�Ӽ̳���CompareTestResult�����ǵ���Ҫ���²��ԣ�����������һ��NewCompareTestResult��
 *     NewFaultMatrix�Ĵ�����Ҫ������CompareTestResult����������Ҫ�޸�CompareTestResult�� 
 */
/**
 * @author Administrator
 *
 */
public class NewCompareTestResult {
	int verNo; //version  numbers 
	int tcNo;  //test case number;
	private int tc2Index; //fault-matrix.v0.tsl.universe��Ĳ���������Ŀ��Ҳ��fault-matrix.v2.tsl.universe���������
    //��tcMatrixs������λ�õĿ�ʼ��tcNo-tc2Index����fault-matrix.v2.tsl.universe��Ĳ���������Ŀ��
	//����ڶ���fault-matrix�������Ĳ��Լ�¼�����ʱ��tc2Index = tcNo
	/*
	 * һ��Ҫע�⣺tcMatrixs��һ��Ԫ���ǵ�һ���汾V1�Ĳ�������ͨ����񼯺ϣ��ڶ�������ˣ��������� ��
	 * List<int[]>����� int[]Ҳ�����ǰ���˳���ţ���һ����������������ڶ�����...��
	 * * ĳЩ���ݼ�����flex,...�ȣ����������������ļ������Ĳ��Խ����
	 *    fault-matrix.v0.tsl.universe �� fault-matrix.v2.tsl.universe
	 */
	List<int[]> tcMatrixs = new ArrayList<int[]>();  //���а汾�Ĳ�������ͨ����񼯺ϡ�
	String objectName; //object name
	
	public NewCompareTestResult(int ver,int tc,String object,int tc2)
	{
		verNo = ver;
		tcNo = tc;
		objectName = object;
		tc2Index = tc2;
	}
	
	/** ����FLEX���ݼ��Ĳ��Խ���Ƚϡ� flex��Ҫ�Ƚ������ļ���ֻҪ��һ����ͬ������Ϊ����δͨ����
	 * @param tcnum ���������� from 1 start
	 * @param vernum ���������� from 1 start
	 * @return �ȽϽ�� 1�����������ͬ������ʧ�ܣ� 0 �������ļ���������ͬ����������ͨ��
	 *         -1�����ļ�����
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
		{ //�ڶ��������׼���
			faultFile1 = dirFault+"/err3.n"+String.valueOf(tcnum-tc2Index);
			okFile1 = dirOk + "/err3.n"+String.valueOf(tcnum-tc2Index);
			faultFile2 = dirFault +"/out2.n"+String.valueOf(tcnum-tc2Index);
			okFile2 = dirOk +"/out2.n"+String.valueOf(tcnum-tc2Index);
		}
		nok = SBFLTools.newCompareFile(faultFile1,okFile1);
		if( nok<0 )
			return nok; //���ļ�ʧ��
		if( nok==0 ) //�����ļ���ͬ������Ҫ�����Ƚϡ�
			nok = SBFLTools.newCompareFile(faultFile2,okFile2);
		//nok = SBFLTools.compareFile(faultFile2,okFile2);
		return nok;
	}
	
	/** ����grep���ݼ��Ĳ��Խ���Ƚϡ� grep�Ƚ�һ���ļ���
	 * @param tcnum ���������� from 1 start
	 * @param vernum ���������� from 1 start
	 * @return �ȽϽ�� 1�����������ͬ������ʧ�ܣ� 0 �������ļ���������ͬ����������ͨ��
	 *         -1�����ļ�����
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
		{ //�ڶ��������׼���
			faultFile = dirFault+"/t2_"+String.valueOf(tcnum-tc2Index);
			okFile = dirOk + "/t2_"+String.valueOf(tcnum-tc2Index);
		}
		nok = SBFLTools.compareFile(faultFile,okFile); //not newCompareFile
		return nok;
	}
	
	
	/** ����gzip���ݼ��Ĳ��Խ���Ƚϡ� gzip�Ƚ�һ���ļ���
	 * @param tcnum ���������� from 1 start
	 * @param vernum ���������� from 1 start
	 * @return �ȽϽ�� 1�����������ͬ������ʧ�ܣ� 0 �������ļ���������ͬ����������ͨ��
	 *         -1�����ļ�����
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
	
	/** ����SED���ݼ��Ĳ��Խ���Ƚϡ� sed��Ҫ�Ƚ������ļ���ֻҪ��һ����ͬ������Ϊ����δͨ����
	 * @param tcnum ���������� from 1 start
	 * @param vernum ���������� from 1 start
	 * @return �ȽϽ�� 1�����������ͬ������ʧ�ܣ� 0 �������ļ���������ͬ����������ͨ��
	 *         -1�����ļ�����
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
		//t??�����ļ��ж��У�ÿ�������������У�����s??.wout����Щ���������ޣ����ҿ���ֻ������һ���ļ��С�
		nok = SBFLTools.newCompareFile(faultFile2,okFile2);
		if( nok<0 )
			return nok; //���ļ�ʧ��
		if( nok==0 ) //�����ļ���ͬ������Ҫ�����Ƚϡ�
			nok = SBFLTools.newCompareFile(faultFile1,okFile1);
		return nok;
	}
	
	
	//��SIR�������������ļ������Ƚ��ļ����ݡ����ȽϽ������tcMatrixs
	public boolean readFile()
	{
		//��ʼ����Ų������������matrix,-1��ʾû�н����
		for( int p=0;p<verNo;p++ )
		{
			int[] tcLines = new int[tcNo];//һ���汾�����в��Խ����
			for( int q=0;q<tcNo;q++  )
				tcLines[q] = -1;
			tcMatrixs.add(tcLines);
		}
		
		//�Ƚ�v(xxx)��v(ok)֮��Ĳ����������ͬ������Ϊ��������ͨ��������ʧ�ܡ�
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
	
	//����ĳ�汾��������ͨ����Ŀ
	//ver from 1 start 
	public int getPassedTestcase(int ver)
	{
		int[] tcLines = tcMatrixs.get(ver-1);
		int passed =SIRTools.getPassedTc(tcLines);
		return passed;
	}
	
	//����ĳ�汾��������ʧ����Ŀ
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
	        //�ر�ע�⣺����Ӳ���룬��tc2Indexд���ļ�fault-matrix��Siemens�׼�û�и����ݡ�
	        ops.write("   "+String.valueOf(tc2Index)+"  inV0.tsl.universe\r\n");
	        for( int t=1;t<=tcNo;t++)
	        	ops.write("testcase  "+String.valueOf(t)+"\r\n");
	        //������Ϣд�롣
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
	 * ���Դ���
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
