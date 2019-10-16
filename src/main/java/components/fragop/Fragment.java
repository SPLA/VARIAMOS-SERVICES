/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.fragop;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import components.util.FileUtilsApache;

/**
 *
 * @author DanielGara
 */
public class Fragment implements Comparable<Fragment> {
    public String content;
    public String filename;
    public String c_folder;
    public int pos_frag;
    public int priority;
    
    public static File assets_folder;
    public static File assembled_folder;
    public static String error_var="";

	public Map<String, String> data = new HashMap<String, String>();
    public static List<Map<String, String>> data_no_fragments = new ArrayList();
    
    public Fragment(String c, String f, String c_folder, int p, File assembled, File pool){
    	assembled_folder=assembled;
        assets_folder=pool;
        this.setContent(c);
        this.setFilename(f);
        this.setComponentFolder(c_folder);
        this.setPos_frag(p);
        this.parse_fragment_content();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getComponentFolder() {
		return c_folder;
	}

	public void setComponentFolder(String c_folder) {
		this.c_folder = c_folder;
	}
	
    public int getPos_frag() {
		return pos_frag;
	}

	public void setPos_frag(int pos_frag) {
		this.pos_frag = pos_frag;
	}
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int prio) {
		this.priority = prio;
	}
	
	public void setPriority(String prio) {
		if(prio.equals("high")) {
			this.priority=10;
		}else if(prio.equals("medium")) {
			this.priority=500;
		}else if(prio.equals("low")){
			this.priority=1000;
		}else {
			System.out.println(" - " +Integer.parseInt(prio));
			this.priority=Integer.parseInt(prio);
		}
	}

