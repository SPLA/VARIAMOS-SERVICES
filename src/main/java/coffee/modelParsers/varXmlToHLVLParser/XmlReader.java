package coffee.modelParsers.varXmlToHLVLParser;


import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import coffee.modelParsers.utils.FileUtils;


/**
 * This is a class which is responsible for reading a XML file, loading that
 * file into tree structure. and extract information from XML tree to create XML
 * element object or XML dependency objects too.
 * 
 * @version 0.5, 19/01/2019
 * @author Joan David Colina Echeverry
 * contracts modified on September 15th by Juan Diego Carvajal Castaño
 */
public class XmlReader {
	
	/**
	 * @param ArrayList<Dependecy>: ArrayList with Dependency objects
	 */
	private ArrayList<Dependency> xmlDependecies;
	/**
	 * @param HashMap<String, Element>: HashMap with Element objects
	 */

	private HashMap<String, Element> xmlElements;

	/**
	 * This method return a list of Dependency objects
	 * 
	 * @return ArrayList: ArrayList with Dependency objects
	 */
	public ArrayList<Dependency> getImportantXmlDependecy() {
		return xmlDependecies;
	}

	/**
	 * This method change importantXMLDependecies's value for parameter.
	 * 
	 * @param importantXmlDependecy: ArrayList with Dependency objects
	 */
	public void setImportantXmlDependecy(ArrayList<Dependency> importantXmlDependecy) {
		this.xmlDependecies = importantXmlDependecy;
	}

	/**
	 * This method return a list of Element objects
	 * 
	 * @return HashMap: HashMap with Element objects
	 */

	public HashMap<String, Element> getXmlElements() {
		return xmlElements;
	}

	/**
	 * This method changes importantXMLElement' value for parameter.
	 * 
	 * @param xmlElements: HashMap with Element objects
	 */

	public void setXmlElements(HashMap<String, Element> xmlElements) {
		this.xmlElements = xmlElements;
	}

	/**
	 * This method is responsible for initialize xmlDependecy and
	 * xmlElement Arrays, creating a DocumentBuilder
	 * to load a XML file, read that file and load it into Dependency and
	 * Element objects.
	 * 
	 * @param path: location to the XML file to load.
	 */

