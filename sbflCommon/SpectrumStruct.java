/**
 * 
 */
package sbflCommon;

/**
 * @author Administrator
 * ����List���������Ա�д������һ���࣬�ṹһ��ʹ�á�
 */
public class SpectrumStruct {
	private int lineno; //��ִ�������к�
	private int aep;  //���Ǹ���䣬�ɹ��Ĳ�������������
	private int aef;  //���Ǹ���䣬ʧ�ܵĲ�������������
	
	public SpectrumStruct(int lineno,int aep,int aef)
	{
		this.lineno = lineno;
		this.aep = aep;
		this.aef = aef;
	}
	
	public int getLineNo()
	{
		return lineno;
	}
			
	public int getAep()
	{
		return aep;
	}
	
	public int getAef()
	{
		return aef;
	}
}
