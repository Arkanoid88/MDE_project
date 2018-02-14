package it.univaq.disim.mde.tempmain.MavenManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import it.univaq.disim.mde.tempmain.json.DataObject;

import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Maven_component {
	
	// get file java that executes a certain MTL
	public String getJavaList(String path) throws IOException, XmlPullParserException{
			
			File file = new File(path);
			
			String JavaFiles_list = new String();
			
			Reader reader = new FileReader(file);
			try {
			    MavenXpp3Reader xpp3Reader = new MavenXpp3Reader();
			    Model model = xpp3Reader.read(reader);
			    System.out.println(model.getBuild().getPlugins());
			    
			    for(Object node : model.getBuild().getPlugins())
			    {
			    	Plugin p = (Plugin)node;
			    	if(p.getArtifactId().equals("exec-maven-plugin"))
			    	{
//			    		System.out.println(p.getArtifactId());
//				    	System.out.println(p.getConfiguration().toString());
				    	String configuration = p.getConfiguration().toString();
				    	String complete_domain = configuration.substring(configuration.indexOf("mainClass") + 10 , configuration.indexOf("/mainClass") - 1);
				    	//System.out.println(configuration.substring(configuration.indexOf("mainClass") + 10 , configuration.indexOf("/mainClass") - 1));
				    	
				    	System.out.println(complete_domain.substring(complete_domain.lastIndexOf(".") + 1).concat(".java"));
				    	String linked_file = complete_domain.substring(complete_domain.lastIndexOf(".") + 1).concat(".java");
				    	JavaFiles_list = linked_file;
			    	}
			    }	
			    	
			} finally {
			    reader.close();
			}
			
			return JavaFiles_list;
		}
	
}

