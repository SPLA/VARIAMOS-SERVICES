package services;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import requirex.models.entitys.Adaptation;
import requirex.services.AdaptationServiceImp;

@RestController
@RequestMapping("/requirex")
public class AdaptationControllerImp implements IAdaptationController{
	
	AdaptationServiceImp adaptationService = new AdaptationServiceImp();

	@GetMapping("/adaptations")
	@Override
	public List<Adaptation> findAll() {
		return adaptationService.findAll();
	}

	@GetMapping("/adaptations/byestado/{estado}")
	@Override
	public List<Adaptation> findAllByEstado(@PathVariable boolean estado) {
		return adaptationService.findAllByEstado(estado);
	}

	@GetMapping("/adaptations/{id}")
	@Override
	public Adaptation findById(@PathVariable int id) {
		return adaptationService.findById(id);
	}

	@GetMapping("/adaptations/bytotal/{estado}")
	@Override
	public int findTotalByEstado(@PathVariable boolean estado) {
		return adaptationService.findTotalByEstado(estado);
	}

	@PostMapping("/adaptations")
	@Override
	public Adaptation save(Adaptation adaptation) {
		return adaptationService.save(adaptation);
	}

	@PutMapping("/adaptations")
	@Override
	public Adaptation update(Adaptation adaptation) {
		return adaptationService.update(adaptation);
	}

}
