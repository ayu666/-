/**
 * ����(object).fault�ļ�
 * ���ļ�˵���˶�Ӧobject���а汾�Ĺ�����������кš�
 */
package sbflCommon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import sirDataset.ProjectConfiguration;
import sirDataset.SubjectDirectoryMap;

/**
 * @author Administrator
 *
 */
public class FaultFile {
	int verNo; //version  numbers
	int tcNo;  //test case number;
	/*
	 * һ��Ҫע�⣺faultLines��һ��Ԫ���ǵ�һ���汾V1�Ĺ�������кż��ϣ��ڶ�������ˣ��������� ��
	 * ��Ȼ��(object).fault�ļ������ϢҲ���밴��˳���š�
	 */
	List<int[]> faultLines = new ArrayList<int[]>();  //���а汾�Ĺ�������кż��ϡ�
	String faultFilename; //(object).fault
	/*
	 * objectName:�������֡�
	 */
	public FaultFile(String objectName)
	{
		faultFilename = ProjectConfiguration.DatasetDirectory +
				SubjectDirectoryMap.getDirectoryFromName(objectName) +"/"+objectName+".fault";
	}
	
	/* spilt(" ");�Ĵ�������
	 * 1���ַ����ײ���ÿ���ո�ת����һ���մ���
	 * 2���м�Ķ���ո��У������ÿ���ո�ת����һ���մ���
	 * 3��ĩβ�Ķ���ո�ȫ��ȥ���ˡ�
	 * 
	 * spilt("\\s+");�Ĵ�������
	 * 1���ַ����ײ������пո�ת����һ���մ���
	 * 2���м�Ķ���ո�ȫ��ȥ���ˡ�
	 * 3��ĩβ�Ķ���ո�ȫ��ȥ���ˡ�
	 */
	/**
	 * @return true: read file ok.
	 */
	public boolean readFaultFile()
	{
		boolean result = true;
		try {
			File file = new File(faultFilename);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file));
				BufferedReader br = new BufferedReader(read);
				String lineTXT = null;
				//while ((lineTXT = br.readLine()) != null) {}
				br.readLine(); //the prompt
			
				lineTXT = br.readLine(); //Version total
				lineTXT = lineTXT.trim(); //ȥ����β�ո�
				String[]  strAry = lineTXT.split("\\s+"); //�������ո�ָ��ַ���
				verNo = Integer.valueOf(strAry[0]); //�汾��
				
				lineTXT = br.readLine();  //test case total
				lineTXT = lineTXT.trim(); //ȥ����β�ո�
				strAry = lineTXT.split("\\s+");
				tcNo = Integer.valueOf(strAry[0]); //����������Ŀ
				
				br.readLine(); //fault line info prompt
				for( int i=0;i<verNo;i++ )
				{
					lineTXT = br.readLine();  //fault line info
					lineTXT = lineTXT.trim(); //ȥ����β�ո�
					strAry = lineTXT.split("\\s+");
					String[]  faultAry =  strAry[1].split(",");
					int[] faultLine = new int[faultAry.length];
					for( int k=0;k<faultAry.length;k++ )
					{
						int lineno = Integer.valueOf(faultAry[k]);
						faultLine[k] = lineno;
					}
					faultLines.add(faultLine);
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
		return this.faultLines;
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
		
		System.out.println("Fault lines : ");
		List<int[]> fls = getFaultLines();
		for( int[] items : fls )
		{
			int num = items.length;
			for( int t=0;t<num;t++ )
				System.out.print(items[t]+"  ");
			System.out.println(" ");
		}
	} //end of testMe.
}//end of class
