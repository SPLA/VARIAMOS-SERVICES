package variamos.models.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import variamos.models.entitys.Application;


public interface IApplicationDao extends CrudRepository<Application, Long> {
	
	public List<Application> findByEstado(boolean estado);
	
}
