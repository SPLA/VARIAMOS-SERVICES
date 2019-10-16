package services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import commandExecutor.CmdExecutor;
import com.coffee.compiler.CompilationParameters;
import com.coffee.compiler.Compiler;
import com.coffee.compiler.SourceOfCompilation;
import coffee.modelParsers.varXmlToHLVLParser.VariamosXMLToHlvlParser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Controller
public class Verification {
	
	private static VariamosXMLToHlvlParser hlvl_parser = new VariamosXMLToHlvlParser();
	private String project_direction = "C:\\Users\\admin\\git\\VARIAMOS-SERVICES\\";
	private String Solver_config = "CoffeeSolvers";
	private String Solver_selected = "Gecode";
	private String Frontend_config = "frontend";
	
	@CrossOrigin
	@RequestMapping(value="/Verification/test", method=RequestMethod.POST, produces="text/plain")
	@ResponseBody
	public String executeTest(@RequestBody String data_collected) {
		JsonParser parser = new JsonParser();
		JsonObject rootObj = parser.parse(data_collected).getAsJsonObject();
		JsonElement data = rootObj.get("data");
		String data_string = data.getAsString();
		System.out.println("Data:");
		//System.out.println(data_string.substring(0,1000));
		System.out.println(data_string.substring(0,data_string.length()));
		System.out.println("End");
		return "XML code received by the server";
	}
	
	@CrossOrigin
	@RequestMapping(value="/Verification/check_void_false", method=RequestMethod.POST, produces="text/plain")
	@ResponseBody
	public String check_void_false(@RequestBody String data_collected) {
		JsonParser parser = new JsonParser();
		JsonObject rootObj = parser.parse(data_collected).getAsJsonObject();
		JsonElement data = rootObj.get("data");
		String name = rootObj.get("name").getAsString();
		name = name.replaceAll("\\s","");
		name = name.replaceAll("-","");
		String data_string = data.getAsString();
		JsonObject param = rootObj.get("param").getAsJsonObject();

		File currentFileDir = verifyDirectory(project_direction+"testfiles\\");
		
		parsehlvl(data_string, name, currentFileDir, param);
		
		//MCS
		int length = 0;
		List<String> temparray = new ArrayList<String>();
		for (String keyStr : param.keySet()) {
	        temparray.add(keyStr);
	        length++;
	    }
		boolean voidfalseresult = checksubsets(length, temparray, param, currentFileDir, name);
		
		String returnmessage = "";
//		JsonObjectBuilder objectBuilder = Json.createObjectBuilder().add("solution", getsolverresult(param, currentFileDir, name)).add("hlvl", "HLVL:\n"+result);
//		javax.json.JsonObject response_result = objectBuilder.build();
		if(voidfalseresult)
			returnmessage = "There is at least one solution.";
		else
			returnmessage = "There is no solution.";
		System.out.println("End");
		return returnmessage;
	}
	
	@CrossOrigin
	@RequestMapping(value="/Verification/check_dead", method=RequestMethod.POST, produces="text/plain")
	@ResponseBody
	public String check_dead(@RequestBody String data_collected) {
		JsonParser parser = new JsonParser();
		JsonObject rootObj = parser.parse(data_collected).getAsJsonObject();
		JsonElement data = rootObj.get("data");
		String name = rootObj.get("name").getAsString();
		name = name.replaceAll("\\s","");
		name = name.replaceAll("-","");
		String data_string = data.getAsString();
		JsonObject param = rootObj.get("param").getAsJsonObject();

		File currentFileDir = verifyDirectory(project_direction+"testfiles\\");
		
		parsehlvl(data_string, name, currentFileDir, param);
		
		//MCS
		int length = 0;
		List<String> temparray = new ArrayList<String>();
		for (String keyStr : param.keySet()) {
	        temparray.add(keyStr);
	        length++;
	    }
		List<String> voidfalseresult = checkdeadsubsets(length, temparray, param, currentFileDir, name);
		if(voidfalseresult.size() == 0)
			return null;
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
		for(int i = 0; i < voidfalseresult.size(); i++)
		{
			objectBuilder.add("" + i, voidfalseresult.get(i));
		}
		javax.json.JsonObject response_result = objectBuilder.build();
		System.out.println(response_result.toString());
		return response_result.toString();
	}
	
