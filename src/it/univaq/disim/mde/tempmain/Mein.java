package it.univaq.disim.mde.tempmain;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.http.HTTPException;

import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.xml.sax.SAXException;

import it.univaq.disim.mde.tempmain.HuristicManager.HeuristicManager;
import it.univaq.disim.mde.tempmain.extesionHeur.heuristic_no1;
import it.univaq.disim.mde.tempmain.gitRepoDownloader.GitHubRepositoryManager;
import it.univaq.disim.mde.tempmain.linkJavaMtl.parse_generate_java;
import it.univaq.disim.mde.tempmain.modelReader.Model_Reader;


public class Mein {


public static void main(String[] args) throws HTTPException, KeyManagementException, NoSuchAlgorithmException, IOException, GitAPIException, ParserConfigurationException, SAXException, XmlPullParserException{
	
		// per debug
		HeuristicManager HeuManager1 = new HeuristicManager();
		File path_file2 = new File("C:/Users/Andrea/Desktop/MDE_project");
		HeuManager1.ScanRepository(path_file2);
	
	
		/* scelta del file del modello da leggere */
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "XMI models", "xmi");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " +
                    chooser.getSelectedFile().getName());
        }
        /* fine scelta modello */
	
		/* lettura modello */
		ArrayList<ArrayList<String>> owner_repos_List = new ArrayList<ArrayList<String>>();
		
		Model_Reader model_reader = new Model_Reader();
		File file = new File(chooser.getSelectedFile().getAbsolutePath());
		owner_repos_List = model_reader.parse_model(file);
		
		List<String> files_list = new ArrayList<String>();//DA CONTROLLARE
		/* fine lettura modello */
		
		for(int i=0; i<owner_repos_List.size(); i++)
		{
			/* caso in cui uno user ha più di 1 repository */
			if(owner_repos_List.get(i).size()>2)
			{
				for(int j=0; j<owner_repos_List.get(i).size(); j=j+2)
				{
					System.out.println("utente: "+owner_repos_List.get(i).get(j));
					System.out.println("repository: "+owner_repos_List.get(i).get(j+1));
					
					/*clone*/
					GitHubRepositoryManager manage = new GitHubRepositoryManager();
					String path = null;
					path = manage.clone(owner_repos_List.get(i).get(j), owner_repos_List.get(i).get(j+1));
					/* end clone*/
					
					/*2nd part*/
					HeuristicManager HeuManager = new HeuristicManager();
					File path_file = new File(path);
					HeuManager.ScanRepository(path_file);
					/*end analysis*/
				}
			}
	
			/* caso normale, una repository */
			if(owner_repos_List.get(i).size()==0)
			{
				continue;
			}
			
			else
			{
				System.out.println("utente: "+owner_repos_List.get(i).get(0));
				System.out.println("repository: "+owner_repos_List.get(i).get(1));
				
				/* clone*/
				GitHubRepositoryManager manage = new GitHubRepositoryManager();
				String path = null;
				path = manage.clone(owner_repos_List.get(i).get(0), owner_repos_List.get(i).get(1));
				/* end clone*/
				
				/*2nd part*/
				HeuristicManager HeuManager = new HeuristicManager();
				//File path_file = new File("C:/Arkanoid88/MDE_project");//->path
				File path_file = new File(path);
				HeuManager.ScanRepository(path_file);
				/*end analysis*/
					
				break;
			}
			
		}
		
		/*bisogna aggiungere un heuristic manager*/
		
		/*for(String file2 : files_list)
		{
			System.out.println(file2);
			if (file2.indexOf(".java") != -1)// da controllare
			{
				File file_path = new File(file2);
				parse_generate_java parse = new parse_generate_java();
				System.out.println(parse.parse_java(file_path));
			}
			
		}*/
		
		
		
	}

}
