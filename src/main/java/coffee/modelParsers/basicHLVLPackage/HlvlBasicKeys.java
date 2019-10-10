package coffee.modelParsers.basicHLVLPackage;

/**
 * Interface declaring  a set of constants used to represent HLVL constructs, and keywords 
 * in the basic dialect of HLVL (Hlvl(basic))
 * @author Angela Villota
 * Coffee V1
 * January 2019
 */

public interface HlvlBasicKeys {
	/**
	 * Hlvl Constructs for the dialect HLVL(basic)
	 */
	public static String CORE ="coreElements";
	public static String MUTEX ="mutex";
	public static String IMPLIES ="implies";
	public static String DECOMPOSITION ="decomposition";
	public static String GROUP ="group";
	public static String EXPRESSION ="expression";
	
	/**
	 * Elements declaration, all are boolean
	 */
	 public static String ELM_DECLARATION ="boolean";
	
	/**
	 * Cardinalities for decomposition and group constructs 
	 */
	 public static String MANDATORY ="<1>";
	 public static String OPTIONAL ="<0>";
	 public static String ALTERNATIVE ="[1,1]";
	 public static String OR ="[1,*]";
	 
	 /**
	  * Syntax elements
	  */
	  public static String OPEN_CALL ="(";
	  public static String CLOSE_CALL =")";
	  public static String OPEN_LIST ="[";
	  public static String CLOSE_LIST ="]";
	  public static String COMMA =",";
	  public static String COLON =":";
	  public static String SPACE =" ";
	  
	  /**
	   * Model labels
	   */
	   public static String MODEL_LABEL ="model " ;
	   public static String ELEMENTS_LABEL ="elements: \n"; 
	  
	   public static String RELATIONS_LABEL ="relations:\n";
	   public static String OPERATIONS_LABEL = "operations: \n";
	   
	   /**
	    * Basic operations
	    */
	    public static String VALID_MODEL ="validModel";
	    public static String NUM_CONF ="numberOfConfigurations";
	    public static String FIND_ONE ="findConfiguration";
	    public static String FIND_ALL ="findAllConfigurations";
	    public static String VALID_CONF ="validConfiguration";
	    
	    /**
	     * Expressions 
	     */
	     public static String NEG ="~";
	     public static String L_AND ="AND";
	     public static String L_OR ="OR";
	     
	
	
}