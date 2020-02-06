package variamos.models.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import variamos.models.entitys.Domain;

public interface IDomainDao extends CrudRepository<Domain, Long> {
	
	public List<Domain> findByEstado(boolean estado);

}
