package variamos.models.services;

import java.util.List;

import variamos.models.entitys.Application;

public interface IApplicationService {

	public List<Application> findAll();

	public List<Application> findByEstado(boolean estado);

	public Application findById(long id);

	public int findTotalByEstado(boolean estado);

	public Application save(Application application);

}
