package variamos.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import variamos.models.dao.IAdaptationDao;
import variamos.models.entitys.Adaptation;


@Service
public class AdaptationServiceImp implements IAdaptationService {

	@Autowired
	private IAdaptationDao adaptationDao;
	
	@Override
	public List<Adaptation> findAll() {
		return (List<Adaptation>) adaptationDao.findAll();
	}

	@Override
	public List<Adaptation> findByEstado(boolean estado) {
		return adaptationDao.findByEstado(estado);
	}

	@Override
	public Adaptation findById(Long id) {
		return adaptationDao.findById(id).orElse(null);
	}

	@Override
	public int findTotalByEstado(boolean estado) {
		return adaptationDao.findByEstado(estado).size();
	}

	@Override
	public Adaptation save(Adaptation adaptation) {
		return adaptationDao.save(adaptation);
	}

	

}
