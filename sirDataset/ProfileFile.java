/**
 * 
 */
package sirDataset;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import sbflCommon.SpectrumStruct;

/**
 * @author Administrator
 * 注意：只保存单个.profile数据，并非保存所有版本的。
 */
public class ProfileFile {
	private int thVer;  //第几个版本，注意不是版本个数verNo。
	private int tcPassed; //测试用例通过数
	private int tcFailed; //测试用例未通过数
	private int execTotal; //该版本代码，可执行语句条数
	private List<SpectrumStruct> spectrumList; //程序谱
	private String objectName; //对象名。
	
	
	//某一个版本的程序谱。
	public ProfileFile(int ver,String object)
	{
		thVer = ver;
		objectName = object;
	}
	
	//注意ver=1,...verNo,不是版本个数verNo。
	public ProfileFile(String object,int ver,int passed,int failed,int total,
			      List<SpectrumStruct> ssList)
	{
		objectName = object;
		thVer = ver;
		tcPassed = passed;
		tcFailed = failed;
		execTotal = total;
		spectrumList = ssList;
	}
	
	
	public int getVerTh()
	{
		return thVer;
	}
	
	public int getPassed()
	{
		return tcPassed;
	}
	
	public int getFailed()
	{
		return tcFailed;
	}
	
	public int getTotalExec()
	{
		return execTotal;
	}
	
	public List<SpectrumStruct> getSpectrumList()
	{
		return spectrumList;
	}
	
