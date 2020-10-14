/**
 * 
 */
package sirDataset;

/**
 * @author Administrator
 *
 */
public class ExcludeVersion {
	//下面两行代码可用于测试。
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
		 * print_tokens 的V4和V6,.C文件相同，错误在.h文件，
		 * 排除。
		 */
		/*
		 * print_tokens2 的V10执行部分测试用例时，发生Segmentation Fault,GCOV无法产生.gcno文件，没有覆盖数据。
		 * 另外，比较文件的方式和SIR matrix_fault的结果也不同。所以排除。
		 */
		/*
		 *  schedule的V1,V5,V6 发生Segmentation Fault,GCOV无法产生.gcno文件，没有覆盖数据。
		 *  并且这些版本都有测试用例比较文件的方式和SIR matrix_fault的结果不同。
		 *  V9发生Segmentation Fault,GCOV无法产生.gcno文件，没有覆盖数据。
		 *   排除这些版本。
		 */
		/*
		 * schedule2的V8发生Segmentation Fault,GCOV无法产生.gcno文件，没有覆盖数据。
		 *  并且该版本有测试用例比较文件的方式和SIR matrix_fault的结果不同。
		 * schedule2的V9没有失败的测试用例。
		 *  排除这些版本。
		 */
		/* replace修改了代码：
		 *    #define NULL0   ===========>  #define TNULL 0
		 *    getline         ===========>  get_line  
		 * replace的V27发生Segmentation Fault,GCOV无法产生.gcno文件，没有覆盖数据。
		 * schedule2的V32没有失败的测试用例。
		 *  排除这些版本。
		 *  V8，V13,V14,V26这四个版本，比较文件的方式和SIR matrix_fault的结果不同。
		 *  这些版本保留，使用我自己的比较结果填入matrix_fault，旧的更名matrix_fault.sir
		 */
		/*
		 * tcas的V38版本有测试用例比较文件的方式和SIR matrix_fault的结果不同。
		 * 原来的matrix_fault里，该版本有0个FAIL，现在我有56个FAIL
		 *  排除该版本。
		 *  另外，V4,V23,V28,V29,V30,V33,V35,V36,V37,V40,V41这些版本，比较文件的方式和SIR matrix_fault的结果不同。
		 *  但它们不同的测试用例结果数很少。
		 *  这些版本保留，使用我自己的比较结果填入matrix_fault，旧的更名matrix_fault.sir
		 */
		/*
		 * tot_info的V21版本故障语句是一个宏定义，牵涉到许多变量，不好制定故障语句的位置。
		 *  排除该版本。
		 *  另外，V11,V15,这些版本，比较文件的方式和SIR matrix_fault的结果不同。
		 *  但它们不同的测试用例结果数很少。
		 *  这些版本保留，使用我自己的比较结果填入matrix_fault，旧的更名matrix_fault.sir
		 */
		/*
		 * space的V1,2,32测试用例全部通过，排除这些版本。
		 *  另外，V34,比较文件的方式和SIR matrix_fault的结果太多不同。排除
		 *  V25,30,35,36,38,这些版本，很可能指针问题，程序奔溃，无法记录gcov。排除这些版本。
		 *  V26，尽管也有check file (noExecutedTimes)的错误，暂且保留。
		 *  v7(2),v9(50),v10(19),v11(21),v16(2),v17(2),v19(19),v20(1),v21(1),v23(1),v28(70),
		 *  它们不同的测试用例结果数相对较少，括号内不同结果的测试用例数。
		 *  这些版本保留，使用我自己的比较结果填入matrix_fault，旧的更名matrix_fault.sir
		 */
		/*
		 * flex(4)的V2版本故障语句和V1是同一个数组组，不能区分。
		 *  排除该版本。
		 *  V3,V4,V9,V10,V11都没有未通过的测试用例，排除这些版本。
		 *  使用我的比较结果覆盖原来的fault-matrix，两者也只有第二个测试用例有少许版本结果不同。
		 */
		/*
		 * flex(2)的  V1,4,8,12,13,17,19,20都没有未通过的测试用例，排除这些版本。
		 *  使用我的比较结果覆盖原来的fault-matrix，两者也只有第6个测试用例有少许版本结果不同。
		 */
		/*
		 * grep(3)的  V4,5,6,7,9,11,13,14,15,17都没有未通过的测试用例，排除这些版本。
		 *  使用我的比较结果覆盖原来的fault-matrix，两者差别：
		 *      V3一个测试用例不同； V13许多测试用例结果不同。
		 */
		/*
		 * grep(2)的  V3,4,5,8都没有未通过的测试用例，排除这些版本。
		 *  使用我的比较结果覆盖原来的fault-matrix，两者完全相同。
		 */
		/*
		 * grep(4)的  V1,3~11都没有未通过的测试用例，排除这些版本。
		 *  使用我的比较结果覆盖原来的fault-matrix，两者完全相同。
		 */
		/*
		 * gzip(1)的  V1,3,6~12都没有未通过的测试用例，排除这些版本。
		 *  v4，我的比较结果与原来的fault-matrix，太多不同，也排除v4.
		 *  使用我的比较结果覆盖原来的fault-matrix，除v5,v15外完全相同。
		 *    v5的tc52, v15的tc38,tc52不同。
		 */
		/*
		 * gzip(2)的  V2,4,5,7都没有未通过的测试用例，排除这些版本。
		 *  使用我的比较结果覆盖原来的fault-matrix，。
		 *    v1的tc38,49,52, v3的tc52不同,v6的tc38,49,52不同。
		 */
		/*
		 * sed(6)的  V2不知道什么原因，评估值超过100%（可能没有列入到可执行语句）。
		 */
		/*
		 * sed(7)的  V2，只有一个失败测试用例，并且tc13,tc17导致没有可执行语句。
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
	
	//计算该对象需要排除的版本数目。
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
