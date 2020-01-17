package requirex.services;

import java.util.List;

import requirex.models.entitys.Adaptation;

public interface IAdaptationService {

	public List<Adaptation> findAll();

	public List<Adaptation> findAllByEstado(boolean estado);

	public Adaptation findById(int id);

	public int findTotalByEstado(boolean estado);

	public Adaptation save(Adaptation adaptation);

	public Adaptation update(Adaptation adaptation);
}
