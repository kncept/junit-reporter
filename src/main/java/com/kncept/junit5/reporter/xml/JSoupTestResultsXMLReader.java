package com.kncept.junit5.reporter.xml;

import static java.lang.System.lineSeparator;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.kncept.junit5.reporter.domain.TestCase;

public class JSoupTestResultsXMLReader implements XMLTestResults {
	final Document doc;
	
	public JSoupTestResultsXMLReader(File file) throws IOException {
		doc = Jsoup.parse(file, null);
	}
	
	public JSoupTestResultsXMLReader(InputStream in) throws IOException {
		doc = Jsoup.parse(in, null, "");
		in.close();
	}
	
	@Override
	public LinkedHashMap<String, String> properties() {
		LinkedHashMap<String, String> properties = new LinkedHashMap<>();
		Elements xmlProperties = doc.select("properties > property");
		xmlProperties.forEach(e -> {
			properties.put(e.attr("name"), e.attr("value"));
		});
		return properties;
	}

	@Override
	public List<TestCase> testcases() {
		List<TestCase> testcases = new ArrayList<>();
		Elements xmlTestcases = doc.select("testcase");
		xmlTestcases.forEach(e -> {
			TestCase testCase = new TestCase(e.attr("name"), e.attr("classname"), toBigDecimal(e.attr("time")));
			
			testCase.getSystemOut().addAll(output(e.select("system-out")));
			testCase.getSystemErr().addAll(output(e.select("system-err")));	
			
			testcases.add(testCase);
		});
		return testcases;
	}
	
	private BigDecimal toBigDecimal(String val) {
		if (val.equals(""))
			return BigDecimal.ZERO;
		return new BigDecimal(val);
	}

	private List<String> output(Elements outputElement) {
		if (outputElement.isEmpty())
			return emptyList();
		Element e = outputElement.get(0);
		List<String> output = new ArrayList<>();
		for(TextNode text: e.textNodes()) {
			output.addAll(asList(text.getWholeText().split(lineSeparator())));
		}
		return output;
	}
	
}

