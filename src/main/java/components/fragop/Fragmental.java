/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.fragop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.json.JSONObject;
import org.json.JSONArray;

import components.util.FileUtilsApache;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 *
 * @author DanielGara
 */
public class Fragmental {
    
	public static ArrayList<String> customize_files = new ArrayList<String>();
    public static File assets_folder;
    public static File assembled_folder;
    public static List<Map<String, String>> data;
    public static List<String> error_var;
    public static PriorityQueue<Fragment> fragments = new PriorityQueue<Fragment>();

    public static void principal(List<Map<String, String>> data_received, File assembled, File pool) {
    	error_var=new ArrayList();
    	data = new ArrayList();
        data=data_received;
        assembled_folder=assembled;
        assets_folder=pool;
        Fragment.data_no_fragments = new ArrayList();
        clean_directories();
        assemble_assets();
    }
    
    public static String get_errors(){
    	String errors="";
    	if(error_var.size()<=0) {return "";}
    	for(int i=0; i<error_var.size();i++) {
    		errors+="- "+error_var.get(i)+"\n";
    	}
    	return errors;
    }
    
    public static void assemble_assets(){
		//no fragments
		for(int i=0;i<data.size();i++){
			if(data.get(i).get("filename").equals("")) {
				error_var.add("Missing filename field for: "+data.get(i).get("ID"));
			}else if(!data.get(i).get("filename").contains(".frag")){
		        move_asset(data.get(i));
		        Fragment.data_no_fragments.add(data.get(i));
		    }
		}
		
		//fragments parse
		for(int i=0;i<data.size();i++){
			if(data.get(i).get("filename").equals("")) {}
			else if(data.get(i).get("filename").contains(".frag")){
				parse_fragment(data.get(i));
			}
		}
		while(fragments.size()>0) {
			Fragment f1= fragments.poll();
			f1.execute_actions();
		}
    }
    
    public static void parse_fragment(Map<String, String> fragment){
        File source_f = new File(assets_folder+"/"+fragment.get("component_folder")+"/"+fragment.get("filename"));
        if(source_f.exists()){
            try{
                String f_content = FileUtilsApache.readFileToString(source_f, "utf-8");
                int pos_frag=0;
                while(pos_frag!=-1) {
                	Fragment f1 = new Fragment(f_content,fragment.get("filename"),fragment.get("component_folder"),pos_frag,assembled_folder,assets_folder);
                	fragments.add(f1);
                	pos_frag=extract_multiple_fragments(f_content,pos_frag);
                }
            }
            catch(Exception e){
            	error_var.add(e.getMessage()+e.getStackTrace());
            }
        }else {
        	error_var.add(fragment.get("filename")+" doesn't exists, check the filename and path");
        }
    }
    
    public static int extract_multiple_fragments(String content, int pos_frag) {
    	pos_frag=pos_frag+10;
    	int pos_init = content.indexOf("Fragment ",pos_frag);
    	if(pos_init != -1) {
    		int pos_final = content.indexOf("{",pos_init+9);
    		if(pos_final != -1) {
    			return pos_init;
    		}else {
    			return -1;
    		}
    	}else {
    		return -1;
    	}
    	
    }
    
    public static void move_asset(Map<String, String> asset){
        set_directories_move_file(asset.get("filename"),asset.get("destination"),asset.get("component_folder"));        
    }
    
    public static void clean_directories(){
        File source_f = assembled_folder;
        if(source_f.exists()){
            try{
                FileUtilsApache.deleteDirectory(source_f);
                source_f.mkdirs();
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }else {
        	System.out.println("Invalid derived folder path");
        }
    }
    
    public static void set_directories_move_file(String filename, String destination, String component_folder){
    	try {
    		File source_f = new File(assets_folder+"/"+component_folder+"/"+filename);
    	
	        if(source_f.exists()){
	            File dest_f = new File(assembled_folder+"/"+destination);
	            File dest_path = new File(dest_f.getParent());
	            if(!dest_path.exists()){
	                dest_path.mkdirs();
	            }
	            try{
	            	String ext = getExtension(filename);
	            	//if it is a zip file, then extract
	            	if(ext.equals("zip")) {
	            		try {
	            		    ZipFile zipFile = new ZipFile(source_f);
	            		    zipFile.extractAll(assembled_folder+"/"+destination);
	            		} catch (ZipException e) {
	            		    e.printStackTrace();
	            		}
	            		
	            		System.out.println(filename);
	            	}else {
	            		FileUtilsApache.copyFile(source_f, dest_f);
	            	}
	            }
	            catch(IOException e){
	            	//System.out.println(assets_folder+"/"+component_folder+"/"+filename);
	            	//error_var.add("C04d - "+e.getMessage()+e.getStackTrace());
	            }
	            catch(Exception e){
	            	error_var.add("C04 - "+e.getMessage()+e.getStackTrace());
	            }
	        }else{
	        	error_var.add(filename+" doesn't exists, check the filename and path");
	        }
    	}catch(Exception e){
        	error_var.add("C011 - "+e.getMessage()+e.getStackTrace());
        }
    }
    
    //start customization functions
    public static ArrayList<String> check_folder(String component, File pool){
    	ArrayList<String> data_file = new ArrayList<String>();
        assets_folder=pool;
    	File source_f = new File(assets_folder+"/"+component+"/customization.json");
        if(source_f.exists()){
            try{
                String f_content = FileUtilsApache.readFileToString(source_f, "utf-8");
                JSONObject json = new JSONObject(f_content);
                
                if (json.get("CustomizationPoints") instanceof JSONArray) {
                	data_file.add("multiple");
                	JSONArray cpoints = (JSONArray) json.get("CustomizationPoints");
                	JSONArray plans;
                	try {
                		plans = (JSONArray) json.get("PointBracketsLans");
                	}
                	catch(Exception e){
                		plans = (JSONArray) json.get("PointBracketsLangs");
                	}
                	JSONArray ids = (JSONArray) json.get("IDs");
                	data_file.add(Integer.toString(cpoints.length()));

                	for (int i = 0; i < cpoints.length(); i++) {
                		String cpoint = cpoints.get(i).toString();
	                	String plan = plans.get(i).toString();
	                	String id = ids.get(i).toString();
	                	data_file.add(id);
	                	data_file.add(cpoint);
	                	data_file.add(plan);
                	}
                }else {
                	data_file.add("single");
                	data_file.add("1");
                	String cpoint = json.getString("CustomizationPoints");
                	String plan = json.getString("PointBracketsLans");
                	String id = json.getString("IDs");
                	data_file.add(id);
                	data_file.add(cpoint);
                	data_file.add(plan);
                }
            }
            catch(Exception e){
            	//error_var.add(e.getMessage()+e.getStackTrace());
            }
        }else {
        	data_file.add("error");
        }
    	return data_file;
    }
    
    public static String customize_one(String destination, String cpoint, String plan){
    	Fragment.assembled_folder=Fragmental.assembled_folder;
    	return Fragment.get_customization_code(destination, cpoint, plan);
    }
    
    public static void set_customize_one(String destination, String cpoint, String plan, String ccode){
    	Fragment.set_customization_code(destination, cpoint, plan, ccode);
    }
    
    public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int index = filename.lastIndexOf('.');
        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }
    
}