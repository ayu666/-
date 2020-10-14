/**
 *  ����SIR�ṩ���ļ�info\fault-matrix
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
	 * һ��Ҫע�⣺tcMatrixs��һ��Ԫ���ǵ�һ���汾V1�Ĳ�������ͨ����񼯺ϣ��ڶ�������ˣ��������� ��
	 * List<int[]>����� int[]Ҳ�����ǰ���˳���ţ���һ����������������ڶ�����...��
	 */
	List<int[]> tcMatrixs = new ArrayList<int[]>();  //���а汾�Ĳ�������ͨ����񼯺ϡ�
	String matrixFilename; //fault-matrix
	String objectName; //dataset object name
	/*
	 * objectName:�������֡�
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
	/* �������а汾�����в����������Խ��ͨ�����Ľ����
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
				lineTXT = lineTXT.trim(); //ȥ����β�ո�
				String[]  strAry = lineTXT.split("\\s+"); //�������ո�ָ��ַ���
				verNo = Integer.valueOf(strAry[0]); //�汾��
				
				lineTXT = br.readLine();  //test case total
				lineTXT = lineTXT.trim(); //ȥ����β�ո�
				strAry = lineTXT.split("\\s+");
				tcNo = Integer.valueOf(strAry[0]); //����������Ŀ
				
				for( int i=0;i<tcNo;i++ ) //���� nTcs �У��ǲ�������������
					br.readLine(); 
				
				//��ʼ����Ų������������matrix,-1��ʾû�н����
				for( int p=0;p<verNo;p++ )
				{
					int[] tcLines = new int[tcNo];//һ���汾�����в��Խ����
					for( int q=0;q<tcNo;q++  )
						tcLines[q] = -1;
					tcMatrixs.add(tcLines);
				}
				//���ж���
				for( int i=0;i<tcNo;i++ )
				{
					lineTXT = br.readLine();  //unitestXXX,from 0 start
					lineTXT = lineTXT.substring(7, lineTXT.length()-1);//"unitest"=7,���һ����":");
					int tcno = Integer.valueOf(lineTXT);
					for( int j=0;j<verNo;j++ )
					{
						lineTXT = br.readLine();  //vXXX,from 1 start
						lineTXT = lineTXT.substring(1, lineTXT.length()-1);//"v"=1,���һ����":");
						int vno = Integer.valueOf(lineTXT);
						lineTXT = br.readLine();  //���Խ��,0 or 1
						lineTXT = lineTXT.trim(); //ȥ����β�ո�
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
	
	//�ҵ���������SIR�ṩ��ͬ���Ƚ�fault-matrix��fault-matrix.sir
	public void compareFaultMatrixSIR()
	{
		if( false==readTcMatrixFile() )
			return;
		List<int[]>  myMatrixs= tcMatrixs; //��ʱfault-matrix����ҵıȽϽ����
		tcMatrixs = new ArrayList<int[]>();  //���SIR�ṩ�ıȽϽ����
		matrixFilename = matrixFilename+".sir";
		if( false==readTcMatrixFile() ) //���ļ�fault-matrix.sir
		{
			System.out.println("Read file is error:  "+matrixFilename);
			return;
		}
		
		int[] diffs = new int[verNo]; //��¼ÿ���汾�Ĳ��������
		boolean identify = true;
		for( int p=0;p<verNo;p++ )
		{
			int[] myTc = myMatrixs.get(p);
			int[] sirTc = tcMatrixs.get(p);
			int totalDiff = 0; //�ð汾�ҵıȽϽ����SIR�����ͬ�ĸ�����
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
		//���̫�಻ͬ���޷�����ʾ��������⡣
		for( int k=0;k<verNo;k++ )
		{
			if( diffs[k]>0 )
				System.out.println("Version  "+String.valueOf(k+1)+"  have "+String.valueOf(diffs[k])+" differents");
		}
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
