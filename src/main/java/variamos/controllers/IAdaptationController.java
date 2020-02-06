package variamos.controllers;

import java.util.List;

import variamos.models.entitys.Adaptation;

public interface IAdaptationController {
	
	public List<Adaptation> findAll();

	public List<Adaptation> findAllByEstado(boolean estado);

	public Adaptation findById(Long id);

	public int findTotalByEstado(boolean estado);
	
	public Adaptation save(Adaptation adaptation);
	
	public Adaptation update(Adaptation adaptation, long id);
	
	public void delete (long id);
}
