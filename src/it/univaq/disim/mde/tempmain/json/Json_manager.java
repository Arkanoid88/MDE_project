package it.univaq.disim.mde.tempmain.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class Json_manager {

	public DataObject add_node(String name, int x, int y, File path, List<String> URI)
	{
		//creazione nuovo nodo
		DataObject obj = new DataObject();
		obj.setName(name);
		obj.setId(name+""+ThreadLocalRandom.current().nextInt(-9, 9));
		obj.setX(x);
		obj.setY(y);
		obj.setPath(path);
		obj.setURI(URI);
		List<String> lista = new ArrayList<String>();
		obj.setConnections(lista);
		return obj;
	}
	
	public DataObject add_edge(DataObject source, DataObject target, List<String> personal)
	{
		//List<String> lista = new ArrayList<String>();
		
		//connessione source->target
		personal.add(target.getId());
		source.setConnections(personal);
		
		return source;
	}

}
