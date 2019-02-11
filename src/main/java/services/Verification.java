package services;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Controller
public class Verification {
	
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
	
}
