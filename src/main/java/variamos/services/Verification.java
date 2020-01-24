package variamos.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObjectBuilder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coffee.compiler.CompilationParameters;
import com.coffee.compiler.Compiler;
import com.coffee.compiler.CompilerAnswer;
import com.coffee.compiler.SourceOfCompilation;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import coffee.modelParsers.varXmlToHLVLParser.VariamosXMLToHlvlParser;
import commandExecutor.CmdExecutor;

@Controller
public class Verification {
	
	/**
	 * @param hlvl_parser the parser from xml of VariamosWeb to HLVL
	 * @TODO project_direction need to find a good way to execute HLVLparser.jar
	 * @TODO Solver_config the list of coffee solvers are not completed.
	 * @TODO Solver_selected currently, Gecode is default solver.
	 * @param Frontend_config the name of configuration for solver.
	 */
	private static VariamosXMLToHlvlParser hlvl_parser = new VariamosXMLToHlvlParser();
	private String project_direction = Compiler.class.getProtectionDomain().getCodeSource().getLocation().getPath().split("compiler.jar")[0].substring(1);
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
	@RequestMapping(value="/Verification/check_void", method=RequestMethod.POST, produces="text/plain")
	@ResponseBody
	public String check_void(@RequestBody String data_collected) {
		JsonParser parser = new JsonParser();
		JsonObject rootObj = parser.parse(data_collected).getAsJsonObject();
		JsonElement data = rootObj.get("data");
		String name = rootObj.get("name").getAsString();
		name = name.replaceAll("\\s","");
		name = name.replaceAll("-","");
		String data_string = data.getAsString();
		JsonObject param = rootObj.get("param").getAsJsonObject();

		// set the path for all the files
		File currentFileDir = verifyDirectory(project_direction+"testfiles\\");
		
		// run xmltoHLVL parser, HLVL parser, manage coffee solver lists
		parsehlvl(data_string, name, currentFileDir, param);
		
		// send empty object to get one solution from solver
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
		javax.json.JsonObject selections = objectBuilder.build();
		String returnmessage = "";
		if(getsolverresult(selections, currentFileDir, name))
			returnmessage = "There is at least one solution.";
		else
			returnmessage = "There is no solution.";
		System.out.println("End");
		return returnmessage;
	}
	
