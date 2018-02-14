package it.univaq.disim.mde.tempmain.linkJavaMtl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

//java parser link: https://github.com/javaparser/javaparser/wiki/Manual
//https://stackoverflow.com/questions/32178349/parse-attributes-from-java-files-using-java-parser

public class parse_generate_java {

	public File parse_java(File file) throws FileNotFoundException {
		
        InputStream inputStream = new FileInputStream(file);
        
        CompilationUnit cu = JavaParser.parse(inputStream);
        
        String string_find = "MODULE_FILE_NAME";
        String string_find_check = "TEMPLATE_NAMES";
        
        List<Node> node_list = cu.getChildNodes();
        File result = new File("null");
        
        int i = 0;
        while(true)
        {
	        try
	        {
	            Node node = node_list.get(i);
	            String main_string = node.toString();
	            if(main_string.indexOf(string_find) !=-1 && main_string.indexOf(string_find_check) !=-1)
	            {
	            	int index = main_string.indexOf(string_find); 
	            	int index_final = main_string.indexOf(";",index);
	            	//index + offset
	            	result = new File(main_string.substring(index+20,index_final-1));
	            }
		        i = i + 1;
	        }
	        
	        catch(Exception exc)
	        {
	        	break;
	        }
        }
        System.out.println(result);
		return result;
        
        
	}

}
