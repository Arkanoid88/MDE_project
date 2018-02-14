package it.univaq.disim.mde.tempmain.json;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class mein_json {

	public static void main(String[] args) {
		
		Json_manager manage = new Json_manager();
		List<DataObject> object_list = new ArrayList<DataObject>();
		List<DataObject> object_list2 = new ArrayList<DataObject>();
		
		DataObject node_s = new DataObject();
		DataObject node_t = new DataObject();
		
		String source = "ecore";
		String target = "mtl";
		
		//node_s = manage.add_node(source, 7, "null","null");
		//node_t = manage.add_node(target, 3, "null","null");
		
		String json = null;
		Gson gson = new Gson();
//		node_s =  manage.add_edge(node_s, node_t);
//		node_t = manage.add_edge(node_t, node_s);
		object_list.add(node_s);
		object_list.add(node_t);
		json = gson.toJson(object_list);
		
		
		  try {
			   //write converted json data to a file named "CountryGSON.json"
			   FileWriter writer = new FileWriter("C:/Users/Riccardo/workspace/mde/mde_project/src/it/univaq/disim/mde/tempmain/testw.json");
			   writer.write(json);
			   writer.close();
			  
			  } catch (IOException e) {
			   e.printStackTrace();
			  }
		

	}

}
