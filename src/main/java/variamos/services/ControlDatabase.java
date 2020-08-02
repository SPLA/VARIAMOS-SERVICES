package variamos.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import variamos.models.ControlRepository;
import variamos.models.Control;

@RestController
public class ControlDatabase {
	
	@Autowired
	ControlRepository ControlRepository;
    private String textArduino="";
	private String textVue="0";

	
	@CrossOrigin
	@RequestMapping(value = "/Control/dataVisualization", method =  RequestMethod.POST  ,produces="text/plain")
	@ResponseBody
	public String DataArduino(@RequestBody String data_collected) {		
		textArduino=data_collected;
		return textVue;
	}
	
	@CrossOrigin
	@RequestMapping(value="/Control/getData", method= RequestMethod.GET, produces="text/plain")
	@ResponseBody
	public String DataVuejs() {
		return textArduino;
	}
	
	
	@CrossOrigin
	@RequestMapping( value = "/Control/saveBD", method = RequestMethod.POST ,produces="text/plain")
	@ResponseBody
	public String SaveData(@RequestBody String data_collected) {
		if(data_collected.length()>5)
		{
			JSONObject json = new JSONObject(data_collected);
			String nameProject =   (String) json.get("name");
			Control myControl = ControlRepository.findAllByName(nameProject);
			if(myControl==null)	{
				ControlRepository.save(new Control(data_collected,nameProject));
			}
			else {
				myControl.setdata(data_collected);
				ControlRepository.save(myControl );
			}
		}
		else {
			textVue=data_collected;
		}
		
	return data_collected;
	}
    
}