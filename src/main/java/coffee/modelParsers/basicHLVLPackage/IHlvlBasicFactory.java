package coffee.modelParsers.basicHLVLPackage;

import java.util.List;

/**
 * Interface declaring the methods that must be implemented form parsing a variability model 
 * to a model in the basic dialect of HLVL (Hlvl(basic))
 * @author Angela Villota
 * Coffee V1
 * January 2019
 * October 2019, including the changes in the HLVL syntax
 */
public interface IHlvlBasicFactory {
	
	/**
	 * Produces an hlvl sentence with the declaration of a boolean element
	 * @param identifier
	 * @return string that represents the declaration of a hlvl boolean element 
	 */
	
	public String getElement(String identifier);
	
	/**
	 * Produces a commons relation 
	 * @param element, is a String for the identifiers of the element  
	 * @return a String with the hlvl sentences in the form common(E1)
	 */
	public String getCommon(String element);
	/**
	 * Produces a core relation for the elements that must be included in the
	 * product. 
	 * @param identifiers, is a list of the identifiers of the elements that  
	 * @return a String with the hlvl sentences in the form common(E1, E2, E3)
	 */
	public String getCommonList(List<String> identifiers);

	/**
	 * Produces an implies declaration of the form left => right
	 * @param left, the identifier of the left
	 * @param right, the identifier in the right side of the implication
	 * @return an hlvl sentence in the form implies(left, right)
	 */
	public String getImplies(String left, String right);
	
	/**
	 * Produces a mutex declaration for two elements that can't be included in a product at the same time
	 * @param left, the identifier of the left
	 * @param right, the identifier in the right side of the implication
	 * @return an hlvl sentence in the form mutex(left, right)
	 */
	public String getMutex(String left, String right);
	
	/**
	 * 
	 * @param parent id of the parent 
	 * @param child is an identifier
	 * @param type can be Mandatory or Optional, the decomposition types supported by HLVL(basic)
	 * @return an hlvl sentence in the form decomposition(parent, [c1], [m,n]) 
	 */
	public String getDecomposition(String parent, String child, DecompositionType type);
	
	/**
	 * 
	 * @param parent id of the parent 
	 * @param children list of the cildren's identifiers
	 * @param type can be Mandatory or Optional, the decomposition types supported by HLVL(basic)
	 * @return an hlvl sentence in the form decomposition(parent, [c1, c2, ..cn], [m,n]) 
	 */
	public String getDecompositionList(String parent, List <String> children, DecompositionType type);
	
	/**
	 * 
	 * @param parent id of the parent 
	 * @param children list of the cildren's identifiers
	 * @param type can be Alternative and Or, the group types supported by HLVL(basic)
	 * @return an hlvl sentence in the form group(parent, [c1, c2, ..cn], [m,n])
	 */
	public String getGroup(String parent, List <String> children, GroupType type);
	
	
	//TODO This definition may change
	//public String getExpression();
	
	/**
	 * 
	 * @param parent id of the parent 
	 * @param children list of the cildren's identifiers
	 * @param type can be Alternative and Or, the group types supported by HLVL(basic)
	 * @param range is the cardinality between low range and high range
	 * @return an hlvl sentence in the form group(parent, [c1, c2, ..cn], [m,n])
	 */
	public String getRangeGroup(String parent, List<String> children, GroupType type, String range);
	
	/**
	 * 
	 * @param positives
	 * @param negatives
	 * @return
	 */
	public String parseCNF2expression(List<String> positives, List<String> negatives);
	

	
	/**
	 * Produces the header of an HLVL file
	 * @param targetName a string with the name of the model (the same as the name of the file)
	 * @return a String with a header for the HLVL file
	 */
	public String getHeader(String targetName);

	/**
	 * Returns the label for the variability relations block
	 * @return
	 */
	public String getRelationsLab();
	
	/**
	 * Returns an operations block with the set of basic operations
	 */
	
	public String getBasicOperationsBlock();
	
}
