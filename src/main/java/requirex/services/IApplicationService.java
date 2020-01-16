package requirex.services;

import java.util.List;

import requirex.models.entitys.Application;

public interface IApplicationService {

	public List<Application> findAll();

	public List<Application> findAllByEstado(boolean estado);

	public Application findById(int id);
	
	public int findTotalByEstado(boolean estado);

}
