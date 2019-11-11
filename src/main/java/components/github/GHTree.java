package components.github;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.Resource;

public class GHTree {
	String path = "";
	String mode = "100644";
	String content = "";
	String type = "blob";
	
	public GHTree() {
		
	}
	
	public GHTree(String pa, String con) {
		this.path=pa;
		this.content=con;
	}
	
	public static Map<String, List<GHTree>> getTrees(Resource resource_derived){
		Map<String, List<GHTree>> hm = new HashMap<String, List<GHTree>>();
		List<GHTree> list = new ArrayList<GHTree>();
		
		try {
			addFilesToTree(list,resource_derived.getFile(),resource_derived.getFile());
		}catch (Exception e) {
			
		}
		hm.put("tree", list);
		return hm;
	}
	
	private static void addFilesToTree(List<GHTree> list, File baseDirectory, File currentDirectory) throws IOException {
	    for(File file : currentDirectory.listFiles()) {
	        String relativePath = baseDirectory.toURI().relativize(file.toURI()).getPath();
	        if(file.isFile()) {
	        	GHTree g1 = new GHTree(relativePath,FileUtils.readFileToString(file, "UTF-8"));
	        	list.add(g1);
	        } else {
	            addFilesToTree(list, baseDirectory, file);
	        }
	    }
	}
}
