package services;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DomainImplementation {

	@CrossOrigin
	@RequestMapping(value="/DomainImplementation/execute", method=RequestMethod.GET, produces="text/plain")
	@ResponseBody
	public String executeDerivation(Model model) {
		return "executing...";
	}
	
}
