/**
 * ����object�����ֺ�Ŀ¼��һ�£��صش������࣬��֮ӳ�䡣
 */
package sirDataset;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 *
 */
public final class SubjectDirectoryMap {
	private static Map<String,String> dirObject = new HashMap<String,String>();
	static //��̬�����
	{
		//key-value: ������������·��
		dirObject.put("print_tokens","printtokens"); 
		dirObject.put("print_tokens2","printtokens2"); 
		dirObject.put("replace","replace"); 
		dirObject.put("schedule","schedule"); 
		dirObject.put("schedule2","schedule2"); 
		dirObject.put("tcas","tcas"); 
		dirObject.put("tot_info","totinfo"); 
		
		dirObject.put("space","space");
		//SIR �¸�ʽ�����ݼ�
		//flex
		dirObject.put("flexV4","flexV4"); 
		dirObject.put("flexV2","flexV2"); 
		dirObject.put("flex","flex");
		//grep
		dirObject.put("grepV2","grepV2"); 
		dirObject.put("grepV3","grepV3"); 
		dirObject.put("grepV4","grepV4"); 
		dirObject.put("grep","grep"); 
		//gzip
		dirObject.put("gzipV1","gzipV1"); 
		dirObject.put("gzipV2","gzipV2"); 
		dirObject.put("gzip","gzip");
		//sed
		dirObject.put("sed","sed");
		dirObject.put("sedV3","sedV3");
		dirObject.put("sedV5","sedV5");
		dirObject.put("sedV6","sedV6");
		dirObject.put("sedV7","sedV7");
	}
	
	/*
	 * ���캯������
	 */
	private SubjectDirectoryMap()
	{
	}
	
	
	
	public static String getDirectoryFromName(String objectName)
	{
		return dirObject.get(objectName);
	}
}
