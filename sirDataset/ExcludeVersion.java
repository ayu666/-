/**
 * 
 */
package sirDataset;

/**
 * @author Administrator
 *
 */
public class ExcludeVersion {
	//�������д�������ڲ��ԡ�
	//private static String[] objectDts={"schedule2","schedule","print_tokens"};
	//private static int[][] verDts = {{9},{1,3},{2,5,6}};
	private static String[] objectDts={"print_tokens","print_tokens2",
			                           "schedule",   "schedule2",
			                           "replace","tcas","tot_info",
			                           "space",
			                           "flexV4","flexV2",
			                           "grepV3","grepV2","grepV4",
			                           "gzipV1","gzipV2",
			                           "sedV6","sedV7"
			                           };
	private static int[][] verDts = { {4,6},{10},
			                          {1,5,6,9},{8,9},
			                          {27,32},{38},{21},
			                          {1,2,25,30,32,34,35,36,38},
			                           {2,3,4,9,10,11}, {1,4,8,12,13,17,19,20},
			                           {4,5,6,7,9,11,13,14,15,17},{3,4,5,8},{1,3,4,5,6,7,8,9,10,11},
			                           {1,3,4,6,7,8,9,10,11,12},{2,4,5,7},
			                           {2},{2}
			                           };
	
	private void promptTips()
	{
		/*
		 * print_tokens ��V4��V6,.C�ļ���ͬ��������.h�ļ���
		 * �ų���
		 */
		/*
		 * print_tokens2 ��V10ִ�в��ֲ�������ʱ������Segmentation Fault,GCOV�޷�����.gcno�ļ���û�и������ݡ�
		 * ���⣬�Ƚ��ļ��ķ�ʽ��SIR matrix_fault�Ľ��Ҳ��ͬ�������ų���
		 */
		/*
		 *  schedule��V1,V5,V6 ����Segmentation Fault,GCOV�޷�����.gcno�ļ���û�и������ݡ�
		 *  ������Щ�汾���в��������Ƚ��ļ��ķ�ʽ��SIR matrix_fault�Ľ����ͬ��
		 *  V9����Segmentation Fault,GCOV�޷�����.gcno�ļ���û�и������ݡ�
		 *   �ų���Щ�汾��
		 */
		/*
		 * schedule2��V8����Segmentation Fault,GCOV�޷�����.gcno�ļ���û�и������ݡ�
		 *  ���Ҹð汾�в��������Ƚ��ļ��ķ�ʽ��SIR matrix_fault�Ľ����ͬ��
		 * schedule2��V9û��ʧ�ܵĲ���������
		 *  �ų���Щ�汾��
		 */
		/* replace�޸��˴��룺
		 *    #define NULL0   ===========>  #define TNULL 0
		 *    getline         ===========>  get_line  
		 * replace��V27����Segmentation Fault,GCOV�޷�����.gcno�ļ���û�и������ݡ�
		 * schedule2��V32û��ʧ�ܵĲ���������
		 *  �ų���Щ�汾��
		 *  V8��V13,V14,V26���ĸ��汾���Ƚ��ļ��ķ�ʽ��SIR matrix_fault�Ľ����ͬ��
		 *  ��Щ�汾������ʹ�����Լ��ıȽϽ������matrix_fault���ɵĸ���matrix_fault.sir
		 */
		/*
		 * tcas��V38�汾�в��������Ƚ��ļ��ķ�ʽ��SIR matrix_fault�Ľ����ͬ��
		 * ԭ����matrix_fault��ð汾��0��FAIL����������56��FAIL
		 *  �ų��ð汾��
		 *  ���⣬V4,V23,V28,V29,V30,V33,V35,V36,V37,V40,V41��Щ�汾���Ƚ��ļ��ķ�ʽ��SIR matrix_fault�Ľ����ͬ��
		 *  �����ǲ�ͬ�Ĳ���������������١�
		 *  ��Щ�汾������ʹ�����Լ��ıȽϽ������matrix_fault���ɵĸ���matrix_fault.sir
		 */
		/*
		 * tot_info��V21�汾���������һ���궨�壬ǣ�浽�������������ƶ���������λ�á�
		 *  �ų��ð汾��
		 *  ���⣬V11,V15,��Щ�汾���Ƚ��ļ��ķ�ʽ��SIR matrix_fault�Ľ����ͬ��
		 *  �����ǲ�ͬ�Ĳ���������������١�
		 *  ��Щ�汾������ʹ�����Լ��ıȽϽ������matrix_fault���ɵĸ���matrix_fault.sir
		 */
		/*
		 * space��V1,2,32��������ȫ��ͨ�����ų���Щ�汾��
		 *  ���⣬V34,�Ƚ��ļ��ķ�ʽ��SIR matrix_fault�Ľ��̫�಻ͬ���ų�
		 *  V25,30,35,36,38,��Щ�汾���ܿ���ָ�����⣬���������޷���¼gcov���ų���Щ�汾��
		 *  V26������Ҳ��check file (noExecutedTimes)�Ĵ������ұ�����
		 *  v7(2),v9(50),v10(19),v11(21),v16(2),v17(2),v19(19),v20(1),v21(1),v23(1),v28(70),
		 *  ���ǲ�ͬ�Ĳ��������������Խ��٣������ڲ�ͬ����Ĳ�����������
		 *  ��Щ�汾������ʹ�����Լ��ıȽϽ������matrix_fault���ɵĸ���matrix_fault.sir
		 */
		/*
		 * flex(4)��V2�汾��������V1��ͬһ�������飬�������֡�
		 *  �ų��ð汾��
		 *  V3,V4,V9,V10,V11��û��δͨ���Ĳ����������ų���Щ�汾��
		 *  ʹ���ҵıȽϽ������ԭ����fault-matrix������Ҳֻ�еڶ�����������������汾�����ͬ��
		 */
		/*
		 * flex(2)��  V1,4,8,12,13,17,19,20��û��δͨ���Ĳ����������ų���Щ�汾��
		 *  ʹ���ҵıȽϽ������ԭ����fault-matrix������Ҳֻ�е�6����������������汾�����ͬ��
		 */
		/*
		 * grep(3)��  V4,5,6,7,9,11,13,14,15,17��û��δͨ���Ĳ����������ų���Щ�汾��
		 *  ʹ���ҵıȽϽ������ԭ����fault-matrix�����߲��
		 *      V3һ������������ͬ�� V13���������������ͬ��
		 */
		/*
		 * grep(2)��  V3,4,5,8��û��δͨ���Ĳ����������ų���Щ�汾��
		 *  ʹ���ҵıȽϽ������ԭ����fault-matrix��������ȫ��ͬ��
		 */
		/*
		 * grep(4)��  V1,3~11��û��δͨ���Ĳ����������ų���Щ�汾��
		 *  ʹ���ҵıȽϽ������ԭ����fault-matrix��������ȫ��ͬ��
		 */
		/*
		 * gzip(1)��  V1,3,6~12��û��δͨ���Ĳ����������ų���Щ�汾��
		 *  v4���ҵıȽϽ����ԭ����fault-matrix��̫�಻ͬ��Ҳ�ų�v4.
		 *  ʹ���ҵıȽϽ������ԭ����fault-matrix����v5,v15����ȫ��ͬ��
		 *    v5��tc52, v15��tc38,tc52��ͬ��
		 */
		/*
		 * gzip(2)��  V2,4,5,7��û��δͨ���Ĳ����������ų���Щ�汾��
		 *  ʹ���ҵıȽϽ������ԭ����fault-matrix����
		 *    v1��tc38,49,52, v3��tc52��ͬ,v6��tc38,49,52��ͬ��
		 */
		/*
		 * sed(6)��  V2��֪��ʲôԭ������ֵ����100%������û�����뵽��ִ����䣩��
		 */
		/*
		 * sed(7)��  V2��ֻ��һ��ʧ�ܲ�������������tc13,tc17����û�п�ִ����䡣
		 */
	}
	
	/**
	 * @param objectName dataset object name
	 * @param ver version th
	 * @return true: this version will exclude.
	 */
	public static boolean isExcludeVer(String objectName,int ver)
	{
		boolean result = false;
		int index = 0;
		for( String strObject : objectDts )
		{
			index++;
			if( false==strObject.equalsIgnoreCase(objectName))
				continue;
			for( int vItem : verDts[index-1] )
			{
				if( vItem==ver )
				{
					result = true;
					break;
				}
			}
		}//end of for(String
		return result;
	}
	
	//����ö�����Ҫ�ų��İ汾��Ŀ��
	public static int getNumberOfExcludeVer(String objectName)
	{
		boolean result = false;
		int index = 0;
		for( String strObject : objectDts )
		{
			if( true==strObject.equalsIgnoreCase(objectName))
			{
				result = true;
				break;
			}
			index++;
		}//end of for(String
		if( true==result )
			return verDts[index].length;
		else
			return 0;
	}
}
