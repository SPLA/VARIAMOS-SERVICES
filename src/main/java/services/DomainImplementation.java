package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import components.fragop.Fragmental;
import lexermain.MainParser;

@Controller
public class DomainImplementation {
	
	@CrossOrigin
	@RequestMapping(value="/DomainImplementation/execute", method=RequestMethod.POST, produces="text/plain")
	@ResponseBody
	public String executeDerivation(@RequestBody String data_collected) {
		
		String completedMessage="";
		boolean some_files=false;
		Resource resource_derived = new ClassPathResource("/uploads/component_derived/");
		Resource resource_pool = new ClassPathResource("/uploads/component_pool/");
		JsonParser parser = new JsonParser();
		JsonObject rootObj = parser.parse(data_collected).getAsJsonObject();
		JsonElement data = rootObj.get("data");
	    String data_string = data.getAsString();
	    JsonArray rootArray = parser.parse(data_string).getAsJsonArray();
	    JsonArray rootFiles = rootArray.get(0).getAsJsonArray();
	    
	    List<Map<String, String>> files = new ArrayList<>();
	    for (int i = 0; i < rootFiles.size(); i++) {
	    	some_files=true;
		    Map<String, String> file_map = new HashMap<String, String>();
			JsonObject current_file = rootFiles.get(i).getAsJsonObject();
			file_map.put("component_folder", current_file.get("component_folder").getAsString());
			file_map.put("ID", current_file.get("ID").getAsString());
			file_map.put("filename", current_file.get("filename").getAsString());
			if(current_file.get("destination")!=null) {
				file_map.put("destination", current_file.get("destination").getAsString());
			}else {
				file_map.put("destination", "");
			}
			files.add(file_map);
	    }
	    
	    if(some_files) {
	    	if(resource_derived.exists()) {
				try {
					Fragmental.principal(files,resource_derived.getFile(),resource_pool.getFile());
					String found_errors=Fragmental.get_errors();
					if(found_errors.equals("")) {
						completedMessage=found_errors+"!!!Components successfully assembled!!!";
			        }else {
			        	completedMessage=found_errors+"!!!Components assembled with multiple errors!!!";
			        }
				}catch(Exception e){
	                System.out.println(e.getMessage());
	            }
			}
	    }else {
	    	completedMessage="No components to assemble";
	    }
		
		return completedMessage;
	}
	
	@CrossOrigin
	@RequestMapping(value="/DomainImplementation/verify", method=RequestMethod.POST, produces="text/plain")
	@ResponseBody
	public String verifyDerivation(@RequestBody String data_collected) {
		String completedMessage="";
		boolean some_files=false;
		Resource resource_derived = new ClassPathResource("/uploads/component_derived/");
		JsonParser parser = new JsonParser();
		JsonObject rootObj = parser.parse(data_collected).getAsJsonObject();
		JsonElement data = rootObj.get("data");
	    String data_string = data.getAsString();
	    JsonArray rootArray = parser.parse(data_string).getAsJsonArray();
	    
	    ArrayList<String> destinations = new ArrayList<>();
	    for (int i = 0; i < rootArray.size(); i++) {
	    	some_files=true;
	    	destinations.add(rootArray.get(i).getAsString());
	    }
	    
	    if(some_files) {
	    	if(resource_derived.exists()) {
				try {
					completedMessage=MainParser.executeParser(resource_derived.getFile(), destinations);
				}catch(Exception e){
	                System.out.println(e.getMessage());
	            }
	    	}
	    }else {
	    	completedMessage="No components assembled";
	    }
	    return completedMessage;
	}
	
}
