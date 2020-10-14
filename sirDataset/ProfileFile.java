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
 * ע�⣺ֻ���浥��.profile���ݣ����Ǳ������а汾�ġ�
 */
public class ProfileFile {
	private int thVer;  //�ڼ����汾��ע�ⲻ�ǰ汾����verNo��
	private int tcPassed; //��������ͨ����
	private int tcFailed; //��������δͨ����
	private int execTotal; //�ð汾���룬��ִ���������
	private List<SpectrumStruct> spectrumList; //������
	private String objectName; //��������
	
	
	//ĳһ���汾�ĳ����ס�
	public ProfileFile(int ver,String object)
	{
		thVer = ver;
		objectName = object;
	}
	
	//ע��ver=1,...verNo,���ǰ汾����verNo��
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
	
	//��.profile�ļ����������
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
		        //����������������Ϣ��
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
	
	
	/**  ��������д��.profile�ļ�
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
	        //������������Ϣд�롣
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
	
	/**��.txt�ļ��������ס�
	 * Ϊ���Ա�д�˶δ���
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
				br.readLine(); //th ver������Ϣ�Ѿ��У������ٶ���
			
				lineTXT = br.readLine(); //passed
				lineTXT = lineTXT.trim(); //ȥ����β�ո�
				String[]  strAry = lineTXT.split("\\s+"); //�������ո�ָ��ַ���
				tcPassed = Integer.valueOf(strAry[0]); //ͨ������������
				
				lineTXT = br.readLine(); //failed
				lineTXT = lineTXT.trim(); //ȥ����β�ո�
				strAry = lineTXT.split("\\s+"); //�������ո�ָ��ַ���
				tcFailed = Integer.valueOf(strAry[0]); //δͨ������������
				
				lineTXT = br.readLine();  //test case total
				lineTXT = lineTXT.trim(); //ȥ����β�ո�
				strAry = lineTXT.split("\\s+");
				execTotal = Integer.valueOf(strAry[0]); //��ִ�������Ŀ
				
				for( int i=0;i<execTotal;i++ )
				{
					lineTXT = br.readLine();  //fault line info
					lineTXT = lineTXT.trim(); //ȥ����β�ո�
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
	
	/**  ����ǰ�ĳ������ļ�ת����.txt
	 *   �Ѿ�����.profile�ļ�
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
	        //������������Ϣд�롣
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
	 * ���Դ���
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
