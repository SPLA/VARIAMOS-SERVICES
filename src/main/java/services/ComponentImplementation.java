package services;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import components.fragop.Fragmental;
import components.util.FileUtilsApache;
import lexermain.MainParser;

@Controller
public class ComponentImplementation {
	
	public static Resource resource_derived = new ClassPathResource("/uploads/component_derived/");
	public static Resource resource_pool = new ClassPathResource("/uploads/component_pool/");
	
	@CrossOrigin
	@RequestMapping(value="/ComponentImplementation/execute", method=RequestMethod.POST, produces="text/plain")
	@ResponseBody
	public String executeDerivation(@RequestBody String data_collected) {
		
		String completedMessage="";
		boolean some_files=false;
		JsonParser parser = new JsonParser();
		JsonObject rootObj = parser.parse(data_collected).getAsJsonObject();
		JsonElement data = rootObj.get("data");
	    String data_string = data.getAsString();
	    
		JsonElement p_pool = rootObj.get("p_pool");
		resource_pool= new ClassPathResource(p_pool.getAsString());
		JsonElement p_derived = rootObj.get("p_derived");
		resource_derived= new ClassPathResource(p_derived.getAsString());
	    
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
	@RequestMapping(value="/ComponentImplementation/verify", method=RequestMethod.POST, produces="text/plain")
	@ResponseBody
	public String verifyDerivation(@RequestBody String data_collected) {
		String completedMessage="";
		boolean some_files=false;
		JsonParser parser = new JsonParser();
		JsonObject rootObj = parser.parse(data_collected).getAsJsonObject();
		JsonElement data = rootObj.get("data");
	    String data_string = data.getAsString();
	    JsonArray rootArray = parser.parse(data_string).getAsJsonArray();
	    
		JsonElement p_derived = rootObj.get("p_derived");
		resource_derived= new ClassPathResource(p_derived.getAsString());
	    
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
	
	@CrossOrigin
	@RequestMapping(value="/ComponentImplementation/uploadfile", consumes = {"multipart/form-data"}, method=RequestMethod.POST)
	@ResponseBody
    public String uploadFile(@RequestParam("dest") String dest, @RequestParam("p_derived") String p_derived, @RequestPart("file") MultipartFile file) throws Exception{
		resource_derived= new ClassPathResource(p_derived);
        File derivation = resource_derived.getFile();
        if (!file.isEmpty()) {
            try {
            	File convFile = new File(derivation+"/"+dest);
                convFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(convFile); 
                fos.write(file.getBytes());
                fos.close();
                return "uploaded";
            } catch (Exception e) {
                return "error" + e.getMessage();
            }
        } else {
            return "error";
        }

    }
	
	@CrossOrigin
	@RequestMapping(value="/ComponentImplementation/customize/{type}", method=RequestMethod.POST, produces="text/plain")
	@ResponseBody
	public String customizeDerivation(@RequestBody String data_collected, @PathVariable String type) {
		JsonParser parser = new JsonParser();
		JsonObject rootObj = parser.parse(data_collected).getAsJsonObject();
		
		JsonElement p_pool = rootObj.get("p_pool");
		resource_pool= new ClassPathResource(p_pool.getAsString());
		JsonElement p_derived = rootObj.get("p_derived");
		resource_derived= new ClassPathResource(p_derived.getAsString());
		
		try {
			Fragmental.assembled_folder=resource_derived.getFile();
		}
		catch(Exception e){
            System.out.println(e.getMessage());
        }
		JsonElement data = rootObj.get("data");
	    String data_string = data.getAsString();
	    JsonArray rootArray = parser.parse(data_string).getAsJsonArray();
	    ArrayList<String> data_row = new ArrayList<String>();
	    ArrayList<ArrayList<String>> data_to_return = new ArrayList<ArrayList<String>>();
	    String component="";
	    
	    if(type.equals("start")) {
	    	for (int i = 0; i < rootArray.size(); i++) {
		    	component=rootArray.get(i).getAsString();
		    	if(resource_pool.exists()) {
					try {
						data_row = Fragmental.check_folder(component,resource_pool.getFile());
						data_to_return.add(data_row);
					}
					catch(Exception e){
		                System.out.println(e.getMessage());
		            }
		    	}
		    }
	    	
		    Gson gson = new Gson();
		    return gson.toJson(data_to_return);
	    }else if(type.equals("next")) {
	    	if(rootArray.size()>3) {
	    		if(rootArray.get(4).getAsString().equals("") && rootArray.get(5).getAsString().equals("")) {
	    			//
	    		}else {
	    			Fragmental.set_customize_one(rootArray.get(3).getAsString(),rootArray.get(4).getAsString(),rootArray.get(5).getAsString(),rootArray.get(6).getAsString());
	    		}
	    	}
	    	
	    	if(rootArray.get(1).getAsString().equals("") && rootArray.get(2).getAsString().equals("")) {
	    		return "file";
	    	}else {
	    		return Fragmental.customize_one(rootArray.get(0).getAsString(),rootArray.get(1).getAsString(),rootArray.get(2).getAsString());
	    	}
	    	
	    }else if(type.equals("onlysave")) {
	    	if(rootArray.get(1).getAsString().equals("") && rootArray.get(2).getAsString().equals("")) {
	    		//
	    	}else {
	    		Fragmental.set_customize_one(rootArray.get(0).getAsString(),rootArray.get(1).getAsString(),rootArray.get(2).getAsString(),rootArray.get(3).getAsString());
	    	}
	    	return "";
	    }else {
	    	return "";
	    }
	   
	}
	
	@CrossOrigin 
	@RequestMapping(value="/ComponentImplementation/getFile", method=RequestMethod.POST, produces="text/plain")
	@ResponseBody
	public String getFile(@RequestBody String data_collected) {
		String completedMessage="";
		boolean some_files=false;
		JsonParser parser = new JsonParser();
		JsonObject rootObj = parser.parse(data_collected).getAsJsonObject();
		JsonElement data = rootObj.get("data");
		String dat=data.getAsString();
		JsonObject rootObj2 = parser.parse(dat).getAsJsonObject();
		JsonElement data2 = rootObj2.get("filename");
		String filename = data2.getAsString();
		JsonElement data3 = rootObj2.get("component");
		String component = data3.getAsString();
		String file_code="";

		JsonElement p_pool = rootObj.get("p_pool");
		resource_pool= new ClassPathResource(p_pool.getAsString());
        try {
    		File component_p = resource_pool.getFile();
            File the_file = new File(component_p+"/"+component+"/"+filename);
            if(the_file.exists()){
                file_code = FileUtilsApache.readFileToString(the_file, "utf-8");
            }else {
            	file_code = "File not found";
            }

        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
		
		return file_code;
	}
	
	
}