	//从.profile文件读入程序谱
	public boolean readProfileFile()
	{
		boolean result = true;
		
		spectrumList = new ArrayList<SpectrumStruct> ();
		FileInputStream fis = null;
	    DataInputStream dis = null;
	    File file;
	    try {
	        String filename = ProjectConfiguration.DatasetDirectory +
					SubjectDirectoryMap.getDirectoryFromName(objectName) +"/profile/"+
					objectName+"_v"+String.valueOf(thVer)+".profile";;
	        file = new File(filename);
	        if( file.isFile()&& file.exists() )
	        {
	        	fis = new FileInputStream(file);
	        	dis = new DataInputStream(fis);
	        	thVer = dis.readInt();
	        	tcPassed = dis.readInt();
	        	tcFailed = dis.readInt();
	        	execTotal = dis.readInt();
		        //逐条读入语句的谱信息。
	        	for(int k=0;k<execTotal;k++ )
		        {
	        		int lineno = dis.readInt();
	        		int aep = dis.readInt();
	        		int aef = dis.readInt();
	        		SpectrumStruct item  = new SpectrumStruct(lineno,aep,aef);
	        		spectrumList.add(item);
		        }
	        }//end of if
	        else
	        	result = false;
	    }
	    catch (Exception e) {
	    	result = false;
	        e.printStackTrace();
	    } 
	    finally {
	        try {
	            if (fis != null) {
	            	dis.close();
	            	fis.close();
	            }
	        } 
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
		return result;

	}
	
	
	/**  将程序谱写入.profile文件
	 * @return
	 */
	public boolean writeProfileFile()
	{
		boolean result = true;
		FileOutputStream fos = null;
	    DataOutputStream dos = null;
	    File file;
	    try {
	        String filename = ProjectConfiguration.DatasetDirectory +
					SubjectDirectoryMap.getDirectoryFromName(objectName) +"/profile/"+
					objectName+"_v"+String.valueOf(thVer)+".profile";;
	        file = new File(filename);
	        if( file.isFile()&& file.exists() )
	        	file.delete();
	        file.createNewFile();
	        fos = new FileOutputStream(file);
	        dos = new DataOutputStream(fos);
	        dos.writeInt(thVer);
	        dos.writeInt(tcPassed);
	        dos.writeInt(tcFailed);
	        dos.writeInt(execTotal);
	        //逐条语句的谱信息写入。
	        for( SpectrumStruct item : spectrumList )
	        {
        		dos.writeInt(item.getLineNo());
        		dos.writeInt(item.getAep());
        		dos.writeInt(item.getAef());
	        }
	    }
	    catch (Exception e) {
	    	result = false;
	        e.printStackTrace();
	    } 
	    finally {
	        try {
	            if (fos != null) {
	            	dos.close();
	            	fos.close();
	            }
	        } 
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
		return result;

	}
	
	/**从.txt文件读程序谱。
	 * 为测试编写此段代码
	 * @return true: read file ok.
	 */
	public boolean readTxtFile()
	{
		boolean result = true;
		spectrumList = new ArrayList<SpectrumStruct> ();
		try {
			String filename = ProjectConfiguration.DatasetDirectory +
					SubjectDirectoryMap.getDirectoryFromName(objectName) +"/profile/"+
					objectName+"_v"+String.valueOf(thVer)+".txt";;
			File file = new File(filename);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file));
				BufferedReader br = new BufferedReader(read);
				String lineTXT = null;
				//while ((lineTXT = br.readLine()) != null) {}
				br.readLine(); //th ver，此信息已经有，不用再读。
			
				lineTXT = br.readLine(); //passed
				lineTXT = lineTXT.trim(); //去掉首尾空格
				String[]  strAry = lineTXT.split("\\s+"); //允许多个空格分割字符串
				tcPassed = Integer.valueOf(strAry[0]); //通过测试用例数
				
				lineTXT = br.readLine(); //failed
				lineTXT = lineTXT.trim(); //去掉首尾空格
				strAry = lineTXT.split("\\s+"); //允许多个空格分割字符串
				tcFailed = Integer.valueOf(strAry[0]); //未通过测试用例数
				
				lineTXT = br.readLine();  //test case total
				lineTXT = lineTXT.trim(); //去掉首尾空格
				strAry = lineTXT.split("\\s+");
				execTotal = Integer.valueOf(strAry[0]); //可执行语句数目
				
				for( int i=0;i<execTotal;i++ )
				{
					lineTXT = br.readLine();  //fault line info
					lineTXT = lineTXT.trim(); //去掉首尾空格
					String[] spectrumAry = lineTXT.split("\\s+");
					int lineno =Integer.valueOf(spectrumAry[0]);
	        		int aep = Integer.valueOf(spectrumAry[1]);
	        		int aef = Integer.valueOf(spectrumAry[2]);
	        		SpectrumStruct item  = new SpectrumStruct(lineno,aep,aef);
	        		spectrumList.add(item);
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
	
	/**  将当前的程序谱文件转换成.txt
	 *   已经读入.profile文件
	 * @return
	 */
	public boolean writeToTXT()
	{
		boolean result = true;
	    OutputStreamWriter ops = null;
	    BufferedWriter bw = null; 
	    File file;
	    try {
	        String filename = ProjectConfiguration.DatasetDirectory +
					SubjectDirectoryMap.getDirectoryFromName(objectName) +"/profile/"+
					objectName+"_v"+String.valueOf(thVer)+".txt";;
	        file = new File(filename);
	        if( file.isFile()&& file.exists() )
	        	file.delete();
	        file.createNewFile();
	        ops = new OutputStreamWriter(new FileOutputStream(file));
	        bw = new BufferedWriter(ops);
	        ops.write(String.valueOf(thVer)+"    th ver\r\n");
	        ops.write(String.valueOf(tcPassed)+"    passed\r\n");
	        ops.write(String.valueOf(tcFailed)+"    failed\r\n");
	        ops.write(String.valueOf(execTotal)+"   exec total \r\n");
	        //逐条语句的谱信息写入。
	        for( SpectrumStruct item : spectrumList )
	        {
	        	ops.write(String.valueOf(item.getLineNo())+"   "+
	        			String.valueOf(item.getAep())+"   "+
	        			String.valueOf(item.getAef())+"\r\n");
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
	/**
	 * 测试代码
	 */
	public void testMe()
	{
		int verth = getVerTh();
		System.out.println("ver th := "+verth);
		int pass = getPassed();
		System.out.println("Passed test case  := "+pass);
		int fail = getFailed();
		System.out.println("Failed test case  := "+fail);
		int total = getTotalExec();
		System.out.println("Total Exec statement: "+total);
		List<SpectrumStruct> ssl = getSpectrumList();
		for( SpectrumStruct item : ssl )
		{
			System.out.print(item.getLineNo()+","+item.getAep()+","+item.getAef());
			System.out.println(" ");
		}
	} //end of testMe.
}
