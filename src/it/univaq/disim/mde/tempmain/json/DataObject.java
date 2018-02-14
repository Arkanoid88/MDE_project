package it.univaq.disim.mde.tempmain.json;

import java.io.File;
import java.util.List;

public class DataObject {
	
	private String name;
	private int x;
	private int y;
	private String id;
	private File path;
	private List<String> URI;
	private List<String> connections;
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getConnections() {
		return connections;
	}
	public void setConnections(List<String> connections) {
		this.connections = connections;
	}
	public File getPath() {
		return path;
	}
	public void setPath(File path) {
		this.path = path;
	}
	public List<String> getURI() {
		return URI;
	}
	public void setURI(List<String> uRI) {
		URI = uRI;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
