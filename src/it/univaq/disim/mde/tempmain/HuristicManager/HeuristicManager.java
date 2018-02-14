package it.univaq.disim.mde.tempmain.HuristicManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import com.google.gson.Gson;

import it.univaq.disim.mde.tempmain.MavenManager.Maven_component;
import it.univaq.disim.mde.tempmain.extesionHeur.heuristic_no1;
import it.univaq.disim.mde.tempmain.json.DataObject;
import it.univaq.disim.mde.tempmain.json.Json_manager;
import it.univaq.disim.mde.tempmain.linkJavaMtl.parse_generate_java;
import it.univaq.disim.mde.tempmain.linkMtlEcore.Mtl_component;;

public class HeuristicManager {
	
	
	public void ScanRepository(File path_file) throws IOException, XmlPullParserException
	{
		/* scansionje repository e creazione lista dei nodi */
		heuristic_no1 heu1 = new heuristic_no1();
		List<DataObject> object_list = new ArrayList<DataObject>();
		object_list = heu1.Files_List(path_file, object_list);
		/* fine scansione*/
		
		// lista di supporto
		List<DataObject> final_object_list = new ArrayList<DataObject>();
		final_object_list = object_list;
		
		/*
		 * per ogni nodo nella lista controlla se l'estensione è .java, in tal caso
		 * esamina il file .java e vede se esiste il campo MODULE_FILE_NAME
		 * alchè riscorre tutta la lista dei nodi cercando i .mtl,
		 * se il path contenuto nel java è uguale al path del nodo .mtl
		 * allora si procede a creare l'arco tra i 2 nodi, e si sostituisce nella lista
		 * il nuovo nodo con la lista delle connessioni (archi) aggiornata
		 */
		for( DataObject node : object_list)
		{
			
			String ext1 = FilenameUtils.getExtension(node.getName());
			// confronto file Acceleo con Ecore
			
			if(FilenameUtils.getExtension(node.getName()).equals("xml"))
			//if(node.getName().indexOf(".json") != -1)
			{
					// ecore dall'mtl
					String java_files = new Maven_component().getJavaList(node.getPath().toString());
					
					
					for(DataObject node_target : object_list)
					{
						if(node_target.getName().indexOf(".java") != -1 && node_target.getName().equals(java_files))
						{   
							Json_manager manage = new Json_manager();
							DataObject new_node = manage.add_edge(node, node_target, node.getConnections());
							int index = final_object_list.indexOf(node);
							final_object_list.set(index, new_node);
							System.out.println("okay!");
					   }	  
					
					}
			}
			
			
			if(node.getName().indexOf(".java") != -1)
			{
				// qui ottengo il nome del file mtl collegato al java
				parse_generate_java java_parser = new parse_generate_java();
				try 
				{	
					File var = java_parser.parse_java(node.getPath());
					if(var.toString()!="null")
					{
						for(DataObject node_target : object_list)
						{
							if(node_target.getName().indexOf(".mtl") != -1)
							{
								System.out.println("VAR: "+var.toString()+"\n NODE TARGET: "+node_target.getPath().toString());
								if(node_target.getPath().toString().indexOf(var.toString()) != -1)
								{
									Json_manager manage = new Json_manager();
									DataObject new_node = manage.add_edge(node, node_target, node.getConnections());
									int index = final_object_list.indexOf(node);
									final_object_list.set(index, new_node);
									System.out.println("okay!");
								}
								else
								{
									System.out.println("mtl: "+node_target.getPath().toString());
									System.out.println("mtl puntato dal java: "+var.toString());
								}
							}
						}
					}
				} 
					
				catch (FileNotFoundException e) 
				{
					System.out.print("il file "+node.getPath()+" non è stato trovato");
					e.printStackTrace();
				}
			}
			
			
			
			// confronto file Acceleo con Ecore
			if(node.getName().indexOf(".mtl") != -1)
			{
				// ecore dall'mtl
				String referenced_ecore = new Mtl_component().get_Ecore(node.getPath().toString());
				System.out.println(referenced_ecore);
				
				for(DataObject node_target : object_list)
				{
					if(node_target.getName().indexOf(".ecore") != -1)
					{

						if(node_target.getURI().contains(referenced_ecore))
						{
							System.out.println("okay!");
							Json_manager manage = new Json_manager();
							DataObject new_node = manage.add_edge(node, node_target, node.getConnections());
							int index = final_object_list.indexOf(node);
							final_object_list.set(index, new_node);
						}
						else
						{
							System.out.println("mtl: "+node_target.getPath().toString());
							System.out.println("mtl puntato dal java: "+node.toString());
						}
					}
				}
			}
			
			
			
		}
		
		
		/* scrittura su file */
		String json = null;
		Gson gson = new Gson();
		
	    json = gson.toJson(object_list);
	    
		  try {
			   //write converted json data to a file named "CountryGSON.json"
			   FileWriter writer = new FileWriter("./graph.json"); // da modificare
			   writer.write(json);
			   writer.close();
			  
			  } catch (IOException e) {
			   e.printStackTrace();
			  }
		
	}

}
