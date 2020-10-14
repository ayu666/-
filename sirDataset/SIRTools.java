/**
 *  SIR 数据集用到的工具。
 */
package sirDataset;

/**
 * @author Administrator
 *
 */
public class SIRTools {
	//统计tcLines里1的个数，也就是通过的测试用例个数。
	public static int getPassedTc(int[] tcLines)
	{
		int passed = 0;
		for( int k=0;k<tcLines.length;k++)
			if( 0==tcLines[k] ) //0=pass
				passed += 1;
		return passed;
	}
	
	//统计tcLines里1的个数，也就是通过的测试用例个数。
	public static int getFailedTc(int[] tcLines)
	{
		int failed = 0;
		for( int k=0;k<tcLines.length;k++)
			if( 1==tcLines[k] ) //1=fail
				failed += 1;
		return failed;
	}
}
