package services;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import requirex.models.entitys.Adaptation;
import requirex.models.entitys.Application;
import requirex.services.ApplicationServiceImp;

@RestController
@RequestMapping("/requirex")
public class ApplicationControllerImp implements IApplicationController{
	
	private ApplicationServiceImp applicationService = new ApplicationServiceImp();	

	@GetMapping("/applications")
	@Override
	public List<Application> findAll() {
		return applicationService.findAll();
	}
	
	@GetMapping("/applications/byestado/{estado}")
	@Override
	public List<Application> findAllByEstado(@PathVariable boolean estado) {
		return applicationService.findAllByEstado(estado);
	}

	@GetMapping("/applications/{id}")
	@Override
	public Application findById(@PathVariable int id) {
		return applicationService.findById(id);
	}

	@GetMapping("/applications/bytotal/{estado}")
	@Override
	public int findTotalByEstado(@PathVariable boolean estado) {
		return applicationService.findTotalByEstado(estado);
	}
	@PostMapping("/applications")
	@Override
	public Application save(Application application) {
		// TODO Auto-generated method stub
		return null;
	}

	@PutMapping("/applications")
	@Override
	public Application update(Application application) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