	private boolean getsolverresult(JsonObject param, File currentFileDir, String name, String[] verification) {
		List<String> list = Arrays.asList(verification);
		for (String keyStr : param.keySet()) {
			if(list.contains(keyStr))
			{
				param.addProperty(keyStr, true);
			}
			else
			{
				param.addProperty(keyStr, false);
			}
	    }
		System.out.println("Configuration: " + param.toString());
		
		CompilationParameters params;
		Compiler compiler= new Compiler();
		try {
			params = new CompilationParameters(
					project_direction+"testfiles\\src-gen\\", //INPUT_FILES_PATH 
					project_direction+"testfiles\\src-gen\\", //MZN_FILES_PATH 
					project_direction+"testfiles\\", //OUTPUT_FILES_PATH
					name,
					Solver_config,
					Frontend_config,
					SourceOfCompilation.FILE
					);
			compiler.setUpCompilation(params);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		javax.json.JsonObject solution = null;
		try {
			solution = compiler.getNSolutions(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(solution.get("state").toString());
		if(solution.get("state").toString().contains("UNSATISFIABLE"))
			return false;
		return true;
	}
	private static File verifyDirectory(String dir) {
		File fileDir = new File(dir);
		//System.out.println("fileDir: "+fileDir.getAbsolutePath());
		if(!fileDir.exists()) fileDir.mkdir();
		return fileDir;
	}
	private void parsehlvl(String data_string, String name, File currentFileDir, JsonObject param) {
		hlvl_parser = new VariamosXMLToHlvlParser();
		String result = "";
		//System.out.println("Data:");
		//System.out.println("Received xml:" + data_string);
		//System.out.println(data_string.substring(0,1000));
		try {
			result = hlvl_parser.parse(data_string, name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File currentModelFile1 = new File(currentFileDir.getAbsolutePath()+"/"+ name +".hlvl");
		//System.out.println(currentModelFile.getAbsolutePath());
		BufferedWriter bw1;
		try {
			bw1 = new BufferedWriter(new FileWriter(currentModelFile1));
			bw1.write(result);
			bw1.close();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CmdExecutor executor = new CmdExecutor(project_direction+"testfiles\\");
		List<String> temp = new ArrayList<String>();
		
		String command = "java -jar " + project_direction + "src\\main\\webapp\\WEB-INF\\lib\\HLVLParser.jar "+ name +".hlvl";
		temp.add(command);
		
		executor.setCommandInConsole(temp);
		try {
			executor.runCmd();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String coffeesolver = "{\r\n" + 
				"	\"CSPSolver\": [\r\n" + 
				"		{\r\n" + 
				"			\"solverId\": \"Gecode\",\r\n" + 
				"			\"cmd\": \"Gecode\",\r\n" + 
				"            \"maxSolutions\": 10,\r\n" + 
				"            \"maxTime\":10000,\r\n" + 
				"            \"allInfo\": \"-v\",\r\n" + 
				"            \"allSolutions\" : \"-a\",\r\n" + 
				"            \"boundSolutions\" : \"-n\",\r\n" + 
				"            \"timeLimit\": \"--time-limit\"\r\n" + 
				"		}\r\n" + 
				"	],\r\n" + 
				"	\"SATSolver\": [\r\n" + 
				"		{\r\n" + 
				"			\"solverId\":\"picat\",\r\n" + 
				"			\"cmd\": \"picat_sat_cmd_line\", \r\n" + 
				"            \"maxSolutions\": 10,\r\n" + 
				"            \"maxTime\":10000,\r\n" + 
				"            \"allInfo\": \"-v\",\r\n" + 
				"            \"allSolutions\" : \"-a\",\r\n" + 
				"            \"boundSolutions\" : \"-n\",\r\n" + 
				"            \"timeLimit\": \"--time-limit\"\r\n" + 
				"		},\r\n" + 
				"		{\r\n" + 
				"			\"solverId\": \"Gecode\",\r\n" + 
				"			\"cmd\": \"Gecode\",\r\n" + 
				"            \"maxSolutions\": 10,\r\n" + 
				"            \"maxTime\":10000,\r\n" + 
				"            \"allInfo\": \"-v\",\r\n" + 
				"            \"allSolutions\" : \"-a\",\r\n" + 
				"            \"boundSolutions\" : \"-n\",\r\n" + 
				"            \"timeLimit\": \"--time-limit\"\r\n" + 
				"		}\r\n" + 
				"	]\r\n" + 
				"}\r\n" + 
				"";
		
		File currentModelFile3 = new File(currentFileDir.getAbsolutePath()+ "\\src-gen\\" +Solver_config+".json");
		//System.out.println(currentModelFile.getAbsolutePath());
		BufferedWriter bw3;
		try {
			bw3 = new BufferedWriter(new FileWriter(currentModelFile3));
			bw3.write(coffeesolver);
			bw3.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		String frontEndData = "{\n";
		frontEndData += "\"solverSelected\" : \""+Solver_selected+"\",\n";
		frontEndData += "\"problemType\" : \""+"BASIC_BOOL"+"\",\n";
		frontEndData += "\"configuration\" : \n"+ param.toString() +"\n";
		frontEndData += "}";
		
		File currentModelFile2 = new File(currentFileDir.getAbsolutePath()+ "\\src-gen\\" +Frontend_config+".json");
		//System.out.println(currentModelFile.getAbsolutePath());
		BufferedWriter bw2;
		try {
			bw2 = new BufferedWriter(new FileWriter(currentModelFile2));
			bw2.write(frontEndData);
			bw2.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	private boolean checksubsets(int length, List<String> temparray, JsonObject param, File currentFileDir, String name) {
		String[][] result = new String [(int) Math.pow(2,length)][length];
		if(temparray.size() == 0)
			return false;
		result[0][0] = temparray.get(0);
		if(getsolverresult(param, currentFileDir, name, result[0]))
			return true;
		int currentlength = 1;
		for(int i = 1 ; i < length; i++)
		{
			String[][] temp = new String [currentlength+1][length];
			for(int j = 0; j < currentlength; j++)
			{
				int stoplength = 0;
				for(int k = 0 ; k < length; k++)
				{
					temp[j][k]= result[j][k];
					if(result[j][k] != null)
						stoplength++;
				}
				temp[j][stoplength] = temparray.get(i);
				if(getsolverresult(param, currentFileDir, name, temp[j]))
					return true;
			}
			temp[currentlength][0] = temparray.get(i);
			if(getsolverresult(param, currentFileDir, name, temp[currentlength]))
				return true;
			for(int j = 0; j < currentlength + 1; j++)
			{
				result[currentlength+j] = temp[j];
			}
			currentlength = currentlength + currentlength + 1;
		}
		return false;
	}
	
	private List<String> checkdeadsubsets(int length, List<String> temparray, JsonObject param, File currentFileDir, String name) {
		List<String> cache = new ArrayList<String>();
		for(int i = 0; i < length; i++)
		{
			cache.add(i, temparray.get(i));
		}
		String[][] result = new String [(int) Math.pow(2,length)][length];
		if(temparray.size() == 0)
			return cache;
		result[0][0] = temparray.get(0);
		if(getsolverresult(param, currentFileDir, name, result[0]))
			cache.remove(result[0][0]);
		int currentlength = 1;
		for(int i = 1 ; i < length; i++)
		{
			String[][] temp = new String [currentlength+1][length];
			for(int j = 0; j < currentlength; j++)
			{
				int stoplength = 0;
				for(int k = 0 ; k < length; k++)
				{
					temp[j][k]= result[j][k];
					if(result[j][k] != null)
						stoplength++;
				}
				
				temp[j][stoplength] = temparray.get(i);
				if(getsolverresult(param, currentFileDir, name, temp[j]))
				{
					for(int a = 0; a < stoplength + 1; a++)
					{
						if(cache.contains(temp[j][a]))
							cache.remove(temp[j][a]);
					}
					if(cache.size() == 0)
						return cache;
				}
			}
			temp[currentlength][0] = temparray.get(i);
			if(getsolverresult(param, currentFileDir, name, temp[currentlength]) && cache.contains(temp[currentlength][0]))
				cache.remove(temp[currentlength][0]);
			if(cache.size() == 0)
				return cache;
			for(int j = 0; j < currentlength + 1; j++)
			{
				result[currentlength+j] = temp[j];
			}
			currentlength = currentlength + currentlength + 1;
		}
		return cache;
	}
}
