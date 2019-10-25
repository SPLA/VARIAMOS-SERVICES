package coffee.modelParsers.basicHLVLPackage;

/**
 * Enumeration declaring the types of Groups
 * in the basic dialect of HLVL (Hlvl(basic))
 * Xor: To define that from a group of elements just one can be present on the product
 * Or: To define that from a group of elements one or more can be present on the product
 * And: To define that from a group of elements all of them must be present on the product
 * @author Angela Villota
 * Coffee V1
 * January 2019
 * contracts modified on September 15th by Juan Diego Carvajal Castaño
 */
public enum GroupType {
	Xor,
	Or,
	And,
	Range
}
