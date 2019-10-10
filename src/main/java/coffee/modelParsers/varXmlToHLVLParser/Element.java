package coffee.modelParsers.varXmlToHLVLParser;

/**
* this class repesent a root, general and leaf element from the XML tree
* 
* @author Joan David Colina Echeverry
* modified on 25th January by avillota
*/
public class Element {

	/**
	 * @param String: Element's name
	 */
	private String name;
	/**
	 * @param String: Element's id
	 */
	private String id;
	/**
	 * @param String: Element's type
	 */
	private String type;
	/**
	 * @param String: Element's selected
	 */
	private String selected;
	/**
	 * @param String: Element's bundleType
	 */
	private String bundleType;
	
	/**
	 * this method return the element's name
	 * 
	 * @return String: element's name
	 */
	public String getName() {
		return name;
	}
	/**
	 * this method change element's name value for paramater.
	 * 
	 * @param name: element's name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * this method return the element's Id
	 * 
	 * @return String: element's name
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * this method change element's id value for paramater.
	 * 
	 * @param id: element's id
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * this method return the element's type
	 * 
	 * @return String: element's type
	 */
	public String getType() {
		return type;
	}
	/**
	 * this method change element's type value for paramater.
	 * 
	 * @param type: element's type
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * this method return the element's selected
	 * 
	 * @return String: element's selected
	 */
	public String getSelected() {
		return selected;
	}
	/**
	 * this method change element's selected value for paramater.
	 * 
	 * @param selected: element's selected
	 */
	public void setSelected(String selected) {
		this.selected = selected;
	}
	/**
	 * this method return the element's BoundleType
	 * 
	 * @return String: element's boundleType
	 */
	public String getBundleType() {
		return bundleType;
	}
	/**
	 * this method change element's boundleType value for paramater.
	 * 
	 * @param bundleType: element's boundleType
	 */
	public void setBundleType(String bundleType) {
		this.bundleType = bundleType;
	}

}
