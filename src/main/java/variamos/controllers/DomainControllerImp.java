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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import variamos.models.entitys.Domain;
import variamos.models.services.IDomainService;


@RestController
@RequestMapping("/requirex")
@CrossOrigin
public class DomainControllerImp implements IDomainController {
	
	@Autowired
	private IDomainService domainService;

	@Override
	@GetMapping("/domains")
	public List<Domain> findAll() {
		return domainService.findAll();
	}

	@Override
	@GetMapping("/domains/byestado/{estado}")
	public List<Domain> findByEstado(@PathVariable boolean estado) {
		return domainService.findByEstado(estado);
	}

	@Override
	@GetMapping("/domains/{id}")
	public Domain findById(@PathVariable int id) {
		return domainService.findById(id);
	}

	@Override
	@GetMapping("/domains/bytotal/{estado}")
	public int getTotal(@PathVariable boolean estado) {
		return domainService.getTotal(estado);
	}

	@Override
	@PostMapping("/domains")
	@ResponseStatus(HttpStatus.CREATED)
	public Domain save(@RequestBody Domain domain) {
		return domainService.save(domain);
	}

	@Override
	@PutMapping("/domains/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Domain update(@RequestBody Domain domain, @PathVariable long id) {
		
		Domain domainActual = domainService.findById(id);
		
		domainActual.setAffectedSystems(domain.getAffectedSystems());
		domainActual.setCondition(domain.isCondition());
		domainActual.setConditionDescription(domain.getConditionDescription());
		domainActual.setFrom(domain.getFrom());
		domainActual.setImperative(domain.getImperative());
		domainActual.setMsg(domain.getMsg());
		domainActual.setName(domain.getName());
		domainActual.setObject(domain.getObject());
		domainActual.setProcessVerb(domain.getProcessVerb());
		domainActual.setReqType(domain.getReqType());
		domainActual.setSystem(domain.getSystem());
		domainActual.setSystemActivity(domain.getSystemActivity());
		domainActual.setSystemCondition(domain.isSystemCondition());
		domainActual.setSystemConditionDescription(domain.getSystemConditionDescription());
		domainActual.setSystemName(domain.getSystemName());
		domainActual.setThoseCodition(domain.getThoseCodition());
		domainActual.setUser(domain.getUser());
		
		return domainService.save(domainActual);
	}

	@DeleteMapping("/domains/{id}")
	@Override
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable long id) {
		Domain domainActual = domainService.findById(id);
		
		domainActual.setEstado(false);
		
		domainService.save(domainActual);
	}
	
	

}
