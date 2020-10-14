/**
 * 
 */
package objectFile;


import java.util.Scanner;

import sbflCommon.FaultFile;
import sbflCommon.TestcaseFile;
import sirDataset.CompareTestResult;
import sirDataset.FaultMatrix;
import sirDataset.GcovCoverage;
import sirDataset.ProfileFile;
import sirDataset.SubjectDirectoryMap;
import sirNewDataset.NewCompareTestResult;
import sirNewDataset.NewFaultMatrix;
import sirNewDataset.NewGcovCoverage;

/**
 * @author Administrator
 *   执行SIR 新格式的dataset的文件操作。
 *      flex,gzip,grep,...
 */
public class CheckNewSIRObject {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String objectName = "sed"; //flex,gzip,grep,sed

		// TODO Auto-generated method stub
		System.out.println("1, new (a)read SIR file fault-matrix and \n"
				          + "      (b)Generate new fault-matrix ");
    	System.out.println("2, new check object name dir map is ok.");
    	System.out.println("3, new (a)check read file my fault-matrix and \n"
				          + "      (b)generate .testcase file.");
    	System.out.println("4, new check read file (object).fault is ok.");
    	System.out.println("5, Compare my fault-matrix and old fault-matrix(fault-matrix-XXX-???).");
    	System.out.println("6, new check read .gcov file and generate .profile file.");
    	System.out.println("7, new check read .profile file.");
    	System.out.println("8, new check read file (object).testcase is ok.");
    	
    	System.out.println("\r\n Others, exit............ .");
    	System.out.println("Please key your choice.");
    	Scanner sc=new Scanner(System.in);
    	int choice = sc.nextInt();
    	sc.close();
    	switch( choice )
    	{
    	case 1:
    		NewFaultMatrix matrixFile = new NewFaultMatrix(objectName);
    		matrixFile.readSIRfaultMatrix();
    		int verno = matrixFile.getVerNo();
    		int tcno = matrixFile.getTesecaseNo();
    		int tc2index = matrixFile.getTc2Index();
    		NewCompareTestResult mctr = new NewCompareTestResult(verno,tcno,objectName,tc2index);
    		mctr.readFile();
    		mctr.writeMyFaultMatrix();//比较的结果存入fault-matrix
    		mctr.testMe();
    		break;
    	case 2:
    		String strDir = SubjectDirectoryMap.getDirectoryFromName(objectName);
    		System.out.println(strDir);
    		break;
    	case 3://只有第一步完成，才可进行到此步骤。
    		NewFaultMatrix faultMatrix = new NewFaultMatrix(objectName);
    		boolean result = faultMatrix.readFaultMatrix();
    		faultMatrix.testMe();
    		if( true==result )
    			faultMatrix.writeTestCaseFile();
    		break;
    	case 4:
    		FaultFile faultFile = new FaultFile(objectName);
    		faultFile.readFaultFile();
    		faultFile.testMe();
    		break;
    	case 5://只有第一步完成，才可进行到此步骤。 
    		NewFaultMatrix cmpFaultMatrix = new NewFaultMatrix(objectName);
    		cmpFaultMatrix.compareFaultMatrixSIR();
    		break;
    	case 6:
    		//第一步通过后，才能执行此步骤。
    		NewFaultMatrix nfm = new NewFaultMatrix(objectName);
    		nfm.readFaultMatrix();
    		int vernum = nfm.getVerNo();
    		int tcnum = nfm.getTesecaseNo();
    		int tcindex = nfm.getTc2Index();
    		NewGcovCoverage gcov = new NewGcovCoverage(vernum,tcnum,tcindex,nfm.getTestcaseMatrixs(),objectName);
    		gcov.readGcovGenProfile();
    		//gcov.testMe();
    		break;
    	case 7:
    		ProfileFile pf = new ProfileFile(4,objectName);//只能逐个版本测试。
    		pf.readProfileFile();
    		pf.testMe();
    		break;
    	case 8:
    		TestcaseFile tcFile = new TestcaseFile(objectName);
    		tcFile.readTestcaseFile();
    		tcFile.testMe();
    		break;
    	default:
    	}//end of switch
    	System.out.println("The task about (####"+objectName+"####)is over.");
	}

}
