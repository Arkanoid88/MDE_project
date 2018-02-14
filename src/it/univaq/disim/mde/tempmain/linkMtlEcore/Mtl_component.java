package it.univaq.disim.mde.tempmain.linkMtlEcore;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.*;

public class Mtl_component {
	
	
	public String get_Ecore(String path) throws FileNotFoundException{
		
		File file = new File(path);
		Scanner inputFile = new Scanner(file);
		String ecore = null;
		while (inputFile.hasNext())
	      {
	        String familyName = inputFile.nextLine();
	        
	        if(familyName.contains("module generate"))
			{	
	        	  String resultString = familyName.replaceFirst("\\[\\s+ ", "\\[");
		          resultString = resultString.replaceFirst("\\s+\\]", "\\]");
		        
		          Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(resultString);
		          while(m.find()) {
		              ecore = m.group(1);
		              ecore = ecore.replace("'", "");
		          }
	        	  
//				  System.out.println(familyName);
//			      System.out.println("\n\n\nseparator\n\n\n");
//			      System.out.println(resultString);
//			      System.out.println("\n\n\nseparator\n\n\n");
			}


	      }
			return ecore;
	}

}
