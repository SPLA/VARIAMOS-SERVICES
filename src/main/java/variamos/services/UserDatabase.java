package variamos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import variamos.models.User;
import variamos.models.UserRepository;

@RestController
public class UserDatabase {
	
	@Autowired
	UserRepository UserRepository;

    @GetMapping("/UserDatabase/insert")
    public void insert(){
    	UserRepository.save(new User("Luis"));
    }
}