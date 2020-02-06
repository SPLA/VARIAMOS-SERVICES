package variamos.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import variamos.models.dao.IDomainDao;
import variamos.models.entitys.Domain;

@Service
public class DomainServiceImp implements IDomainService {
	
	@Autowired
	private IDomainDao domainDao;

	@Override
	public List<Domain> findAll() {
		return (List<Domain>) domainDao.findAll();
	}

	@Override
	public List<Domain> findByEstado(boolean estado) {
		return domainDao.findByEstado(estado);
	}

	@Override
	public Domain findById(long id) {
		return domainDao.findById(id).orElse(null);
	}

	@Override
	public int getTotal(boolean estado) {
		return domainDao.findByEstado(estado).size();
	}

	@Override
	public Domain save(Domain domain) {
		return domainDao.save(domain);
	}


}
