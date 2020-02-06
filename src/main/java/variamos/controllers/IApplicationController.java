package variamos.controllers;

import java.util.List;

import variamos.models.entitys.Application;


public interface IApplicationController {

	public List<Application> findAll();

	public List<Application> findAllByEstado(boolean estado);

	public Application findById(int id);

	public int findTotalByEstado(boolean estado);

	public Application save(Application application);

	public Application update(Application application, long id);
	
	public void delete(long id);

}
