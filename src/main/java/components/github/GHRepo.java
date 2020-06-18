package components.github;

/**
 * GitHub repository class operations
 */
public class GHRepo {
	/**
	 * Repository name
	 */
	String name = "";
	/**
	 * Repository description
	 */
	String description = "";
	/**
	 * Repository home page URL
	 */
	String homepage = "";
	/**
	 * ???????
	 */
	Boolean autoInit = true;
	
	/**
	 * Default constructor without any parameters
	 */
	public GHRepo(){
		
	}
	
	/**
	 * Repository class construtor for initialization.
	 * @param name Repository name
	 * @param description Repository descriptiom
	 * @param homepage Repository home page URL
	 */
	public GHRepo(String name, String description, String homepage){
		this.name=name;
		this.description=description;
		this.homepage=homepage;
	}
	
}
