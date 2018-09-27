package com.kncept.junit.reporter.html;

import static com.kncept.junit.reporter.json.JsonUtils.toJsMap;
import static com.kncept.junit.reporter.json.JsonUtils.toJsMapArrayValue;
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

import com.kncept.junit.reporter.SummaryBucket;
import com.kncept.junit.reporter.TestRunResults;
import com.kncept.junit.reporter.domain.CssRagStatus;
import com.kncept.junit.reporter.domain.TestCase;
import com.kncept.junit.reporter.xml.TestSuite;

public class TestReportWriter {
	List<TestRunResults> runResults;
	
	public TestReportWriter(List<TestRunResults> runResults) {
		this.runResults = new ArrayList<>(runResults);
	}
	
	public void write(File outputDir, CssRagStatus ragStatusSettings) throws IOException {
		Collections.sort(runResults, (t1, t2) -> {return String.CASE_INSENSITIVE_ORDER.compare(t1.category(), t2.category());});
		
		writeDynamicData(outputDir, ragStatusSettings);
		writeStaticData(outputDir);
		
	}
	
	public void writeDynamicData(File htmlDir, CssRagStatus ragStatusSettings) throws IOException {
		writeDataJs(htmlDir);
		writeRagCss(htmlDir, ragStatusSettings);
	}
	
	public SummaryBucket summary() {
		SummaryBucket totals = new SummaryBucket("");
		runResults.forEach(results -> totals.include(results.totals()));
		return totals;
	}
	
	
	private void writeDataJs(File htmlDir) throws IOException {
		try (		
				PrintStream out = new PrintStream(new FileOutputStream(new File(htmlDir, "data.js")));
		) {
			Map<String, SummaryBucket> byPackage = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
			Map<String, SummaryBucket> byClass = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
			Map<String, SummaryBucket> byTestSuite = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
			
			int index = 0;
			out.println("const tests = [");
			for(TestRunResults results: runResults) {
				for(TestSuite suite: results.results()) {
					boolean suiteHasOutput = !suite.sysErr().isEmpty() || !suite.sysOut().isEmpty();
					for(TestCase test: suite.testcases()) {
						if (index != 0) out.println(",");
						out.print(toJs(index, suiteHasOutput, suite.name(), results.category(), test));
						
						getSummaryBucket(byPackage, test.getPackagename()).include(test);
						getSummaryBucket(byClass, test.getClassname()).include(test);
						getSummaryBucket(byTestSuite, suite.name()).include(test);
						
						index++;
					}
				}
			}
			out.println();
			out.println("];");
			
			out.println("const totals = " + summary() + ";");
			printSummaryBuckets(out, "packageTotals", byPackage.values());
			printSummaryBuckets(out, "classTotals", byClass.values());
			printSummaryBuckets(out, "testSuiteTotals", byTestSuite.values());
			
			
			index = 0;
			out.println("const testSuites = [");
			for(TestRunResults results: runResults) {
				for(TestSuite suite: results.results()) {
					if (index != 0) out.println(",");
					out.print(toJs(results.category(), suite));
					index++;
				}
			}
			out.println();
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
	
	private String toJs(int index, boolean suiteHasOutput, String suiteName, String category, TestCase test) {
		List<String> attrs = new ArrayList<>();
		attrs.add(toJsMapValue("testClass", test.getClassname()));
		attrs.add(toJsMapValue("testName", test.getName()));
		attrs.add(toJsMapValue("duration", test.getDuration().toPlainString()));
		attrs.add(toJsMapValue("status", test.getStatus().name()));
		
		if (test.getUnsuccessfulMessage() != null)
			attrs.add(toJsMapValue("unsuccessfulMessage", test.getUnsuccessfulMessage()));
		if (test.getStackTrace() != null)
			attrs.add(toJsMapValue("stackTrace", test.getStackTrace()));
		
		attrs.add(toJsMapArrayValue("systemOut", test.getSystemOut()));
		attrs.add(toJsMapArrayValue("systemErr", test.getSystemErr()));
		attrs.add(toJsMapValue("testId", Integer.toString(index)));
		
		attrs.add(toJsMapValue("suiteHasOutput", Boolean.toString(suiteHasOutput)));
		attrs.add(toJsMapValue("suiteName", suiteName));
		
		attrs.add(toJsMapValue("category", category));
		
		
		return toJsMap(attrs.toArray(new String[attrs.size()]));
	}
	
	private String toJs(String category, TestSuite suite) {
		boolean suiteHasOutput = !suite.sysErr().isEmpty() || !suite.sysOut().isEmpty();
		
		SummaryBucket suiteTotals = new SummaryBucket(suite.name());
		for(TestCase test: suite.testcases()) suiteTotals.include(test);

		return toJsMap(
				toJsMapValue("suiteName", suite.name()),
				toJsMapValue("suiteHasOutput", Boolean.toString(suiteHasOutput)),
				toJsMapArrayValue("sysout", suite.sysOut()),
				toJsMapArrayValue("syserr", suite.sysErr()),
				toJsMapValue("totals", suiteTotals.toString()),
				
				toJsMapValue("testSuiteProps", toJsNvpArray(suite.testsuiteProperties())),
				toJsMapValue("sysprops", toJsNvpArray(suite.systemProperties())),
				
				toJsMapValue("category", category)
		);
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
