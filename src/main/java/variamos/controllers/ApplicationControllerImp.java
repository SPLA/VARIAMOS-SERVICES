package variamos.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import variamos.models.entitys.Application;
import variamos.models.services.IApplicationService;


@RestController
@RequestMapping("/requirex")
@CrossOrigin
public class ApplicationControllerImp implements IApplicationController{
	
	@Autowired
	private IApplicationService applicationService;	

	@GetMapping("/applications")
	@Override
	public List<Application> findAll() {
		return applicationService.findAll();
	}
	
	@GetMapping("/applications/byestado/{estado}")
	@Override
	public List<Application> findAllByEstado(@PathVariable boolean estado) {
		return applicationService.findByEstado(estado);
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
	@ResponseStatus(HttpStatus.CREATED)
	public Application save(Application application) {
		return applicationService.save(application);
	}

	@PutMapping("/applications/{id}")
	@Override
	@ResponseStatus(HttpStatus.CREATED)
	public Application update(Application application, @PathVariable long id) {
		
		Application applicationActual = applicationService.findById(id);
		
		applicationActual.setCondition(application.getCondition());
		applicationActual.setConditionDescription(application.getConditionDescription());
		applicationActual.setFrom(application.getFrom());
		applicationActual.setImperative(application.getImperative());
		applicationActual.setMsg(application.getMsg());
		applicationActual.setName(application.getName());
		applicationActual.setObject(application.getObject());
		applicationActual.setProcessVerb(application.getProcessVerb());
		applicationActual.setReqType(application.getReqType());
		applicationActual.setRequirementNumber(application.getRequirementNumber());
		applicationActual.setSystem(application.getSystem());
		applicationActual.setSystemActivity(application.getSystemActivity());
		applicationActual.setSystemCondition(application.getSystemCondition());
		applicationActual.setSystemConditionDescription(application.getSystemConditionDescription());
		applicationActual.setSystemName(application.getSystemName());
		applicationActual.setUser(application.getUser());
		
		return applicationService.save(applicationActual);
	}

	@DeleteMapping("/applications/{id}")
	@Override
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable long id) {
		Application application = applicationService.findById(id);
		
		application.setEstado(false);
		
		applicationService.save(application);
	}

	
}
