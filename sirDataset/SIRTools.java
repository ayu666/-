/**
 *  SIR ���ݼ��õ��Ĺ��ߡ�
 */
package sirDataset;

/**
 * @author Administrator
 *
 */
public class SIRTools {
	//ͳ��tcLines��1�ĸ�����Ҳ����ͨ���Ĳ�������������
	public static int getPassedTc(int[] tcLines)
	{
		int passed = 0;
		for( int k=0;k<tcLines.length;k++)
			if( 0==tcLines[k] ) //0=pass
				passed += 1;
		return passed;
	}
	
	//ͳ��tcLines��1�ĸ�����Ҳ����ͨ���Ĳ�������������
	public static int getFailedTc(int[] tcLines)
	{
		int failed = 0;
		for( int k=0;k<tcLines.length;k++)
			if( 1==tcLines[k] ) //1=fail
				failed += 1;
		return failed;
	}
}
