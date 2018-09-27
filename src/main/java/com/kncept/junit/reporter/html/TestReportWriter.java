package com.kncept.junit.reporter.html;

import static com.kncept.junit.reporter.json.JsonUtils.toArrayValue;
import static com.kncept.junit.reporter.json.JsonUtils.toJsMap;
import static com.kncept.junit.reporter.json.JsonUtils.toJsMapValue;
import static com.kncept.junit.reporter.json.JsonUtils.toJsNvpArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.kncept.junit.reporter.domain.CssRagStatus;
import com.kncept.junit.reporter.domain.TestCase;
import com.kncept.junit.reporter.xml.TestSuite;

public class TestReportWriter {
	private final String category;
	private List<TestSuite> testSuites;
	
	public TestReportWriter(String category) {
		this.category = category;
		testSuites = new ArrayList<>();
	}
	
	public String getCategory() {
		return category;
	}

	public void include(TestSuite suite) {
		testSuites.add(suite);
	}
	
	public void write(File outputDir, CssRagStatus ragStatusSettings) throws IOException {
		if (testSuites.isEmpty())
			return;
		
		//sort by test name
		Collections.sort(testSuites, (t1, t2) -> {return String.CASE_INSENSITIVE_ORDER.compare(t1.name(), t2.name());});
		
		File htmlDir = outputDir;
		if (category != null) {
			htmlDir = new File(outputDir, category);
			htmlDir.mkdirs();	
		}
		
		writeDynamicData(htmlDir, ragStatusSettings);
		writeStaticData(htmlDir);
		
	}
	
	public void writeDynamicData(File htmlDir, CssRagStatus ragStatusSettings) throws IOException {
		writeDataJs(htmlDir);
		writeRagCss(htmlDir, ragStatusSettings);
	}
	
	private void writeDataJs(File htmlDir) throws IOException {
		try (		
				PrintStream out = new PrintStream(new FileOutputStream(new File(htmlDir, "data.js")));
		) {
			SummaryBucket totals = new SummaryBucket("");
			Map<String, SummaryBucket> byPackage = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
			Map<String, SummaryBucket> byClass = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
			Map<String, SummaryBucket> byTestSuite = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
			
			
			out.println("const tests = [");
			boolean constTestsFirstOutput = true;
			for(int suiteId = 0; suiteId < testSuites.size(); suiteId++) {
				TestSuite suite = testSuites.get(suiteId);
				boolean suiteHasOutput = !suite.sysErr().isEmpty() || !suite.sysOut().isEmpty();
				for(TestCase test: suite.testcases()) {
					if (constTestsFirstOutput) constTestsFirstOutput = false;
					else out.println(",");
					
					List<String> attrs = new ArrayList<>();
					attrs.add(toJsMapValue("testClass", test.getClassname()));
					attrs.add(toJsMapValue("testName", test.getName()));
					attrs.add(toJsMapValue("duration", test.getDuration().toPlainString()));
					attrs.add(toJsMapValue("status", test.getStatus().name()));
					
					if (test.getUnsuccessfulMessage() != null)
						attrs.add(toJsMapValue("unsuccessfulMessage", test.getUnsuccessfulMessage()));
					if (test.getStackTrace() != null)
						attrs.add(toJsMapValue("stackTrace", test.getStackTrace()));
					
					attrs.add(toArrayValue("systemOut", test.getSystemOut()));
					attrs.add(toArrayValue("systemErr", test.getSystemErr()));
					attrs.add(toJsMapValue("suiteId", Integer.toString(suiteId)));
					attrs.add(toJsMapValue("suiteHasOutput", Boolean.toString(suiteHasOutput)));
					
					out.print(toJsMap(attrs.toArray(new String[attrs.size()])));
					
					totals.include(test);
					getSummaryBucket(byPackage, test.getPackagename()).include(test);
					getSummaryBucket(byClass, test.getClassname()).include(test);
					getSummaryBucket(byTestSuite, suite.name()).include(test);
				}
			}
			out.println();
			out.println("];");
			
			out.println("const totals = " + totals + ";");
			printSummaryBuckets(out, "packageTotals", byPackage.values());
			printSummaryBuckets(out, "classTotals", byClass.values());
			printSummaryBuckets(out, "testSuiteTotals", byTestSuite.values());
			
			
			out.println("const testSuites = [");
			for(int suiteId = 0; suiteId < testSuites.size(); suiteId++) {
				TestSuite suite = testSuites.get(suiteId);
				boolean suiteHasOutput = !suite.sysErr().isEmpty() || !suite.sysOut().isEmpty();
				
				SummaryBucket suiteTotals = new SummaryBucket(suite.name());
				for(TestCase test: suite.testcases()) suiteTotals.include(test);
				
				
				out.println("{");
				
				out.print(toJsMapValue("name", suite.name()));
				out.println(",");
				out.print(toJsMapValue("suiteHasOutput", Boolean.toString(suiteHasOutput)));
				out.println(",");
				out.println("totals: ");
				out.println(suiteTotals);
				out.println(",");
				
				out.println("testSuiteProps: ");
				out.println(toJsNvpArray(suite.testsuiteProperties()));
				out.println(",");
				
				out.println("sysprops: ");
				out.println(toJsNvpArray(suite.systemProperties()));
				out.println(",");
				
				out.println(toArrayValue("sysout", suite.sysOut()));
				out.println(",");
				out.println(toArrayValue("syserr", suite.sysErr()));
				
				if (suiteId + 1 < testSuites.size()) out.println("},");
				else out.println("}");
			}
			out.println("];");
			
			out.println("const buildTimeSystemProperties = ");
			out.println(toJsNvpArray(systemProperties()));
			out.println(";");
			
			out.println("const buildTimeEnvironmentProperties = ");
			out.println(toJsNvpArray(System.getenv()));
			out.println(";");
			
			out.flush();
		}
		
	}
	
