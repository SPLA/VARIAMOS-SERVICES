package variamos.models.services;

import java.util.List;

import variamos.models.entitys.Adaptation;


public interface IAdaptationService {

	public List<Adaptation> findAll();

	public List<Adaptation> findByEstado(boolean estado);

	public Adaptation findById(Long id);

	public int findTotalByEstado(boolean estado);

	public Adaptation save(Adaptation adaptation);

}