	public void loadXmlFile(String path) {
		xmlDependecies = new ArrayList<Dependency>();
		xmlElements = new HashMap<String, Element>();
		List<File> xmlFile = FileUtils.readFileFromDirectory(path);

		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			for (int i = 0; i < xmlFile.size(); i++) {
				org.w3c.dom.Document xmlTree = builder.parse(xmlFile.get(i));
				readDocument(xmlTree);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method is responsible for initialize xmlDependecies and
	 * xmlElements Arrays, creating a DocumentBuilder to
	 * read a String that represents the content of a xml file,
	 * and load it into Dependency and Element objects.
	 * 
	 * @param xml:String that represents the content of a xml file
	 */
	public void loadXmlString(String xml) {
		xmlDependecies = new ArrayList<Dependency>();
		xmlElements = new HashMap<String, Element>();
	
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			org.w3c.dom.Document xmlTree = builder.parse(new InputSource(new StringReader(xml)));
			readDocument(xmlTree);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * This method is responsible for creating and adding to xmlElements an Element
	 * object with type equal to General or Root. To create this object is necessary a
	 * name, id and type.
	 * 
	 * @param n: node from XML tree from the loadXmlFile or loadXmlString method
	 */
	public void addGeneralAndRootElement(Node n) {

		Element xmlElement = new Element();

		String name = n.getAttributes().item(1).getNodeValue();
		xmlElement.setName(name);

		String id = n.getAttributes().item(0).getNodeValue();
		xmlElement.setId(id);

		String type = n.getAttributes().item(2).getNodeValue();
		xmlElement.setType(type);
		if (!id.contains("clon")) {

			xmlElements.put(id, xmlElement);
		}

	}

	/**
	 * This method is responsible for creating and adding to xmlElement an Element
	 * object with type equal to Leaf. To create this object is necessary a name, id,
	 * type and select.
	 * 
	 * @param n: node from XML tree from the loadXmlFile or loadXmlString method
	 */
	public void addLeafElement(Node n) {
		Element xmlElement = new Element();

		String name = n.getAttributes().item(1).getNodeValue();
		xmlElement.setName(name);

		String id = n.getAttributes().item(0).getNodeValue();
		xmlElement.setId(id);

		String type = n.getAttributes().item(2).getNodeValue();
		xmlElement.setType(type);

		String select = n.getAttributes().item(3).getNodeValue();
		xmlElement.setSelected(select);
		if (!id.contains("clon")) {

			xmlElements.put(id, xmlElement);
		}

	}

	/**
	 * This method is responsible for creating and adding to xmlElement a Element
	 * object with type equal to bundle. to create this object is necessary a name,
	 * id, type and bundleType.
	 * 
	 * @param n: node from XML tree from the loadXmlFile or loadXmlString method
	 */
	public void addBundleElement(Node n) {
		Element xmlElement = new Element();

		String name = n.getAttributes().item(5).getNodeValue();

		xmlElement.setName(name);

		String id = n.getAttributes().item(2).getNodeValue();
		xmlElement.setId(id);

		String type = n.getAttributes().item(3).getNodeValue();
		xmlElement.setType(type);

		String bundleType = n.getAttributes().item(0).getNodeValue();

		xmlElement.setBundleType(bundleType);

		if (!id.contains("clon")) {

			xmlElements.put(id, xmlElement);
		}

	}

	/**
	 * This method is responsible for checking if xmlDependencies contains a
	 * Dependency with the same id given
	 * 
	 * @param String: Dependency's id
	 */
	private boolean exitsDependecy(String id) {
		for (int i = 0; i < xmlDependecies.size(); i++) {
			if (id.equals(xmlDependecies.get(i).id)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method is responsible for creating and adding to xmlDependencies a
	 * Dependency object with type
	 * 
	 * @param n: node from XML tree from the loadXmlFile or loadXmlString method
	 */
	public void addDependecy(Node n) {
		Dependency newDependecy = new Dependency();

		newDependecy.setId(n.getAttributes().item(0).getNodeValue());

		newDependecy.setRelType(n.getAttributes().item(1).getNodeValue());

		newDependecy.setType("relation");
		if (n.getAttributes().item(2) != null) {
			newDependecy.setType(n.getAttributes().item(2).getNodeValue());
		} else {
			newDependecy.setRelType("bundle");
		}
		AddAtributesFromChildren(n, newDependecy);

		if (!exitsDependecy(n.getAttributes().item(0).getNodeValue()))
			xmlDependecies.add(newDependecy);

	}

	/**
	 * This method is responsible for traveling a whole XML tree and call methods to
	 * create and add the correct type of Element or Dependency object to the
	 * xmlDependencies or xmlElements attributes.
	 * 
	 * @param n: node from XML tree
	 */
	public void readDocument(Node n) {

		if ((n.getNodeName().equals("abstract") || n.getNodeName().equals("root"))
				&& n.getAttributes().item(0) != null) {
			addGeneralAndRootElement(n);
		} else if (n.getNodeName().equals("concrete")) {
			addLeafElement(n);
		} else if (n.getNodeName().equals("bundle")) {
			addBundleElement(n);
		} else if (n.getNodeName().indexOf("rel_") > (-1)) {
			addDependecy(n);
		}
		NodeList childrens = n.getChildNodes();
		for (int i = 0; i < childrens.getLength(); i++) {
			Node grandchildren = childrens.item(i);
			readDocument(grandchildren);
		}

	}

	/**
	 * This method is responsible for traveling the node's children and add source and
	 * target parameters to Dependencies objects.
	 * 
	 * @param n: node from XML tree
	 * @param newDependecy: Dependency that need source and target parameters
	 */
	public void AddAtributesFromChildren(Node n, Dependency newDependecy) {
		NodeList childrens = n.getChildNodes();
		for (int i = 0; i < childrens.getLength(); i++) {
			Node newNode = childrens.item(i);
			if (newNode.getNodeName().equals("mxCell") && newNode.getAttributes() != null) {

				newDependecy.setSource(newNode.getAttributes().item(2).getNodeValue());
				newDependecy.setTarget(newNode.getAttributes().item(3).getNodeValue());
			}
		}
	}
}
