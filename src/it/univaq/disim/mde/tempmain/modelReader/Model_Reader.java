package it.univaq.disim.mde.tempmain.modelReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Model_Reader {

	public ArrayList<ArrayList<String>> parse_model(File file) throws ParserConfigurationException, SAXException, IOException 
	{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        InputStream inputStream = new FileInputStream(file);
        
        Document document = builder.parse(inputStream);
        document.getDocumentElement().normalize();
        System.out.println("Root element :" + document.getDocumentElement().getNodeName());
        
		NodeList UsersList = document.getElementsByTagName("users");
		
		// lista di liste : [[utente,repo],[utente,repo]]
		ArrayList<ArrayList<String>> listOfLists = new ArrayList<ArrayList<String>>();
		
		for(int i=0; i<UsersList.getLength(); i++)
		{	
			Node nNode = UsersList.item(i);
			Element eElement = (Element) nNode;
			
			NodeList ReposList = eElement.getElementsByTagName("repositories");
			//System.out.println("user name:" + eElement.getAttribute("name"));
			
			// lista di 2 elementi, utente-repository
			ArrayList<String> singleList = new ArrayList<String>();
			
			for(int j = 0; j<ReposList.getLength(); j++)
			{
			
				Node nNodeRepo = ReposList.item(j);
				
				Element repoElement = (Element) nNodeRepo;
				
				singleList.add(eElement.getAttribute("name"));
				singleList.add(repoElement.getAttribute("name"));
				
				
				//System.out.println("repo name:" + repoElement.getAttribute("name"));
			}
			
			listOfLists.add(singleList);
			
		}
		
		return listOfLists;

		

	}

}
