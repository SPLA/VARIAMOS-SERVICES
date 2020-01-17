package requirex.models.dao;

import java.util.List;

import requirex.models.entitys.Domain;

public interface IDomainDao {
	
	public List<Domain> findAll();
	
	public List<Domain> findByEstado(boolean estado);
	
	public Domain findById(int id);
	

}
