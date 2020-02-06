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

@Entity
public class Domain implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;
    public String requirementNumber;
    public String affectedSystems;
    public String thoseCodition;
    public String reqType;
    public String name;

	@Column(name = "`condition")
    public boolean condition;

	@Column(nullable = true)
    public String conditionDescription;
    public String imperative;
    public String systemName;
    public String systemActivity;
    public String user;
    public String processVerb;
    public String object;

	@Column(name ="`system")
    public String system;

	@Column(name = "`from")
    public String from;
    public boolean systemCondition;

	@Column(nullable = true)
    public String systemConditionDescription;
    public String msg;
    public boolean estado;
    
    @Column(name = "fechaRegistro")
	@Temporal(TemporalType.DATE)
	private Date fecha_registro;
	
	@PrePersist
	public void prePersist () {
		this.fecha_registro = new Date();
	}
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getRequirementNumber() {
		return requirementNumber;
	}
	public void setRequirementNumber(String requirementNumber) {
		this.requirementNumber = requirementNumber;
	}
	public String getAffectedSystems() {
		return affectedSystems;
	}
	public void setAffectedSystems(String affectedSystems) {
		this.affectedSystems = affectedSystems;
	}
	public String getThoseCodition() {
		return thoseCodition;
	}
	public void setThoseCodition(String thoseCodition) {
		this.thoseCodition = thoseCodition;
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
	public boolean isCondition() {
		return condition;
	}
	public void setCondition(boolean condition) {
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
	public String getSystemActivity() {
		return systemActivity;
	}
	public void setSystemActivity(String systemActivity) {
		this.systemActivity = systemActivity;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
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
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public boolean isSystemCondition() {
		return systemCondition;
	}
	public void setSystemCondition(boolean systemCondition) {
		this.systemCondition = systemCondition;
	}
	public String getSystemConditionDescription() {
		return systemConditionDescription;
	}
	public void setSystemConditionDescription(String systemConditionDescription) {
		this.systemConditionDescription = systemConditionDescription;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public Date getFecha_registro() {
		return fecha_registro;
	}
	public void setFecha_registro(Date fecha_registro) {
		this.fecha_registro = fecha_registro;
	}
    
    

}
