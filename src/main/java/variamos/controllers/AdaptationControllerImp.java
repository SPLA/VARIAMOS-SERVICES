package variamos.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import variamos.models.entitys.Adaptation;
import variamos.models.services.IAdaptationService;


@RestController
@RequestMapping("/requirex")
@CrossOrigin
public class AdaptationControllerImp implements IAdaptationController{
	
	@Autowired
	private IAdaptationService  adaptationService;

	@GetMapping("/adaptations")
	@Override
	public List<Adaptation> findAll() {
		return adaptationService.findAll();
	}

	@GetMapping("/adaptations/byestado/{estado}")
	@Override
	public List<Adaptation> findAllByEstado(@PathVariable boolean estado) {
		return adaptationService.findByEstado(estado);
	}

	@GetMapping("/adaptations/{id}")
	@Override
	public Adaptation findById(@PathVariable Long id) {
		return adaptationService.findById(id);
	}

	@GetMapping("/adaptations/bytotal/{estado}")
	@Override
	public int findTotalByEstado(@PathVariable boolean estado) {
		return adaptationService.findTotalByEstado(estado);
	}

	@PostMapping("/adaptations")
	@Override
	@ResponseStatus(HttpStatus.CREATED)
	public Adaptation save(Adaptation adaptation) {
		return adaptationService.save(adaptation);
	}

	@PutMapping("/adaptations/{id}")
	@Override
	@ResponseStatus(HttpStatus.CREATED)
	public Adaptation update(Adaptation adaptation, @PathVariable long id) {
		Adaptation adaptationActual = adaptationService.findById(id);
		
		adaptationActual.setCondition(adaptation.getCondition());
		adaptationActual.setConditionDescription(adaptation.getConditionDescription());
		adaptationActual.setEvent(adaptation.getEvent());
		adaptationActual.setFrecuency(adaptation.getFrecuency());
		adaptationActual.setImperative(adaptation.getImperative());
		adaptationActual.setMsg(adaptation.getMsg());
		adaptationActual.setName(adaptation.getName());
		adaptationActual.setObject(adaptation.getObject());
		adaptationActual.setPostBehaviour(adaptation.getPostBehaviour());
		adaptationActual.setProcessVerb(adaptation.getProcessVerb());
		adaptationActual.setQuantity(adaptation.getQuantity());
		adaptationActual.setQuantityFrecuency(adaptation.getQuantityFrecuency());
		adaptationActual.setRelaxing(adaptation.getRelaxing());
		adaptationActual.setReqType(adaptation.getReqType());
		adaptationActual.setSystem(adaptation.getSystem());
		adaptationActual.setSystemName(adaptation.getSystemName());
		adaptationActual.setTimeInterval(adaptation.getTimeInterval());
		adaptationActual.setUnits(adaptation.getUnits());
		
		return adaptationService.save(adaptationActual);
	}

	@DeleteMapping("/adaptation/{id}")
	@Override
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable long id) {
		Adaptation adaptation = adaptationService.findById(id);
		adaptation.setEstado(false);
		adaptationService.save(adaptation);
	}


}
