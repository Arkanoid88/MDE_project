package it.univaq.disim.mde.tempmain.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

/*
 * FILE DI PROVA GIUSTO PER CAPIRE COME FUNZIONA
 */

public class evaluate_json {

	public static void main(String[] args) {
		Gson gson = new Gson();
		
		
		DataObject obj = new DataObject();
		obj.setId("node1");
		obj.setX(0);
		obj.setY(1);
		List<String> lista = new ArrayList<String>();
		lista.add("node2");
		lista.add("node3");
		obj.setConnections(lista);
		
		String json = gson.toJson(obj);
		obj.setId("node2");
		obj.setX(0);
		obj.setY(0);
		lista.clear();
		lista.add("node1");
		obj.setConnections(lista);
		
		gson.toJson(obj);
	  
	  try {
	   //write converted json data to a file named "CountryGSON.json"
	   FileWriter writer = new FileWriter("C:/Users/Riccardo/workspace/mde/mde_project/src/it/univaq/disim/mde/tempmain/testw.json");
	   writer.write(json);
	   writer.close();
	  
	  } catch (IOException e) {
	   e.printStackTrace();
	  }
		  
		/*  
	    try {

	        BufferedReader br = new BufferedReader(
	            new FileReader("C:/Users/Riccardo/workspace/mde/MDE_project/src/it/univaq/disim/mde/test.json"));

	        //convert the json string back to object
	        DataObject[] obj1 = gson.fromJson(br, DataObject[].class);
	        System.out.println(obj1[0].getConnections());

	    } catch (IOException e) {
	        e.printStackTrace();
	    }*/
	 }

}
