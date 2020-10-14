/**
 * �Ƚ�v(xxx)��v(ok)֮��Ĳ����������ͬ������Ϊ��������ͨ��������ʧ�ܡ�
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
	 * һ��Ҫע�⣺tcMatrixs��һ��Ԫ���ǵ�һ���汾V1�Ĳ�������ͨ����񼯺ϣ��ڶ�������ˣ��������� ��
	 * List<int[]>����� int[]Ҳ�����ǰ���˳���ţ���һ����������������ڶ�����...��
	 */
	List<int[]> tcMatrixs = new ArrayList<int[]>();  //���а汾�Ĳ�������ͨ����񼯺ϡ�
	String objectName; //object name
	
	public CompareTestResult(int ver,int tc,String object)
	{
		verNo = ver;
		tcNo = tc;
		objectName = object;
	}
	
	//��д��Junit����ģ�飬�������⣬����ô����
	//intǰ���public,private��ͨ�������롣
	int jUnitCompareFile(String faultOutput,String okOutput)
	{
		return SBFLTools.compareFile(faultOutput, okOutput);
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
	
	//���SIR�ṩ��fault-matrix���ҵĽ���Ƿ���ͬ��
	public void checkPassFail(List<int[]> tcResult)
	{
		boolean identify = true;
		int[] diffs = new int[verNo]; //��¼ÿ���汾�Ĳ��������
		for( int p=0;p<verNo;p++ )
		{
			int[] myTc = tcMatrixs.get(p);
			int[] sirTc = tcResult.get(p);
			int totalDiff = 0; //�ð汾�ҵıȽϽ����SIR�����ͬ�ĸ�����
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
		//���̫�಻ͬ���޷�����ʾ��������⡣
		for( int k=0;k<verNo;k++ )
		{
			if( diffs[k]>0 )
				System.out.println("Version  "+String.valueOf(k+1)+"  have "+String.valueOf(diffs[k])+" differents");
		}
	}
}//end of class.
