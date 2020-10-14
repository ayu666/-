/**
 * 
 */
package sbflCommon;

/**
 * @author Administrator
 * 评估测度的接口。
 */
public interface IMetricMethod {
	public IMetricResult getResult();
	public String getMetricName();//测度的名字，如T-score,EXAM,Expense
}
