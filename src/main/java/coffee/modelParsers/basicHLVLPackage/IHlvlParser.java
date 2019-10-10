package coffee.modelParsers.basicHLVLPackage;

/**
 * Interface declaring the methods that must be included in a parser
 * for a feature model into the basic dialect of HLVL (Hlvl(basic))
 * @author Angela Villota
 * Coffee V1
 * January 2019
 */
public interface IHlvlParser {
	
	/**
	 * Method to formatting the name of an element in a valid name 
	 * for hlvl (no spaces, no symbols, etc)
	 * @param name is the identifier of the element
	 * @return a String with formatted name
	 */
	public String getValidName(String name);
	
	/**
	 * Method 
	 * @throws Exception
	 */
	public void parse() throws Exception;
	
	/**
	 * Method 
	 * @throws Exception: 
	 */
	public String parse(String data, String name)throws Exception;
	
	


}