	public void execute_actions(){
		String[] split_dest = null;
		String[] split_point = null;
		//SPLIT multiple destination of FPoint
		if(data.get("destination")!=null){
			split_dest = data.get("destination").split("\\s*,\\s*");
		}
		if(data.get("fpoint")!=null){
			split_point = data.get("fpoint").split("\\s*,\\s*");
		}		
		
		if(data.get("sourcefile")!=null){ //FILES
        	if(data.get("action").equals("add") || data.get("action").equals("replace")){
        		for (String s_dest: split_dest) {
        			Fragmental.set_directories_move_file(data.get("sourcefile"), s_dest, this.getComponentFolder());
        		}
        	}
        }else{
        	//NO - FILES
        	List<Map<String, String>> f_to_modify = new ArrayList();
	        List<String> comment_block_tags = new ArrayList<String>();
	        for (String s_dest: split_dest) {
	        	f_to_modify.add(get_fragment_by_ID(s_dest));
	        }	       
	        comment_block_tags=get_comment_block(data.get("pointbracketslan"));
	        String f_to_modify_content = "";
	
	        if(f_to_modify!=null){
	            if(data.get("action").equals("replace") || data.get("action").equals("hide") || (data.get("action").equals("add") && data.get("sourcecode") != null ) || (data.get("action").equals("remove") && data.get("fpoint") != null )){
	            	for(int h=0;h<f_to_modify.size();h++){
	            		
		                try{
		                	File source_f_to_modify = new File(assembled_folder+"/"+f_to_modify.get(h).get("destination"));
		                	f_to_modify_content = FileUtilsApache.readFileToString(source_f_to_modify, "utf-8");
		                	
		                	if(data.get("action").equals("add")){
				                    if(data.get("sourcecode") != null){
				                        String string_search = comment_block_tags.get(0)+"B-"+split_point[h]+comment_block_tags.get(1);
				                        int pos_init = f_to_modify_content.indexOf(string_search);
				                        if(pos_init != -1){
				                            String new_content = f_to_modify_content.substring(0,pos_init+string_search.length());
				                            String trace = this.add_trace(comment_block_tags,data.get("sourcecode"),"add","injected");
				                            new_content += trace;
				                            new_content += f_to_modify_content.substring(pos_init+string_search.length());
				                            try{
				                                FileUtilsApache.writeStringToFile(source_f_to_modify, new_content, "utf-8");
				                            }
				                            catch(Exception e){
				                            	Fragmental.error_var.add("C02 - "+e.getMessage());
				                            }
				                        }
				                        else {
				                        	Fragmental.error_var.add("Invalid fragmentation point: "+string_search+", doesn't exists (At file "+f_to_modify.get(h).get("filename")+") - (Fragment "+data.get("name")+")");
				                        }
				                    }
				                }//REPLACE - HIDE CODE
					            else if(data.get("action").equals("replace") || data.get("action").equals("hide")){					            	
				            		String string_search = comment_block_tags.get(0)+"B-"+split_point[h]+comment_block_tags.get(1);
				                    String string_search2 = comment_block_tags.get(0)+"E-"+split_point[h]+comment_block_tags.get(1);
				                    
				                    int pos_init = f_to_modify_content.indexOf(string_search);
				                    int pos_final = f_to_modify_content.indexOf(string_search2);
				                    if(pos_init != -1 && pos_final != -1){
				                        String new_content = f_to_modify_content.substring(0,pos_init+string_search.length());
				                        String trace = "";
				                        if(data.get("sourcecode") != null && data.get("action").equals("replace")){
				                            trace = this.add_trace(comment_block_tags,data.get("sourcecode"),"add","replaced");
				                        }else if(data.get("action").equals("hide")){
				                            String new_code=f_to_modify_content.substring(pos_init+string_search.length(),pos_final);
				                            trace = this.add_trace(comment_block_tags,new_code,"hide","hidden");
				                        }else if(data.get("action").equals("remove")){
				                            trace = this.add_trace(comment_block_tags,"","remove","removed");
				                        }
				                        new_content += trace;
				                        new_content += f_to_modify_content.substring(pos_final);
				                        try{
				                        	FileUtilsApache.writeStringToFile(source_f_to_modify, new_content, "utf-8");
				                        }
				                        catch(Exception e){
				                        	Fragmental.error_var.add("C03 - "+e.getMessage());
				                        }
				                    }else {
				                    	Fragmental.error_var.add("Invalid fragmentation point: "+string_search+", doesn't exists (At file "+f_to_modify.get(h).get("filename")+") - (Fragment "+data.get("name")+")");
				                    }
				                }
		                	
		                	
		                }catch(Exception e){
		                	//Revisar
	                    	//Fragmental.error_var.add("C01 - "+e.getMessage());
	                    }
		                
	            	}
	            }else{
	            	Fragmental.error_var.add("Invalid action: "+data.get("action")+")");
	            }
	        }
        }
    }
    
    public String add_trace(List<String> tags, String code, String action, String label){
        String string_t="\n\n";
        string_t+=tags.get(0)+"Code "+label+" by: "+data.get("name")+tags.get(1);
        string_t+="\n";
        
        if(action.equals("hide")){
            string_t+=tags.get(0)+code+tags.get(1);
        }else if(action.equals("add")){
            string_t+=code;
        }
        
        string_t+="\n";
        string_t+=tags.get(0)+"Code "+label+" by: "+data.get("name")+tags.get(1);
        string_t+="\n";
        return string_t;
    }
    
    public void parse_fragment_content(){
        data.put("name",extract_string("Fragment ","{",this.content));
        data.put("action",extract_string("Action:", "\n",this.content));
        data.put("priority",extract_string("Priority:", "\n",this.content));
        this.setPriority(extract_string("Priority:", "\n",this.content));
        data.put("fpoint",extract_string("FragmentationPoints:","\n",this.content));
        data.put("pointbracketslan",extract_string("PointBracketsLan:","\n",this.content));
        data.put("destination",extract_string("Destinations:","\n",this.content));
        data.put("sourcefile",extract_string("SourceFile:","\n",this.content));        
        data.put("sourcecode",extract_string("[ALTERCODE-FRAG]","[/ALTERCODE-FRAG]",this.content));
        
        if(data.get("name")==null || data.get("priority")==null || data.get("action")==null || data.get("destination")==null) {
        	Fragmental.error_var.add("Invalid Fragment definition for:" + this.getFilename() + " - Name, Priority, Action and Destination are required");
        }
    }
    
