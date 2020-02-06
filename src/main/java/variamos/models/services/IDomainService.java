package variamos.models.services;

import java.util.List;

import variamos.models.entitys.Domain;

public interface IDomainService {

	public List<Domain> findAll();

	public List<Domain> findByEstado(boolean estado);

	public Domain findById(long id);

	public int getTotal(boolean estado);

	public Domain save(Domain domain);


}
