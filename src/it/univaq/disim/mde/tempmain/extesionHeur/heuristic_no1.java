package it.univaq.disim.mde.tempmain.extesionHeur;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.io.FilenameUtils;

import com.google.gson.Gson;

import it.univaq.disim.mde.tempmain.json.DataObject;
import it.univaq.disim.mde.tempmain.json.Json_manager;
import it.univaq.disim.mde.tempmain.linkMtlEcore.Mtl_component;
import it.univaq.disim.mde.tempmain.linkMtlEcore.Ecore_component;;;

public class heuristic_no1 
{
	//funzione ricorsiva che data una directory, naviga tutti i file di tutte le sotto-directory
	public List<DataObject> Files_List(File folder_path, List<DataObject> object_list) throws FileNotFoundException
	{
		File[] listOfFiles = folder_path.listFiles();
		Json_manager manage = new Json_manager();
		
	    for (File file : listOfFiles) {
	        
	    	if (file.isDirectory())//directory 
	        {
	            Files_List(file,object_list); // Calls same method again.
	        } 
	    	
	        else //file 
	        {
		    	String ext = FilenameUtils.getExtension(file.getName());
		    	if (ext.equals("txt") || ext.equals("xmi") || ext.equals("java") || ext.equals("mtl"))
		    	{
		    		File file_path = new File(file.getAbsolutePath());
		    		DataObject node = new DataObject();
		    		List<String> list_nsURI = new ArrayList<String>();
		    		list_nsURI.add("");
		    		node = manage.add_node(file.getName(), ThreadLocalRandom.current().nextInt(-200, 200),ThreadLocalRandom.current().nextInt(-200, 200),file_path ,list_nsURI);
		    		object_list.add(node);
		    	}
		    	if (ext.equals("ecore"))
		    	{
		    		File file_path = new File(file.getAbsolutePath());
		    		DataObject node = new DataObject();
		    		List<String> list_nsURI = new Ecore_component().nsURI(file_path.toString());
		    		node = manage.add_node(file.getName(), ThreadLocalRandom.current().nextInt(-200, 200),ThreadLocalRandom.current().nextInt(-200, 200),file_path ,list_nsURI);
		    		object_list.add(node);
		    	}
		    	if (ext.equals("xml"))
		    	{
		    		File file_path = new File(file.getAbsolutePath());
		    		DataObject node = new DataObject();
		    		List<String> list_nsURI = new Ecore_component().nsURI(file_path.toString());
		    		node = manage.add_node(file.getName(), ThreadLocalRandom.current().nextInt(-200, 200),ThreadLocalRandom.current().nextInt(-200, 200),file_path ,list_nsURI);
		    		object_list.add(node);
		    	}
	        }
	    }
	    
	    return object_list;	
		
	}


}
