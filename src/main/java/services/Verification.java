package services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
//		System.out.println(param.toString());
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
		File currentFileDir = verifyDirectory(project_direction+"testfiles\\");
		
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
		
		//new org.eclipse.emf.mwe.utils.StandaloneSetup().setPlatformUri("C:/");
//		Injector injector = new HlvlStandaloneSetup().createInjectorAndDoEMFRegistration();
//		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
//		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
//		Resource resource = resourceSet.createResource(URI.createURI("example.hlvl"));
//
//		InputStream in = new ByteArrayInputStream(result.getBytes());
//		try {
//			resource.load(in, resourceSet.getLoadOptions());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.exit(-2);
//		}
//		resultModel = (Model) resource.getContents().get(0);
//		System.out.println(resultModel.toString());
//		String jsonStr = null;
//		try {
//			jsonStr = EMF2JSON(resultModel);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		

//		
//		File currentModelFile = new File(currentFileDir.getAbsolutePath()+"/1.json");
//		//System.out.println(currentModelFile.getAbsolutePath());
//		BufferedWriter bw;
//		try {
//			bw = new BufferedWriter(new FileWriter(currentModelFile));
//			bw.write(jsonStr);
//			bw.close();	
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
		String frontEndData = "{\n";
		frontEndData += "\"solverSelected\" : \""+Solver_selected+"\",\n";
		frontEndData += "\"problemType\" : \""+"BOOL"+"\",\n";
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
		
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder().add("solution", solution).add("hlvl", "HLVL:\n"+result);
		javax.json.JsonObject response_result = objectBuilder.build();
		
		System.out.println("End");
		return response_result.toString();
	}
	
	private static File verifyDirectory(String dir) {
		File fileDir = new File(dir);
		//System.out.println("fileDir: "+fileDir.getAbsolutePath());
		if(!fileDir.exists()) fileDir.mkdir();
		return fileDir;
	}
}
