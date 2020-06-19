package variamos.models.entitys;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sun.istack.Nullable;

@Entity
public class Adaptation implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String requirementNumber;
	private String reqType;
	private String name;
	@Column(name = "`condition")
	private boolean condition;
	
	@Column(nullable = true)
	private String conditionDescription;
	private String imperative;
	private String systemName;
	private String processVerb;
	private String object;
	@Column(name = "`system")
	private String system;
	private String relaxing;
	private String postBehaviour;
	private String event;
	private int timeInterval;
	private String units;
	private String quantity;
	private String frecuency;
	private String quantityFrecuency;
	private String msg;
	private boolean estado;
	
	@Temporal(TemporalType.DATE)
	private Date fechaRegistro;
	
	@PrePersist
	public void prePersist () {
		this.fechaRegistro = new Date();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRequirementNumber() {
		return requirementNumber;
	}
	public void setRequirementNumber(String requirementNumber) {
		this.requirementNumber = requirementNumber;
	}
	public String getReqType() {
		return reqType;
	}
	public void setReqType(String reqType) {
		this.reqType = reqType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getCondition() {
		return condition;
	}
	public void setCondition(Boolean condition) {
		this.condition = condition;
	}
	public String getConditionDescription() {
		return conditionDescription;
	}
	public void setConditionDescription(String conditionDescription) {
		this.conditionDescription = conditionDescription;
	}
	public String getImperative() {
		return imperative;
	}
	public void setImperative(String imperative) {
		this.imperative = imperative;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getProcessVerb() {
		return processVerb;
	}
	public void setProcessVerb(String processVerb) {
		this.processVerb = processVerb;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getRelaxing() {
		return relaxing;
	}
	public void setRelaxing(String relaxing) {
		this.relaxing = relaxing;
	}
	public String getPostBehaviour() {
		return postBehaviour;
	}
	public void setPostBehaviour(String postBehaviour) {
		this.postBehaviour = postBehaviour;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public int getTimeInterval() {
		return timeInterval;
	}
	public void setTimeInterval(int timeInterval) {
		this.timeInterval = timeInterval;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getFrecuency() {
		return frecuency;
	}
	public void setFrecuency(String frecuency) {
		this.frecuency = frecuency;
	}
	public String getQuantityFrecuency() {
		return quantityFrecuency;
	}
	public void setQuantityFrecuency(String quantityFrecuency) {
		this.quantityFrecuency = quantityFrecuency;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Boolean getEstado() {
		return estado;
	}
	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	
	
}
