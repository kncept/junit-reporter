package com.kncept.junit5.reporter.xml;

import static java.lang.System.lineSeparator;
import static java.util.Arrays.asList;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.kncept.junit5.reporter.domain.TestCase;

public class Junit4DomReader implements XMLTestResults{
	
	private LinkedHashMap<String, String> properties = new LinkedHashMap<>();
	private List<TestCase> testcases = new ArrayList<>();
	
	public Junit4DomReader(InputStream in) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(in);
		
		NodeList nl = doc.getElementsByTagName("property");
		for(int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			properties.put(attr(node, "name"), attr(node, "value"));
		}
		
		nl = doc.getElementsByTagName("testcase");
		for(int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			TestCase testCase = new TestCase(attr(node, "name"), attr(node, "classname"), new BigDecimal(attr(node, "time")));
			testcases.add(testCase);
			
			testCase.getSystemOut().addAll(handleTextNode(child(node, "system-out")));
			testCase.getSystemErr().addAll(handleTextNode(child(node, "system-err")));
			
		}
		
	}
	
	private String attr(Node node, String name) {
		return node.getAttributes().getNamedItem(name).getTextContent();
	}
	
	private Node child(Node parent, String name) {
		NodeList nl = parent.getChildNodes();
		for(int i = 0; i < nl.getLength(); i++) {
			Node child = nl.item(i);
			if (name.equals(child.getNodeName()))
				return child;
		}
		return null;
	}
	
	private List<String> handleTextNode(Node node) {
		List<String> lines = new ArrayList<>();
		if (node != null) {
			NodeList nl = node.getChildNodes();
			for(int i = 0; i < nl.getLength(); i++) {
				Node child = nl.item(i);
				lines.addAll(asList(child.getTextContent().split(lineSeparator())));
			}
		}
		return lines;
	}
	
	public LinkedHashMap<String, String> properties() {
		return properties;
	}
	
	public List<TestCase> testcases() {
		return testcases;
	}
	
}
