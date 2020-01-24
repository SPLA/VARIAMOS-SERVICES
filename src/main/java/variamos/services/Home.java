package variamos.services;

import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
public class Home {
	
	@CrossOrigin
    @RequestMapping("/")
    String home() {
    	return "Back-end Server Running properly...";
    }
}
