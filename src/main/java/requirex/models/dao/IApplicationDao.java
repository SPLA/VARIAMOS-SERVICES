package requirex.models.dao;

import java.util.List;

import requirex.models.entitys.Application;

public interface IApplicationDao {

	public List<Application> findAll();
	
	public List<Application> findAllByEstado(boolean estado);
	
	public Application findById(int id);

	public Application save(Application application);

	public Application update(Application application);
	
}
