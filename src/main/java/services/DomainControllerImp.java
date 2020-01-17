package services;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import requirex.models.entitys.Domain;
import requirex.services.DomainServiceImp;

@RestController
@RequestMapping("/requirex")
public class DomainControllerImp implements IDomainController {
	
	DomainServiceImp domainService = new DomainServiceImp();

	@Override
	@GetMapping("/domains")
	public List<Domain> findAll() {
		return domainService.findAll();
	}

	@Override
	@GetMapping("/domains/byestaso/{estado}")
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
	public Domain save(Domain domain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Domain update(Domain domain) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
