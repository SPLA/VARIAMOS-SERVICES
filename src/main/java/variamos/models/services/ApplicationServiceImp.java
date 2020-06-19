package variamos.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import variamos.models.dao.IApplicationDao;
import variamos.models.entitys.Application;

@Service
public class ApplicationServiceImp implements IApplicationService {

	@Autowired
	private IApplicationDao applicationDao;
	
	@Override
	public List<Application> findAll() {
		return (List<Application>) applicationDao.findAll();
	}

	@Override
	public List<Application> findByEstado(boolean estado) {
		return applicationDao.findByEstado(estado);
	}

	@Override
	public Application findById(long id) {
		return applicationDao.findById(id).orElse(null);
	}

	@Override
	public int findTotalByEstado(boolean estado) {
		return applicationDao.findByEstado(estado).size();
	}

	@Override
	public Application save(Application application) {
		return applicationDao.save(application);
	}

	
	

}
