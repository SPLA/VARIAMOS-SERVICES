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
 */
public class XmlReader {
	
	/**
	 * @param ArrayList<Dependecy>: ArrayList with Dependecy objects
	 */
	private ArrayList<Dependecy> importantXmlDependecy;
	/**
	 * @param HashMap<String, Element>: HashMap with Dependecy objects
	 */

	private HashMap<String, Element> xmlElements;

	/**
	 * this method return a list of Dependecy objects
	 * 
	 * @return ArrayList: ArrayList with Dependecy objects
	 */
	public ArrayList<Dependecy> getImportantXmlDependecy() {
		return importantXmlDependecy;
	}

	/**
	 * this method change importantXMLDependecy's value for paramater.
	 * 
	 * @param importantXmlDependecy: ArrayList with Dependecy objects
	 */
	public void setImportantXmlDependecy(ArrayList<Dependecy> importantXmlDependecy) {
		this.importantXmlDependecy = importantXmlDependecy;
	}

	/**
	 * this method return a list of Element objects
	 * 
	 * @return HashMap: HashMap with Element objects
	 */

	public HashMap<String, Element> getXmlElements() {
		return xmlElements;
	}

	/**
	 * this method change importantXMLElement' value for paramater.
	 * 
	 * @param xmlElements: HashMap with Element objects
	 */

	public void setXmlElements(HashMap<String, Element> xmlElements) {
		this.xmlElements = xmlElements;
	}

	/**
	 * this method is responsible for inicializate importantXmlDependecy and
	 * importantXMLElement Arrays. and this methos have to create a DocumentBuilder
	 * to load the XML file. to then, read that fiel and load into Dependecy and
	 * Element objects.
	 * 
	 * @param path: string that represent the XML source to load.
	 */

	public void loadXmlFiel(String path) {
		importantXmlDependecy = new ArrayList<Dependecy>();
		xmlElements = new HashMap<String, Element>();
		List<File> xmlFiel = FileUtils.readFileFromDirectory(path);

		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			for (int i = 0; i < xmlFiel.size(); i++) {
				org.w3c.dom.Document xmlTree = builder.parse(xmlFiel.get(i));
				readDocument(xmlTree);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void loadXmlString(String xml) {
		importantXmlDependecy = new ArrayList<Dependecy>();
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
	 * this method is responsible for create and add to importarXmlElement a Element
	 * object with type equal to General or Root. to create this objet is necesary a
	 * name, id and type.
	 * 
	 * @param n: node from XML tree
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
	 * this method is responsible for create and add to importarXmlElement a Element
	 * object with type equal to Leaf. to create this objet is necesary a name, id,
	 * type and select.
	 * 
	 * @param n: node from XML tree
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
	 * this method is responsible for create and add to importarXmlElement a Element
	 * object with type equal to bundle. to create this objet is necesary a name,
	 * id, type and bundleType.
	 * 
	 * @param n: node from XML tree
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
	 * this method is responsible for check if importarXmlDependecy contain a
	 * Dependecy wiht the same id
	 * 
	 * @param String: Dependecy's id
	 */
	private boolean exitsDependecy(String id) {
		for (int i = 0; i < importantXmlDependecy.size(); i++) {
			if (id.equals(importantXmlDependecy.get(i).id)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * this method is responsible for create and add to importarXmlDependecy a
	 * Dependecy object with type
	 * 
	 * @param n: node from XML tree
	 */
	public void addDependecy(Node n) {
		Dependecy newDependecy = new Dependecy();

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
			importantXmlDependecy.add(newDependecy);

	}

	/**
	 * this method is responsible for travel whole XML tree and call methos to
	 * create Element or Dependecy object.
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
	 * this method is responsible for travel the node's children and add source and
	 * target parameter to Dependecy object.
	 * 
	 * @param n: node from XML tree
	 * @param newDependecy: Dependecy that need source and target parameter
	 */
	public void AddAtributesFromChildren(Node n, Dependecy newDependecy) {
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
