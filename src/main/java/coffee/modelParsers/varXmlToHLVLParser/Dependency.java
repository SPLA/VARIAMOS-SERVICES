package coffee.modelParsers.varXmlToHLVLParser;

/**
 * This class repesent a mandatory, optional, excludes, requires and group
 * relations bewteen element from the XML tree
 * 
 * @author Joan David Colina Echeverry 
 * modified on 25th January by avillota
 */

public class Dependency {

	/**
	 * @param String: dependecy's type
	 */
	String type;
	/**
	 * @param String: dependecy's relType
	 */
	String relType;
	/**
	 * @param String: dependecy's id
	 */
	String id;
	/**
	 * @param String: dependecy's source
	 */
	String source;
	/**
	 * @param String: dependecy's target
	 */
	String target;

	/**
	 * this method return the Dependcyt's type
	 * 
	 * @return String: Dependecy's type
	 */
	public String getType() {
		return type;
	}

	/**
	 * this method change dependecy's type value for paramater.
	 * 
	 * @param type: dependecy's type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * this method return the Dependcyt's RelType
	 * 
	 * @return String: Dependecy's RelType
	 */
	public String getRelType() {
		return relType;
	}

	/**
	 * this method change dependecy's relType value for paramater.
	 * 
	 * @param relType: dependecy's relType
	 */
	public void setRelType(String relType) {
		this.relType = relType;
	}

	/**
	 * this method return the Dependcyt's Id
	 * 
	 * @return String: Dependecy's id
	 */
	public String getId() {
		return id;
	}

	/**
	 * this method change dependecy's relType value for paramater.
	 * 
	 * @param id: dependecy's relType
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * this method return the Dependcyt's source
	 * 
	 * @return String: Dependecy's source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * this method change dependecy's source value for paramater.
	 * 
	 * @param source: dependecy's source
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * this method return the Dependcyt's target
	 * 
	 * @return String: Dependecy's target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * this method change dependecy's target value for paramater.
	 * 
	 * @param target: dependecy's target
	 */
	public void setTarget(String target) {
		this.target = target;
	}

}
