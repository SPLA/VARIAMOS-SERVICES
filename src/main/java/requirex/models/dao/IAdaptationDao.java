package requirex.models.dao;

import java.util.List;

import requirex.models.entitys.Adaptation;


public interface IAdaptationDao {
	
public List<Adaptation> findAll();
	
	public List<Adaptation> findAllByEstado(boolean estado);
	
	public Adaptation findById(int id);

}
