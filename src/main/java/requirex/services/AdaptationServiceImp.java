package requirex.services;

import java.util.List;

import org.springframework.stereotype.Service;

import requirex.models.dao.AdaptationDaoImp;
import requirex.models.entitys.Adaptation;

@Service
public class AdaptationServiceImp implements IAdaptationService {

	AdaptationDaoImp adaptationDao = new AdaptationDaoImp();
	
	@Override
	public List<Adaptation> findAll() {
		return adaptationDao.findAll();
	}

	@Override
	public List<Adaptation> findAllByEstado(boolean estado) {
		return adaptationDao.findAllByEstado(estado);
	}

	@Override
	public Adaptation findById(int id) {
		return adaptationDao.findById(id);
	}

	@Override
	public int findTotalByEstado(boolean estado) {
		return adaptationDao.findAllByEstado(estado).size();
	}

	@Override
	public Adaptation save(Adaptation adaptation) {
		return adaptationDao.save(adaptation);
	}

	@Override
	public Adaptation update(Adaptation adaptation) {
		return adaptationDao.update(adaptation);
	}

}
