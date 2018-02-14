package it.univaq.disim.mde.tempmain.linkMtlEcore;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Ecore_component {
	
	
	public List<String> nsURI(String path) throws FileNotFoundException{
		
		List<String> URI_list = new ArrayList<String>();
		try {
			  
	         File inputFile = new File(path);
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();
	         //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
	         //System.out.println(doc.getDocumentElement().getAttribute("nsURI"));
	         URI_list.add(doc.getDocumentElement().getAttribute("nsURI"));
	         NodeList nList = doc.getElementsByTagName("eSubpackages");
	         //System.out.println(nList);
	         
	         for (int temp = 0; temp < nList.getLength(); temp++) {
	             Node nNode = nList.item(temp);
	             //System.out.println("\nCurrent Element :" + nNode.getNodeName());
	             
	             if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	                Element eElement = (Element) nNode;
	                URI_list.add(eElement.getAttribute("nsURI"));
	                //System.out.println(eElement.getAttribute("nsURI"));
	             }
	          }
	         
	         return URI_list;
	         
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
		
		return URI_list;
			
	   }
	
}

