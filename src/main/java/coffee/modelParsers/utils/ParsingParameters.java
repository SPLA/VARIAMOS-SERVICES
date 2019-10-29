package coffee.modelParsers.utils;

/**
 * Parameters for the varXml to HLVL parameters.
 * @author Angela Villota
 * Modified by Juan Reyes - 2019-02-15
 * January 2019
 * contracts modified on September 15th by Juan Diego Carvajal Castaño
 */

public class ParsingParameters {
	private static final String HLVL_EXT=".hlvl";
	/**
	 * Path to the xml file that contains the model to parse
	 */
	private String inputPath;
	/**
	 * Path where the parsed xml (Hlvl code) file should be created
	 */
	private String outputPath;
	/**
	 * File name for the parsed xml (Hlvl code)
	 */
	private String targetName;

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
	/**
	 * @return the targetName
	 */
	public String getTargetName() {
		return targetName;
	}
	/**
	 * @param targetName the targetName to set
	 */
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

}
