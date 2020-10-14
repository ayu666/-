/**
 * 
 */
package objectFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

import sbflCommon.FaultFile;
import sbflCommon.TestcaseFile;
import sirDataset.CompareTestResult;
import sirDataset.FaultMatrix;
import sirDataset.GcovCoverage;
import sirDataset.SubjectDirectoryMap;
import sirDataset.ProfileFile;
import sirDataset.ProjectConfiguration;

/**
 * @author Administrator
 *
 */
public class CheckSIRObjectFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
    	String objectName = "space"; //print_tokens  print_tokens2  schedule schedule2 replace tcas tot_info

		// TODO Auto-generated method stub
		System.out.println("1, check read file fault-matrix and generate .testcase file.");
    	System.out.println("2, check object name dir map is ok.");
    	System.out.println("3, check read file (object).fault is ok.");
    	System.out.println("4, check vfault and vok output is same.");
    	System.out.println("5, check read .gcov file and generate .profile file.");
    	System.out.println("6, check read .profile file.");
    	System.out.println("7, change .profile to .txt file.");
    	System.out.println("8, check read file (object).testcase is ok.");
    	System.out.println("9, Generate new fault-matrix and rename old fault-matrix.");
    	System.out.println("10, Compare  new fault-matrix and old fault-matrix(fault-matrix.sir).");
    	System.out.println("11, Read fault-matrix and check my program.");//�ҵĳ����������C���Բ�ͬ����
    	
    	System.out.println("\r\n Others, exit............ .");
    	System.out.println("Please key your choice.");
    	Scanner sc=new Scanner(System.in);
    	int choice = sc.nextInt();
    	sc.close();
    	switch( choice )
    	{
    	case 1:
    		FaultMatrix matrixFile = new FaultMatrix(objectName);
    		boolean result = matrixFile.readTcMatrixFile();
    		if( true==result )
    			matrixFile.writeTestCaseFile();
    		matrixFile.testMe();
    		break;
    	case 2:
    		String strDir = SubjectDirectoryMap.getDirectoryFromName(objectName);
    		System.out.println(strDir);
    		break;
    	case 3:
    		FaultFile faultFile = new FaultFile(objectName);
    		faultFile.readFaultFile();
    		faultFile.testMe();
    		break;
    	case 4:
    		//��һ��ͨ����Ҳ����FaultMatrixͨ���󣬲���ִ�д˲��衣
    		FaultMatrix mf = new FaultMatrix(objectName);
    		mf.readTcMatrixFile();
    		int verno = mf.getVerNo();
    		int tcno = mf.getTesecaseNo();
    		CompareTestResult ctr = new CompareTestResult(verno,tcno,objectName);
    		ctr.readFile();
    		ctr.testMe();
    		ctr.checkPassFail(mf.getTestcaseMatrixs());
    		break;
    	case 5: //��һ��ͨ����Ҳ����FaultMatrixͨ���󣬲���ִ�д˲��衣
    		FaultMatrix fm = new FaultMatrix(objectName);
    		fm.readTcMatrixFile();
    		int vernum = fm.getVerNo();
    		int tcnum = fm.getTesecaseNo();
    		GcovCoverage gcov = new GcovCoverage(vernum,tcnum,fm.getTestcaseMatrixs(),objectName);
    		gcov.readGcovGenProfile();
    		//gcov.testMe();
    		break;
    	case 6:
    		ProfileFile pf = new ProfileFile(5,objectName);//ֻ������汾���ԡ�
    		pf.readProfileFile();
    		pf.testMe();
    		break;
    	case 7:
    		ProfileFile pftxt = new ProfileFile(1,objectName);//ֻ������汾���ԡ�
    		pftxt.readProfileFile();
    		pftxt.writeToTXT();//�ı���ʽ��š�
    		break;
    	case 8:
    		TestcaseFile tcFile = new TestcaseFile(objectName);
    		tcFile.readTestcaseFile();
    		tcFile.testMe();
    		break;
    	case 9:
    		//�ȶ�.testcase����Ҫ�汾���Ͳ���������
    		TestcaseFile tcInf = new TestcaseFile(objectName);
    		tcInf.readTestcaseFile();
    		int vers = tcInf.getVerNo();
    		int tcs = tcInf.getTesecaseNo();
    		CompareTestResult ctResult = new CompareTestResult(vers,tcs,objectName);
    		ctResult.readFile();
    		ctResult.replaceSIRfaultMatrix();
    		break;
    	case 10: //��9��ͨ����Ҳ���Ǵ����ļ�FaultMatrix.sir������ִ�д˲��衣
    		FaultMatrix fMatrix = new FaultMatrix(objectName);
    		fMatrix.compareFaultMatrixSIR();
    		break;	
    	case 11:
    		checkMyReadMatrixFault();//�ҵĳ����������C���Բ�ͬ��..testcase�ļ�����в��
    		break;
    	default:
    	}//end of switch
    	System.out.println("The task about (####"+objectName+"####)is over.");
	}//end of main

	//�ҵĳ����������C���Բ�ͬ��..testcase�ļ�����в��
	private static void checkMyReadMatrixFault()
	{
		String objectName = "replace";
		String matrixFilename = ProjectConfiguration.DatasetDirectory +
				SubjectDirectoryMap.getDirectoryFromName(objectName) +"/info/fault-matrix";
		int verNo,tcNo;
		int verCheck = 14;//Ҫ���˰汾�����⡣
		int count = 0; //�ð汾�ܵ�ʧ�ܲ�������������
		try {
			File file = new File(matrixFilename);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file));
				BufferedReader br = new BufferedReader(read);
				String lineTXT = null;
				lineTXT = br.readLine(); //Version total
				lineTXT = lineTXT.trim(); //ȥ����β�ո�
				String[]  strAry = lineTXT.split("\\s+"); //�������ո�ָ��ַ���
				verNo = Integer.valueOf(strAry[0]); //�汾��
				
				lineTXT = br.readLine();  //test case total
				lineTXT = lineTXT.trim(); //ȥ����β�ո�
				strAry = lineTXT.split("\\s+");
				tcNo = Integer.valueOf(strAry[0]); //����������Ŀ
				
				for( int i=0;i<tcNo;i++ ) //���� nTcs �У��ǲ�������������
					br.readLine(); 
				
				//���ж���
				for( int i=0;i<tcNo;i++ )
				{
					lineTXT = br.readLine();  //unitestXXX,from 0 start
					lineTXT = lineTXT.substring(7, lineTXT.length()-1);//"unitest"=7,���һ����":");
					int tcth = Integer.valueOf(lineTXT);
					for( int j=0;j<verNo;j++ )
					{
						lineTXT = br.readLine();  //vXXX,from 1 start
						lineTXT = lineTXT.substring(1, lineTXT.length()-1);//"v"=1,���һ����":");
						int vth = Integer.valueOf(lineTXT);
						lineTXT = br.readLine();  //���Խ��,0 or 1
						String strTmp = lineTXT.trim(); //ȥ����β�ո�
						int tcoutcome = Integer.valueOf(strTmp);
						if( vth==verCheck )
							System.out.println(lineTXT);
						if( vth==verCheck && tcoutcome==1 )
						{
							System.out.println("vno="+String.valueOf(vth)+",tcno="+String.valueOf(tcth)+
									"   text=   "+lineTXT);
							count++;
						}
					}
				}//end of for( int i
				read.close();
			}
		}//end of try. 
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Fail testcase number :=  "+String.valueOf(count));
	}//end of checkMyReadMatrixFault
}
