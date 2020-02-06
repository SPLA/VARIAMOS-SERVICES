package variamos.models.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import variamos.models.entitys.Adaptation;

public interface IAdaptationDao extends CrudRepository<Adaptation, Long> {

	public List<Adaptation> findByEstado(boolean estado);

}
