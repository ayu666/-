/**
 * 
 */
package sbflCommon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import sirDataset.ProjectConfiguration;
import sirDataset.SubjectDirectoryMap;

/**
 * @author Administrator
 * ��д�ļ�.testcase
 */
public class TestcaseFile {
	int verNo; //version  numbers
	int tcNo;  //test case number;
	/*
	 * һ��Ҫע�⣺testCases��һ��Ԫ���ǵ�һ���汾��passed,failed,�������� ��
	 * ��Ȼ��(object).testcase�ļ������ϢҲ���밴��˳���š�
	 */
	List<int[]> testCases = new ArrayList<int[]>();  //���а汾��passed,failed��
	String tcFilename; //(object).testcase
	
	//���캯������Ҫ����.testcase�ļ�����װ�����ݡ�
	public TestcaseFile (String objectName)
	{
		tcFilename = ProjectConfiguration.DatasetDirectory +
				SubjectDirectoryMap.getDirectoryFromName(objectName) +"/"+objectName+".testcase";
	}

	//���캯������Щ���ݽ�д��.testcase�ļ���
	public TestcaseFile (String objectName,int ver,int tc,List<int[]> tcers)
	{
		tcFilename = ProjectConfiguration.DatasetDirectory +
				SubjectDirectoryMap.getDirectoryFromName(objectName) +"/"+objectName+".testcase";
		verNo = ver;
		tcNo = tc;
		testCases = tcers;
	}

	/**
	 * @return true: read file ok.
	 */
	public boolean readTestcaseFile()
	{
		boolean result = true;
		try {
			File file = new File(tcFilename);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file));
				BufferedReader br = new BufferedReader(read);
				String lineTXT = null;

				lineTXT = br.readLine(); //Version total
				lineTXT = lineTXT.trim(); //ȥ����β�ո�
				String[]  strAry = lineTXT.split("\\s+"); //�������ո�ָ��ַ���
				verNo = Integer.valueOf(strAry[1]); //�汾��
				
				lineTXT = br.readLine();  //test case total
				lineTXT = lineTXT.trim(); //ȥ����β�ո�
				strAry = lineTXT.split("\\s+");
				tcNo = Integer.valueOf(strAry[1]); //����������Ŀ
				
				br.readLine(); //testcase line info prompt
				for( int i=0;i<verNo;i++ )
				{
					lineTXT = br.readLine();  //testcase line info
					lineTXT = lineTXT.trim(); //ȥ����β�ո�
					strAry = lineTXT.split("\\s+");
					int[] pfNums = new int[2];
					for( int k=0;k<2;k++ )
					{
						int tcer = Integer.valueOf(strAry[k+1]);//0 is verTH
						pfNums[k] = tcer;
					}
					testCases.add(pfNums);
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
		return result;
	}
	
	/**  ����ǰ������д��.profile�ļ�
	 *   �Ѿ�ͨ�����캯��������ֵ
	 * @return
	 */
	public boolean writeTestcaseFile()
	{
		boolean result = true;
	    OutputStreamWriter ops = null;
	    BufferedWriter bw = null; 
	    File file;
	    try {
	        file = new File(tcFilename);
	        if( file.isFile()&& file.exists() )
	        	file.delete();
	        file.createNewFile();
	        ops = new OutputStreamWriter(new FileOutputStream(file));
	        bw = new BufferedWriter(ops);
	        ops.write("verno      "+String.valueOf(verNo)+"\r\n");
	        ops.write("testcase   "+String.valueOf(tcNo)+"\r\n");
	        ops.write("version    passed    failed\r\n");
	        //������Ϣд�롣
	        for( int i=0;i<verNo;i++ )
	        {
	        	ops.write("      v"+String.valueOf(i+1)+
	        			"      "+String.valueOf(testCases.get(i)[0])+
	        			"      "+String.valueOf(testCases.get(i)[1])+"\r\n");
	        }
	        bw.close();
        	ops.close();
	    }
	    catch (Exception e) {
	    	result = false;
	        e.printStackTrace();
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
	
	public List<int[]> getFaultLines()
	{
		return this.testCases;
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
		
		System.out.println("testcases(verth,passed,failed) : ");
		for( int i=0;i<verNo;i++ )
		{
			System.out.println( "v"+String.valueOf(i+1)+
					"     "+String.valueOf(testCases.get(i)[0])+
					"     "+String.valueOf(testCases.get(i)[1]) );
		}
	} //end of testMe.
}
