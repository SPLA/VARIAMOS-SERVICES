package requirex.services;

import java.util.List;

import org.springframework.stereotype.Service;

import requirex.models.dao.ApplicationDaoImp;
import requirex.models.entitys.Application;

@Service
public class ApplicationServiceImp implements IApplicationService {

	private ApplicationDaoImp applicationDao = new ApplicationDaoImp();
	
	@Override
	public List<Application> findAll() {
		return applicationDao.findAll();
	}

	@Override
	public List<Application> findAllByEstado(boolean estado) {
		return applicationDao.findAllByEstado(estado);
	}

	@Override
	public Application findById(int id) {
		return applicationDao.findById(id);
	}

	@Override
	public int findTotalByEstado(boolean estado) {
		return applicationDao.findAllByEstado(estado).size();
	}

	@Override
	public Application save(Application application) {
		return applicationDao.save(application);
	}

	@Override
	public Application update(Application application) {
		return applicationDao.update(application);
	}
	
	

}
