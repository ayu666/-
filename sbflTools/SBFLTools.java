/**
 * 
 */
package sbflTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Administrator
 * SBFL相关技术，公共工具。
 */
public class SBFLTools {
	/** Check nStatement is or not fault code line.
	 * @param nStatement 待检查的语句，其行号
	 * @param failStatms 语句行号集合
	 * @return return TRUE: is fault; else is not fault.
	 */
	public static boolean isFaultCode(int nStatement,int[] failStatms)
	{
		boolean bFault = false;
		//judge this is a fault line.
		for( int item : failStatms )
		{
			if( nStatement==item )
			{
				bFault = true;
				break;
			}
		}
		return bFault;
	}
	/**
	 * @param faultOutput 有故障的程序，其输出结果
	 * @param okOutput    正确的程序，其输出结果
	 * @return 1，两个结果不同，测试失败； 0 ，两个文件输出结果相同，测试用例通过
	 *         -1，读文件出错
	 */
	public static int compareFile(String faultOutput,String okOutput)
	{
		FileInputStream fis1 = null;  
	    FileInputStream fis2 = null;  
		try {  
	        fis1 = new FileInputStream(faultOutput);  
	        fis2 = new FileInputStream(okOutput);  
		          
	        int len1 = fis1.available();//返回总的字节数  
	        int len2 = fis2.available();  
		          
	        if (len1 == len2) {//长度相同，则比较具体内容  
	            //建立两个字节缓冲区  
	            byte[] data1 = new byte[len1];  
	            byte[] data2 = new byte[len2];  
	              
	            //分别将两个文件的内容读入缓冲区  
	            fis1.read(data1);  
	            fis2.read(data2);  
	              
	            //依次比较文件中的每一个字节  
	            for (int i=0; i<len1; i++) {  
	                //只要有一个字节不同，两个文件就不一样  
	                if (data1[i] != data2[i]) {  
	                    return 1;  
	                }  
	            }  
	            return 0;  
	        } 
	        else {  
	            //长度不一样，文件肯定不同  
	        	return 1;  
		    }
	    } 
		catch (FileNotFoundException e) {  
	        e.printStackTrace();  
	    } 
		catch (IOException e) {  
	        e.printStackTrace();  
	    } 
		finally {//关闭文件流，防止内存泄漏  
	        if (fis1 != null) {  
	            try {  
	                fis1.close();  
	            } catch (IOException e) {  
	                //忽略  
	                e.printStackTrace();  
	            }  
	        }  
	        if (fis2 != null) {  
	            try {  
	                fis2.close();  
	            } catch (IOException e) {  
	                //忽略  
	                e.printStackTrace();  
	            }  
	        }  
	    }  
	    return -1;  
	}
	
	/**newCompareFile与compareFile不同，如果两个文件任一个文件不存在，后者都会报错。
	 *   当前的newCompareFile，则考虑到，一个文件存在，另一个不存在，认定为：两个结果不同，测试失败；
	 *                  两个文件都不存在，认定为结果相同。
	 *                         两个文件都存在，继续后面的比较。         
	 * @param faultOutput 有故障的程序，其输出结果
	 * @param okOutput    正确的程序，其输出结果
	 * @return 1，两个结果不同，测试失败； 0 ，两个文件输出结果相同，测试用例通过
	 *         -1，读文件出错
	 */
	public static int newCompareFile(String faultOutput,String okOutput)
	{
		 File faultFile,okFile;
		 boolean faultIsExist = false; //文件faultOutput不存在
		 boolean okIsExist = false;//文件okOutput不存在
		 
		 try {
			 faultFile = new File(faultOutput);
		     if( faultFile.isFile()&& faultFile.exists() )
		    	 faultIsExist = true;
		 }
		 catch (Exception e) {
		        e.printStackTrace();
		        return -1;
		 } 
		 try {
			 okFile = new File(okOutput);
		     if( okFile.isFile()&& okFile.exists() )
		    	 okIsExist = true;
		 }
		 catch (Exception e) {
		        e.printStackTrace();
		        return -1;
		 } 
		 if( (false==faultIsExist)&&(false==okIsExist) )
				 return 0; //两个文件都不存在。
		 else if( (true==faultIsExist)&&(true==okIsExist) )
			 return compareFile(faultOutput,okOutput);
		 else//一个文件存在，另一个不存在，认定为：两个结果不同，测试失败；
			 return 1;
	}//end of newCompareFile
}