	@CrossOrigin
	@RequestMapping(value="/Verification/check_false", method=RequestMethod.POST, produces="text/plain")
	@ResponseBody
	public String check_false(@RequestBody String data_collected) {
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
		
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
		javax.json.JsonObject selections = objectBuilder.build();
		String frontEndData = 
				"{\n";
		frontEndData += "\"solverSelected\" : \""+"\",\n";
		frontEndData += "\"problemType\" : \""+"BAISC_BOOL"+"\",\n";
		frontEndData += "\"configuration\" : \n"+ selections.toString() +"\n";
		frontEndData += "}";
		
		File currentModelFile2 = new File(currentFileDir.getAbsolutePath()+ "\\src-gen\\" +Frontend_config+".json");
		BufferedWriter bw2;
		try {
			bw2 = new BufferedWriter(new FileWriter(currentModelFile2));
			bw2.write(frontEndData);
			bw2.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
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
		
		CompilerAnswer solution = null;
		try {
			solution = compiler.getSolutions(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String returnmessage = "";
		// check the first solution
		if(!solution.getState().contains("UNSATISFIABLE"))
		{
			returnmessage = "There is only one solution.";
			// get this first solution
			String[] parts = solution.getSolutions().iterator().next().toString().split("\n");
			// check the second solution based on first solution
			for(int j = 1; j < parts.length-1; j++)
			{
				JsonObjectBuilder Builder = Json.createObjectBuilder();
				if(parts[j].split(", ")[1].contains("true"))
				{
					Builder.add(parts[j].split(", ")[0], false);
				}
				else
				{
					Builder.add(parts[j].split(", ")[0], true);
				}
				javax.json.JsonObject sec = Builder.build();
				// if there is second solution
				if(getsolverresult(sec, currentFileDir, name))
				{
					returnmessage = "There are at least two solutions.";
					break;
				}
			}
		}
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
		
		// check every feature
		List<String> temparray = new ArrayList<String>();
		for (String keyStr : param.keySet()) {
			JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
			objectBuilder.add(keyStr, true);
			javax.json.JsonObject selections = objectBuilder.build();
			// if feature has no solution, add this feature to dead features
			if(!getsolverresult(selections, currentFileDir, name))
				temparray.add(keyStr);
	    }
		// return all the dead features
		JsonObjectBuilder Builder = Json.createObjectBuilder();
		for(int i = 0; i < temparray.size(); i++)
		{
			Builder.add("" + i, temparray.get(i));
		}
		javax.json.JsonObject response_result = Builder.build();
		System.out.println(response_result.toString());
		return response_result.toString();
	}
	
	@CrossOrigin
	@RequestMapping(value="/Verification/check_optional", method=RequestMethod.POST, produces="text/plain")
	@ResponseBody
	public String check_optional(@RequestBody String data_collected) {
		JsonParser parser = new JsonParser();
		JsonObject rootObj = parser.parse(data_collected).getAsJsonObject();
		JsonElement data = rootObj.get("data");
		String name = rootObj.get("name").getAsString();
		name = name.replaceAll("\\s","");
		name = name.replaceAll("-","");
		String data_string = data.getAsString();
		JsonObject param = rootObj.get("param").getAsJsonObject();
		JsonObject optionals = rootObj.get("optional").getAsJsonObject();

		File currentFileDir = verifyDirectory(project_direction+"testfiles\\");
		
		parsehlvl(data_string, name, currentFileDir, param);
		
		// check each optional feature
		List<String> temparray = new ArrayList<String>();
		for (String keyStr : optionals.keySet()) {
			//System.out.println("False optional:" + keyStr);
			
			String frontEndData = 
					"{\n";
			frontEndData += "\"solverSelected\" : \""+"\",\n";
			frontEndData += "\"problemType\" : \""+"BAISC_BOOL"+"\",\n";
			frontEndData += "\"configuration\" : \n"+ "{\""+keyStr + "\""  + ":true}" +"\n";
			frontEndData += "}";
			
			File currentModelFile2 = new File(currentFileDir.getAbsolutePath()+ "\\src-gen\\" +Frontend_config+".json");
			BufferedWriter bw2;
			try {
				bw2 = new BufferedWriter(new FileWriter(currentModelFile2));
				bw2.write(frontEndData);
				bw2.close();	
			} catch (IOException e) {
				e.printStackTrace();
			}	
			
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
			
			CompilerAnswer solution = null;
			try {
				solution = compiler.getSolutions(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// if optional feature has no solution, go to the next optional feature
			if(solution.getState().contains("UNSATISFIABLE"))
				continue;
			// if optional feature has one solution, get solutions from solver
			String[] parts = solution.getSolutions().iterator().next().toString().split("\n");
			JsonObjectBuilder Builder = Json.createObjectBuilder();
			// keep this solution and only change optional feature to false
			for(int j = 1; j < parts.length-1; j++)
			{
				if(parts[j].split(", ")[1].contains("true") && !parts[j].split(", ")[0].equals(keyStr))
				{
					Builder.add(parts[j].split(", ")[0], true);
				}
				else if(parts[j].split(", ")[1].contains("1") && !parts[j].split(", ")[0].equals(keyStr))
				{
					Builder.add(parts[j].split(", ")[0], true);
				}
				else
				{
					Builder.add(parts[j].split(", ")[0], false);
				}
			}
			javax.json.JsonObject selections = Builder.build();
			// if there is no solution, add this optional feature to false optional features
			if(!getsolverresult(selections, currentFileDir, name))
				temparray.add(keyStr);
	    }
		// return all the false optional features
		JsonObjectBuilder Builder = Json.createObjectBuilder();
		for(int i = 0; i < temparray.size(); i++)
		{
			Builder.add("" + i, temparray.get(i));
		}
		javax.json.JsonObject response_result = Builder.build();
		System.out.println(response_result.toString());
		return response_result.toString();
	}
	
	@CrossOrigin
	@RequestMapping(value="/Verification/check_multi_conflict", method=RequestMethod.POST, produces="text/plain")
	@ResponseBody
	public String check_multi_conflicts(@RequestBody String data_collected) {
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
		
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
		javax.json.JsonObject selections = objectBuilder.build();
		String returnmessage = "There is no multiplicity conflict.";
		// if feature model has no solution
		if(!getsolverresult(selections, currentFileDir, name))
		{
			String check_xml = rootObj.get("check_xml").getAsString();
			parsehlvl(check_xml, name, currentFileDir, param);
			// if feature model without cardinality has at least one solution
			if(getsolverresult(selections, currentFileDir, name))
				returnmessage = "There are some multiplicity conflicts.";
		}
		System.out.println("End");
		return returnmessage;
	}
	
	// return HLVL code to frontend
	@CrossOrigin
	@RequestMapping(value="/Verification/check_HLVL", method=RequestMethod.POST, produces="text/plain")
	@ResponseBody
	public String check_HLVL(@RequestBody String data_collected) {
		JsonParser parser = new JsonParser();
		JsonObject rootObj = parser.parse(data_collected).getAsJsonObject();
		JsonElement data = rootObj.get("data");
		String name = rootObj.get("name").getAsString();
		name = name.replaceAll("\\s","");
		name = name.replaceAll("-","");
		String data_string = data.getAsString();
		
		hlvl_parser = new VariamosXMLToHlvlParser();
		String result = "";
		try {
			result = hlvl_parser.parse(data_string, name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	// get results from solver based on input selections
	private boolean getsolverresult(javax.json.JsonObject param, File currentFileDir, String name) {
		System.out.println("Configuration: " + param.toString());
		
		// write input selections in frontend file
		String frontEndData = 
				"{\n";
		frontEndData += "\"solverSelected\" : \""+"\",\n";
		frontEndData += "\"problemType\" : \""+"BAISC_BOOL"+"\",\n";
		frontEndData += "\"configuration\" : \n"+ param.toString() +"\n";
		frontEndData += "}";
		
		File currentModelFile2 = new File(currentFileDir.getAbsolutePath()+ "\\src-gen\\" +Frontend_config+".json");
		BufferedWriter bw2;
		try {
			bw2 = new BufferedWriter(new FileWriter(currentModelFile2));
			bw2.write(frontEndData);
			bw2.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		// manage the inputs for solver
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
		
		// get one solution from solver
		CompilerAnswer solution = null;
		try {
			solution = compiler.getSolutions(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(solution.getState().contains("UNSATISFIABLE"))
			return false;
		return true;
	}
	
	// verify the test path
	private static File verifyDirectory(String dir) {
		File fileDir = new File(dir);
		if(!fileDir.exists()) fileDir.mkdir();
		return fileDir;
	}
	
	// run xmltoHLVL parser, HLVL parser, manage coffee solver lists
	private void parsehlvl(String data_string, String name, File currentFileDir, JsonObject param) {
		hlvl_parser = new VariamosXMLToHlvlParser();
		String result = "";
		try {
			result = hlvl_parser.parse(data_string, name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// write HLVL code in the file
		File currentModelFile1 = new File(currentFileDir.getAbsolutePath()+"/"+ name +".hlvl");
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
		
		// execute HLVLParser.jar
		String command = "java -jar " + project_direction + "HLVLParser.jar "+ name +".hlvl";
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
		
		// write coffee solver lists into file
		String coffeesolver = "{\r\n" + 
				"\"CSPSolver\": [\r\n" + 
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
				"			\"cmd\": \"Gecode\", \r\n" + 
				"            \"maxSolutions\": 10,\r\n" + 
				"            \"maxTime\":10000,\r\n" + 
				"            \"allInfo\": \"-v\",\r\n" + 
				"            \"allSolutions\" : \"-a\",\r\n" + 
				"            \"boundSolutions\" : \"-n\",\r\n" + 
				"            \"timeLimit\": \"--time-limit\"\r\n" + 
				"		}\r\n" + 
				"\r\n" + 
				"	]" +
				"}\r\n";
		
		File currentModelFile3 = new File(currentFileDir.getAbsolutePath()+ "\\src-gen\\" +Solver_config+".json");
		BufferedWriter bw3;
		try {
			bw3 = new BufferedWriter(new FileWriter(currentModelFile3));
			bw3.write(coffeesolver);
			bw3.close();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
