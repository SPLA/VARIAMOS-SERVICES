package requirex.services;

import java.util.List;

import requirex.models.dao.ApplicationDaoImp;
import requirex.models.entitys.Application;

public class ApplicationServiceImp implements IApplicationService {

	private ApplicationDaoImp applicationDao = new ApplicationDaoImp();
	
	@Override
	public List<Application> findAll() {
		return applicationDao.findAll();
	}

	@Override
	public List<Application> findAllByEstado(boolean estado) {
		// TODO Auto-generated method stub
		return applicationDao.findAllByEstado(estado);
	}

	@Override
	public Application findById(int id) {
		// TODO Auto-generated method stub
		return applicationDao.findById(id);
	}

	@Override
	public int findTotalByEstado(boolean estado) {
		return applicationDao.findAllByEstado(estado).size();
	}
	
	

}
