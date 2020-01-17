package requirex.services;

import java.util.List;

import org.springframework.stereotype.Service;

import requirex.models.dao.DomainDaoImp;
import requirex.models.entitys.Domain;

@Service
public class DomainServiceImp implements IDomainService {
	
	DomainDaoImp domainDao = new DomainDaoImp();

	@Override
	public List<Domain> findAll() {
		return domainDao.findAll();
	}

	@Override
	public List<Domain> findByEstado(boolean estado) {
		return domainDao.findByEstado(estado);
	}

	@Override
	public Domain findById(int id) {
		return domainDao.findById(id);
	}

	@Override
	public int getTotal(boolean estado) {
		return domainDao.findByEstado(estado).size();
	}

}
