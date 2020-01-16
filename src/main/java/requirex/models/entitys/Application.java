package requirex.models.entitys;

import java.io.Serializable;
import java.util.Date;

public class Application implements Serializable {

	private int id;
	private String requirementNumber;
	private String reqType;
	private String name;
	private Boolean condition;
	private String conditionDescription;
	private String imperative;
	private String systemName;
	private String systemActivity;
	private String user;
	private String processVerb;
	private String object;
	private String system;
	private String from;
	private Boolean systemCondition;
	private String systemConditionDescription;
	private String msg;
	private Boolean estado;
	private Date fechaRegistro;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	public Boolean getSystemCondition() {
		return systemCondition;
	}
	public void setSystemCondition(Boolean systemCondition) {
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
