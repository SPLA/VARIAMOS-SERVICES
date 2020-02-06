package variamos.controllers;

import java.util.List;

import variamos.models.entitys.Domain;

public interface IDomainController {
	
	public List<Domain> findAll();

	public List<Domain> findByEstado(boolean estado);

	public Domain findById(int id);
	
	public int getTotal(boolean estado);

	public Domain save(Domain domain);

	public Domain update(Domain domain, long id);
	
	public void delete(long id);
}
