/**
 * 
 */
package sbflCommon;

/**
 * @author Administrator
 * 传统的SBFL技术
 */
public class SBFLTradTechnique {
	private static String[] algorithmNames = {"Optimal","Opass","Kulczynski2","Wong3",
							"Tarantula","Jaccard","Ochiai","RusselRao"};
	
	public static String[] getAlgorithmNames()
	{
		return algorithmNames;
	}
	/**Calculate  Suspicious 
	 * @param strAlgorithm SBFL算法名称  
	 * @param stProfile  程序谱。
	 * @return
	 */
	public static double algorithmSuspicious(String strAlgorithm,ProfileStatement stProfile)
	{
		double pSuspi = 0;
		//"Optimal","Opass","Kulczynski2","Wong3","Tarantula","Jaccard","Ochiai","RusselRao"
		if(  strAlgorithm.equalsIgnoreCase("Optimal") )
			pSuspi = optimalSuspicious(stProfile);
		else if(  strAlgorithm.equalsIgnoreCase("Opass") )
			pSuspi = opassSuspicious(stProfile);
		else if(  strAlgorithm.equalsIgnoreCase("Kulczynski2") )
			pSuspi = kulczynski2Suspicious(stProfile);
		else if(  strAlgorithm.equalsIgnoreCase("Wong3") )
			pSuspi = wong3Suspicious(stProfile);
		else if(  strAlgorithm.equalsIgnoreCase("Tarantula") )
			pSuspi = tarantulaSuspicious(stProfile);
		else if(  strAlgorithm.equalsIgnoreCase("Jaccard") )
			pSuspi = jaccardSuspicious(stProfile);
		else if(  strAlgorithm.equalsIgnoreCase("Ochiai") )//Ochiai
			pSuspi = ochiaiSuspicious(stProfile);
		else//"RusselRao"
			pSuspi = russelRaoSuspicious(stProfile);
		return pSuspi;
	}
	//Calculate Optimal Suspicious 
	private static double optimalSuspicious(ProfileStatement stProfile)
	{
		//totalFailed & totalPassed don't be 0.
		int totalFailed = stProfile.Aef+stProfile.Anf;
		if( stProfile.Aef<totalFailed )
			return -1.0;
		else
			return (double)stProfile.Anp;
	}

	//Calculate OptimalPass(Naish et al.2011) Suspicious 
	private static double opassSuspicious(ProfileStatement stProfile)
	{
		//totalFailed & totalPassed don't be 0.
		int totalPassed = stProfile.Aep+stProfile.Anp;
		double fitem = (double)stProfile.Aep/(totalPassed+1.0);
		return stProfile.Aef-fitem;
	}

	//Calculate Kulczynski2 Suspicious
	private static double kulczynski2Suspicious(ProfileStatement stProfile) 
	{
		if( 0==stProfile.Aef )
			return 0.0;
		//totalFailed & totalPassed don't be 0.
		int totalFailed = stProfile.Aef+stProfile.Anf;
		int totalCoverage = stProfile.Aep+stProfile.Aef;
		double f1 = (double)stProfile.Aef/totalFailed;
		double f2 = (double)stProfile.Aef/totalCoverage;
		return 0.5*(f1+f2);
	}

	//Calculate Wong3 Suspicious 
	private static double wong3Suspicious(ProfileStatement stProfile)
	{
	//Wong1: stProfile.Aef
		//Wong3 ,first calculation h
		double h = 0;
		if ( stProfile.Aep<=2 )//0.1.2
			h = stProfile.Aep;
		else if( stProfile.Aep<=10 )
			h = 2+0.1*(stProfile.Aep-2);
		else //stProfile.Aep>10
			h = 2.8+0.001*(stProfile.Aep-10);
		return stProfile.Aef-h;
	}

	//Calculate Tarantula Suspicious 
	private static double tarantulaSuspicious(ProfileStatement stProfile)
	{
		if( 0==stProfile.Aef )
			return 0.0;
		//totalFailed & totalPassed don't be 0.
		int totalFailed = stProfile.Aef+stProfile.Anf;
		int totalPassed = stProfile.Aep+stProfile.Anp;
		double fz = (double)stProfile.Aef/totalFailed;
		return fz/(fz+(double)stProfile.Aep/totalPassed);
	}

	//Calculate Jaccard Suspicious 
	private static double jaccardSuspicious(ProfileStatement stProfile)
	{
		//totalFailed & totalPassed don't be 0.
		int totalFailed = stProfile.Aef+stProfile.Anf;
		double fm = (double)(stProfile.Aep+totalFailed);
		return (double)stProfile.Aef/fm;
	}

	//Calculate Ochiai Suspicious 
	private static double ochiaiSuspicious(ProfileStatement stProfile)
	{
		if( 0==stProfile.Aef )
			return 0.0;
		//totalFailed & totalPassed don't be 0.
		int totalFailed = stProfile.Aef+stProfile.Anf;
		int totalCoverage = stProfile.Aep+stProfile.Aef;
		return (double)stProfile.Aef/Math.sqrt(totalFailed*totalCoverage);
	}

	//Calculate RusselRao Suspicious 
	private static double russelRaoSuspicious(ProfileStatement stProfile)
	{
		//totalFailed & totalPassed don't be 0.
		int totalFailed = stProfile.Aef+stProfile.Anf;
		int totalPassed = stProfile.Aep+stProfile.Anp;
		return (double)stProfile.Aef/(totalFailed+totalPassed);
	}
}
