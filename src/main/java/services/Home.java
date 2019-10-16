package services;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Home {
	@CrossOrigin
	@RequestMapping(value="/", method=RequestMethod.GET, produces="text/plain")
	@ResponseBody
	public String home() {
		return "Back-end Server Running properly...";
	}
	
}
