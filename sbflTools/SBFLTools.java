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
 * SBFL��ؼ������������ߡ�
 */
public class SBFLTools {
	/** Check nStatement is or not fault code line.
	 * @param nStatement ��������䣬���к�
	 * @param failStatms ����кż���
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
	 * @param faultOutput �й��ϵĳ�����������
	 * @param okOutput    ��ȷ�ĳ�����������
	 * @return 1�����������ͬ������ʧ�ܣ� 0 �������ļ���������ͬ����������ͨ��
	 *         -1�����ļ�����
	 */
	public static int compareFile(String faultOutput,String okOutput)
	{
		FileInputStream fis1 = null;  
	    FileInputStream fis2 = null;  
		try {  
	        fis1 = new FileInputStream(faultOutput);  
	        fis2 = new FileInputStream(okOutput);  
		          
	        int len1 = fis1.available();//�����ܵ��ֽ���  
	        int len2 = fis2.available();  
		          
	        if (len1 == len2) {//������ͬ����ȽϾ�������  
	            //���������ֽڻ�����  
	            byte[] data1 = new byte[len1];  
	            byte[] data2 = new byte[len2];  
	              
	            //�ֱ������ļ������ݶ��뻺����  
	            fis1.read(data1);  
	            fis2.read(data2);  
	              
	            //���αȽ��ļ��е�ÿһ���ֽ�  
	            for (int i=0; i<len1; i++) {  
	                //ֻҪ��һ���ֽڲ�ͬ�������ļ��Ͳ�һ��  
	                if (data1[i] != data2[i]) {  
	                    return 1;  
	                }  
	            }  
	            return 0;  
	        } 
	        else {  
	            //���Ȳ�һ�����ļ��϶���ͬ  
	        	return 1;  
		    }
	    } 
		catch (FileNotFoundException e) {  
	        e.printStackTrace();  
	    } 
		catch (IOException e) {  
	        e.printStackTrace();  
	    } 
		finally {//�ر��ļ�������ֹ�ڴ�й©  
	        if (fis1 != null) {  
	            try {  
	                fis1.close();  
	            } catch (IOException e) {  
	                //����  
	                e.printStackTrace();  
	            }  
	        }  
	        if (fis2 != null) {  
	            try {  
	                fis2.close();  
	            } catch (IOException e) {  
	                //����  
	                e.printStackTrace();  
	            }  
	        }  
	    }  
	    return -1;  
	}
	
	/**newCompareFile��compareFile��ͬ����������ļ���һ���ļ������ڣ����߶��ᱨ��
	 *   ��ǰ��newCompareFile�����ǵ���һ���ļ����ڣ���һ�������ڣ��϶�Ϊ�����������ͬ������ʧ�ܣ�
	 *                  �����ļ��������ڣ��϶�Ϊ�����ͬ��
	 *                         �����ļ������ڣ���������ıȽϡ�         
	 * @param faultOutput �й��ϵĳ�����������
	 * @param okOutput    ��ȷ�ĳ�����������
	 * @return 1�����������ͬ������ʧ�ܣ� 0 �������ļ���������ͬ����������ͨ��
	 *         -1�����ļ�����
	 */
	public static int newCompareFile(String faultOutput,String okOutput)
	{
		 File faultFile,okFile;
		 boolean faultIsExist = false; //�ļ�faultOutput������
		 boolean okIsExist = false;//�ļ�okOutput������
		 
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
				 return 0; //�����ļ��������ڡ�
		 else if( (true==faultIsExist)&&(true==okIsExist) )
			 return compareFile(faultOutput,okOutput);
		 else//һ���ļ����ڣ���һ�������ڣ��϶�Ϊ�����������ͬ������ʧ�ܣ�
			 return 1;
	}//end of newCompareFile
}
