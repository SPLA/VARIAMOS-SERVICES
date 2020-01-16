package services;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import requirex.models.entitys.Application;
import requirex.services.ApplicationServiceImp;

@RestController
@RequestMapping("/requirex")
public class ApplicationController {
	
	private ApplicationServiceImp applicationService = new ApplicationServiceImp();	

	@GetMapping("/applications")
	public List<Application> findAll() {
		return applicationService.findAll();
	}
	
	@GetMapping("/applications/byEstado/{estado}")
	public List<Application> findAllByEstado(@PathVariable boolean estado) {
		return applicationService.findAllByEstado(estado);
	}

	@GetMapping("/applications/{id}")
	public Application findById(@PathVariable int id) {
		return applicationService.findById(id);
	}

	@GetMapping("/applications/total/{estado}")
	public int findTotalByEstado(@PathVariable boolean estado) {
		return applicationService.findTotalByEstado(estado);
	}
}