	private Map<String, String> systemProperties() {
		Properties sysProps = System.getProperties();
		LinkedHashMap<String, String> systemProperties = new  LinkedHashMap<>();
		for(Object key: sysProps.keySet()) {
			systemProperties.put(key.toString(), sysProps.get(key).toString());
		}
		return systemProperties;
	}
	
	private void printSummaryBuckets(PrintStream out, String constName, Collection<SummaryBucket> values) {
		out.print("const " + constName + " = [");
		boolean firstElement = true;
		for(SummaryBucket summary: values) {
			if (firstElement) firstElement = false;
			else out.println(",");
			out.print(summary);
		}
		out.println("];");
	}
	
	private void writeRagCss(File htmlDir, CssRagStatus ragStatusSettings) throws IOException {
		try (		
				PrintStream out = new PrintStream(new FileOutputStream(new File(htmlDir, "rag.css")));
		) {
			out.println("/*RAG status indicators*/");
			outputCss(out, "r", ragStatusSettings.getRed());
			outputCss(out, "a", ragStatusSettings.getAmber());
			outputCss(out, "g", ragStatusSettings.getGreen());
		}
	}
	
	private void outputCss(PrintStream out, String cssClass, String colour) throws IOException {
		out.print(".");
		out.print(cssClass);
		out.println(" {");
		out.print("\tcolor: ");
		out.print(colour);
		out.println(";");
		out.println("\tfont-weight: bold;");
		out.println("}");
	}
	
	private SummaryBucket getSummaryBucket(Map<String, SummaryBucket> map, String bucketName) {
		SummaryBucket bucket = map.get(bucketName);
		if (bucket == null) {
			bucket = new SummaryBucket(bucketName);
			map.put(bucketName, bucket);
		}
		return bucket;
	}
	
	
	
	public void writeStaticData(File htmlDir) throws IOException {
		List<String> files = Arrays.asList(
				"index.html",
				"main.css",
				"site.js"
				);
		
		
		for(String fileName: files) {
			File outputFile = new File(htmlDir, fileName);
			if (outputFile.exists())
				outputFile.delete();
			try (
					OutputStream out = new FileOutputStream(outputFile);
					InputStream in = getTemplate(fileName);
			) {
				copy(in, out);
			} catch (IOException e) {
				throw new IOException(fileName, e);
			}
		}
		
	}
	
	public InputStream getTemplate(String templateName) {
		InputStream in = getClass().getClassLoader().getResourceAsStream("template/" + templateName);
		if (in == null)
			throw new NullPointerException("Unable to load resource template/" + templateName);
		return in;
	}
	
	
	public String resolveLine(BufferedReader in) throws IOException {
		return in.readLine();
	}
	
	
	public void copy(InputStream in, OutputStream out) throws IOException {
		byte[] b = new byte[1024];
		
		int read = in.read(b);
		while (read != -1) {
			out.write(b, 0, read);
			read = in.read(b);
		}
	}
	
}
