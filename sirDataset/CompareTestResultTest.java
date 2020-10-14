/**
 * 
 */
package sirDataset;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Administrator
 *  ≤‚ ‘ ¿‡CompareTestResult
 */
class CompareTestResultTest {

	CompareTestResult ctr = new CompareTestResult(0,0,"0");
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void compareTwoNullFile()
	{
		String objectName = "print_tokens2";
		String faultFile = ProjectConfiguration.DatasetDirectory +
				SubjectDirectoryMap.getDirectoryFromName(objectName) +"/outputs/v10/t4015";
		String okFile = ProjectConfiguration.DatasetDirectory +
				SubjectDirectoryMap.getDirectoryFromName(objectName) +"/outputs/vok/t4015";
		//TestCase 1.
		assertEquals(1,ctr.jUnitCompareFile(faultFile,okFile),"Two file is not identify.");
		//2
		okFile = ProjectConfiguration.DatasetDirectory +
				SubjectDirectoryMap.getDirectoryFromName(objectName) +"/outputs/v10/t4011";
		assertEquals(0,ctr.jUnitCompareFile(faultFile,okFile),"Two file is identify.");
		//3
		faultFile = ProjectConfiguration.DatasetDirectory +
				SubjectDirectoryMap.getDirectoryFromName(objectName) +"/outputs/v1/t1015";
		okFile = ProjectConfiguration.DatasetDirectory +
				SubjectDirectoryMap.getDirectoryFromName(objectName) +"/outputs/v10/t1015";
		assertNotEquals(1,ctr.jUnitCompareFile(faultFile,okFile),"Two file is identify.");
	}
	
	@Test
	void test() {
		fail("Not yet implemented");
	}

}
