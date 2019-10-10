package coffee.modelParsers.utils;

/**
 * Parameters for the splot to HLVL parameters
 * @author Angela Villota
 * Modified by Juan Reyes - 2019-02-15
 * January 2019
 */

public class ParsingParameters {
	private static final String HLVL_EXT=".hlvl";
	private String inputPath;
	private String outputPath;
	private String targetName;
	

	/*
	 *  Getters and Setters
	 */
	/**
	 * @return the outputPath
	 */
	public String getOutputPath() {
		return outputPath+"/"+targetName+ HLVL_EXT; // Symbol "/" was added between outputPath and targetName
	}
	/**
	 * @param outputPath the outputPath to set
	 */
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}
	/**
	 * @return the inputPath
	 */
	public String getInputPath() {
		return inputPath;
	}
	/**
	 * @param inputPath the inputPath to set
	 */
	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}
	
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

}
