package requirex.services;

import java.util.List;

import requirex.models.entitys.Domain;

public interface IDomainService {

	public List<Domain> findAll();

	public List<Domain> findByEstado(boolean estado);

	public Domain findById(int id);
	
	public int getTotal(boolean estado);

}