    public static Map<String, String> get_fragment_by_ID(String ID){
        for(int i=0;i<data_no_fragments.size();i++){
            if(ID.equals(data_no_fragments.get(i).get("ID"))){
                return data_no_fragments.get(i);
            }
        }
        return null;
    }
    
    public String extract_string(String s_init, String s_final, String content){
        int pos_init = content.indexOf(s_init,this.getPos_frag());
        int len_init = s_init.length();
        int pos_final = content.indexOf(s_final, pos_init);
        if((pos_init != -1) && (pos_final != -1)){
            String f_string = content.substring(pos_init+len_init, pos_final).trim();
            return f_string;
        }else{
            return null;
        }
    }
    
    public static List<String> get_comment_block(String language){
        List<String> comment_block_tags = new ArrayList<String>();
        if(language.equals("php") || language.equals("css") || language.equals("sql") || language.equals("java")){
            comment_block_tags.add("/*");
            comment_block_tags.add("*/");
        }else if(language.equals("html")){
            comment_block_tags.add("<!--");
            comment_block_tags.add("-->");
        }else{
        	Fragmental.error_var.add("Comment block for: "+language+" doesn't exist please add Fragment.java file");
        }
        return comment_block_tags;
    }
    
    //customization functions
    public static String get_customization_code(String destination, String cpoint, String plan) {
    	String file_code = "";
    	String customization_code = "";
    	List<String> comment_block_tags=get_comment_block(plan);
    	
    	File source_f_to_modify = new File(assembled_folder+"/"+destination);
        if(source_f_to_modify.exists()){
            try{
            	file_code = FileUtilsApache.readFileToString(source_f_to_modify, "utf-8");
            	String string_search = comment_block_tags.get(0)+"BCP-"+cpoint+comment_block_tags.get(1);
                String string_search2 = comment_block_tags.get(0)+"ECP-"+cpoint+comment_block_tags.get(1);
                int pos_init = file_code.indexOf(string_search);
                int pos_final = file_code.indexOf(string_search2);
                
                if(pos_init != -1 && pos_final != -1){
                	customization_code = file_code.substring(pos_init+string_search.length(),pos_final);
                }
            }
            catch(Exception e){
            	//Fragmental.error_var.add("C01 - "+e.getMessage());
            }
        }  
    	return customization_code;
    }
    
    public static void set_customization_code(String destination, String cpoint, String plan, String customized_code) {
    	String file_code = "";
    	List<String> comment_block_tags=get_comment_block(plan);
    	File source_f_to_modify = new File(assembled_folder+"/"+destination); 	
    	
    	if(source_f_to_modify.exists()){
            try{
            	file_code = FileUtilsApache.readFileToString(source_f_to_modify, "utf-8");
            	String string_search = comment_block_tags.get(0)+"BCP-"+cpoint+comment_block_tags.get(1);
                String string_search2 = comment_block_tags.get(0)+"ECP-"+cpoint+comment_block_tags.get(1);
            	int pos_init = file_code.indexOf(string_search);
                int pos_final = file_code.indexOf(string_search2);
                if(pos_init != -1 && pos_final != -1){
                    String new_content = file_code.substring(0,pos_init+string_search.length());
                    new_content += customized_code;
                    new_content += file_code.substring(pos_final);
	                try{
	                	FileUtilsApache.writeStringToFile(source_f_to_modify, new_content, "utf-8");
	                }
	                catch(Exception e){
	                	Fragmental.error_var.add("C05 - "+e.getMessage());
	                }
            	}
            }
            catch(Exception e){
            	//Fragmental.error_var.add("C01 - "+e.getMessage());
            }
        }  
    }

	public int compareTo(Fragment frag) {
		if(this.getPriority() > frag.getPriority()) {
            return 1;
        } else if (this.getPriority() < frag.getPriority()) {
            return -1;
        } else {
            return 0;
        }
	}
    
}
