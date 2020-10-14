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
 *
 */
class ExcludeVersionTest {

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
	void test() {
		fail("Not yet implemented");
	}

	@Test
	void testExclude() {
		//private static String[] objectDts={"schedule2","schedule","print_tokens"};
		//private static int[][] verDts = {{9},{1,3},{2,5,6}};
		//假如ExcludeVersion里的初始化情况如上。
		boolean r1 = ExcludeVersion.isExcludeVer("schedule2", 9);
		System.out.println(r1);
		r1 = ExcludeVersion.isExcludeVer("schedule2", 7);
		System.out.println(r1);
		r1 = ExcludeVersion.isExcludeVer("schedule", 7);
		System.out.println(r1);
		r1 = ExcludeVersion.isExcludeVer("schedule",3);
		System.out.println(r1);
		r1 = ExcludeVersion.isExcludeVer("print_tokens", 5);
		System.out.println(r1);
		r1 = ExcludeVersion.isExcludeVer("print_tokens",3);
		System.out.println(r1);
	}
	
	@Test
	void testExcludeNumber() {
		//private static String[] objectDts={"schedule2","schedule","print_tokens"};
		//private static int[][] verDts = {{9},{1,3},{2,5,6}};
		//假如ExcludeVersion里的初始化情况如上。
		int n = ExcludeVersion.getNumberOfExcludeVer("schedule2");
		System.out.println(n);
		n = ExcludeVersion.getNumberOfExcludeVer("print_tokens");
		System.out.println(n);
		n = ExcludeVersion.getNumberOfExcludeVer("schedule");
		System.out.println(n);
		n = ExcludeVersion.getNumberOfExcludeVer("print_tokens2");
		System.out.println(n);
	}
}
