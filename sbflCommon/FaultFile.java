/**
 * 读入(object).fault文件
 * 该文件说明了对应object所有版本的故障语句所在行号。
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
	 * 一定要注意：faultLines第一个元素是第一个版本V1的故障语句行号集合，第二个亦如此，依次类推 。
	 * 当然，(object).fault文件里的信息也必须按照顺序存放。
	 */
	List<int[]> faultLines = new ArrayList<int[]>();  //所有版本的故障语句行号集合。
	String faultFilename; //(object).fault
	/*
	 * objectName:对象名字。
	 */
	public FaultFile(String objectName)
	{
		faultFilename = ProjectConfiguration.DatasetDirectory +
				SubjectDirectoryMap.getDirectoryFromName(objectName) +"/"+objectName+".fault";
	}
	
	/* spilt(" ");的处理结果。
	 * 1，字符串首部的每个空格都转成了一个空串。
	 * 2，中间的多个空格中，多余的每个空格都转成了一个空串。
	 * 3，末尾的多个空格全都去掉了。
	 * 
	 * spilt("\\s+");的处理结果。
	 * 1，字符串首部的所有空格转成了一个空串。
	 * 2，中间的多个空格，全都去掉了。
	 * 3，末尾的多个空格全都去掉了。
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
				lineTXT = lineTXT.trim(); //去掉首尾空格
				String[]  strAry = lineTXT.split("\\s+"); //允许多个空格分割字符串
				verNo = Integer.valueOf(strAry[0]); //版本数
				
				lineTXT = br.readLine();  //test case total
				lineTXT = lineTXT.trim(); //去掉首尾空格
				strAry = lineTXT.split("\\s+");
				tcNo = Integer.valueOf(strAry[0]); //测试用例数目
				
				br.readLine(); //fault line info prompt
				for( int i=0;i<verNo;i++ )
				{
					lineTXT = br.readLine();  //fault line info
					lineTXT = lineTXT.trim(); //去掉首尾空格
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
	 * 测试代码
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
