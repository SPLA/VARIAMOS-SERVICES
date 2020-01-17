package services;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import requirex.models.entitys.Domain;
import requirex.services.DomainServiceImp;

@RestController
@RequestMapping("/requirex")
public class DomainController implements IDomainController {
	
	DomainServiceImp domainService = new DomainServiceImp();

	@Override
	@GetMapping("/domains")
	public List<Domain> findAll() {
		return domainService.findAll();
	}

	@Override
	public List<Domain> findByEstado(boolean estado) {
		return domainService.findByEstado(estado);
	}

	@Override
	public Domain findById(int id) {
		return domainService.findById(id);
	}

	@Override
	public int getTotal(boolean estado) {
		return domainService.getTotal(estado);
	}
	
	

}
