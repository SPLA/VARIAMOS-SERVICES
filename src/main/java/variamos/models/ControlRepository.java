package variamos.models;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ControlRepository extends CrudRepository<Control, Integer> {
	
	public Control findAllByName(String  name);

}
