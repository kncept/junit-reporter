package com.kncept.junit5.reporter.html;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.kncept.junit5.reporter.domain.TestCase;
import com.kncept.junit5.reporter.domain.TestCaseStatus;
import com.kncept.junit5.reporter.xml.XMLTestResults;

public class TestHTMLReportWriter {
	private final String category;
	
	//Going to assume that the system properties DO NOT CHANEG between tests.
	//TODO: Work out how to make this more flexible
	public LinkedHashMap<String, String> systemProperties = new LinkedHashMap<>();
	public LinkedHashMap<String, String> testsuiteProperties = new LinkedHashMap<>();
	public List<TestCase> testcases = new ArrayList<>();
	
	public TestHTMLReportWriter(String category) {
		this.category = category;
	}
	
	public String getCategory() {
		return category;
	}

	//optional settings... colours, etc.
	
	public void include(XMLTestResults xml) {
		systemProperties.putAll(xml.systemProperties());
		testsuiteProperties.putAll(xml.testsuiteProperties());
		testcases.addAll(xml.testcases());
	}
	
	public void include(TestCase testcase) {
		testcases.add(testcase);
	}
	
	public void write(File outputDir) throws IOException {
		if (testcases.isEmpty())
			return;
		
		//sort by test name
		Collections.sort(testcases, (t1, t2) -> {return String.CASE_INSENSITIVE_ORDER.compare(t1.getName(), t2.getName());});
		
		File htmlDir = new File(outputDir, category);
		htmlDir.mkdirs();
		
		writeDynamicData(htmlDir);
		writeStaticData(htmlDir);
		
	}
	
	public void writeDynamicData(File htmlDir) throws IOException {
		try (
				PrintStream out = new PrintStream(new FileOutputStream(new File(htmlDir, "data.js")));
		) {

			if (!testsuiteProperties.containsKey("timestamp")) {
				testsuiteProperties.put("timestamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date()));
			}
			if (!testsuiteProperties.containsKey("time")) {
				testsuiteProperties.put("time", "unknown");
			}
			
			
			
			out.println("var testSuiteProps = ");
			out.println(toJsNvpArray(testsuiteProperties));
			out.println(";");
			
			
			out.println("var sysprops = ");
			out.println(toJsNvpArray(systemProperties));
			out.println(";");

			SummaryBucket totals = new SummaryBucket("");
			Map<String, SummaryBucket> byPackage = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
			Map<String, SummaryBucket> byClass = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
			
			
			Iterator<TestCase> tests = testcases.iterator();
			out.println("var tests = [");
			while(tests.hasNext()) {
				TestCase next = tests.next();
				out.print(toJsMap(
						toJsMapValue("testClass", next.getClassname()),
						toJsMapValue("testName", next.getName()),
						toJsMapValue("duration", next.getDuration().toPlainString()),
						toJsMapValue("status", next.getStatus().name())
				));
				
				totals.include(next.getStatus());
				getSummaryBucket(byPackage, next.getPackagename()).include(next.getStatus());
				getSummaryBucket(byClass, next.getClassname()).include(next.getStatus());
				
				if(tests.hasNext()) {
					out.println(",");
				} else {
					out.println();
				}
			}
			out.println("];");
			
			out.print("var summary = ");
			out.print(toJsMap(
					toJsMapValue("passed", Integer.toString(totals.passed)),
					toJsMapValue("skipped", Integer.toString(totals.skipped)),
					toJsMapValue("failed", Integer.toString(totals.failed)),
					toJsMapValue("errored", Integer.toString(totals.errored)),
					toJsMapValue("available", Integer.toString(totals.passed + totals.skipped + totals.failed + totals.errored)),
					toJsMapValue("executed", Integer.toString(totals.passed + totals.failed + totals.errored)),
					toJsMapValue("timestamp", testsuiteProperties.get("timestamp")),
					toJsMapValue("duration", testsuiteProperties.get("time"))
					));
			out.println(";");
			
			Iterator<SummaryBucket> packageSummary = byPackage.values().iterator();
			out.println("var packageSummary = [");
			while(packageSummary.hasNext()) {
				SummaryBucket next = packageSummary.next();
				out.print(toJsMap(
						toJsMapValue("key", next.key),
						toJsMapValue("passed", Integer.toString(next.passed)),
						toJsMapValue("skipped", Integer.toString(next.skipped)),
						toJsMapValue("failed", Integer.toString(next.failed)),
						toJsMapValue("errored", Integer.toString(next.errored)),
						toJsMapValue("available", Integer.toString(next.passed + next.skipped + next.failed + next.errored)),
						toJsMapValue("executed", Integer.toString(next.passed + next.failed + next.errored))
						));
				if(packageSummary.hasNext()) {
					out.println(",");
				} else {
					out.println();
				}
			}
			out.println("];");
			
			Iterator<SummaryBucket> classSummary = byClass.values().iterator();
			out.println("var classSummary = [");
			while(classSummary.hasNext()) {
				SummaryBucket next = classSummary.next();
				out.print(toJsMap(
						toJsMapValue("key", next.key),
						toJsMapValue("passed", Integer.toString(next.passed)),
						toJsMapValue("skipped", Integer.toString(next.skipped)),
						toJsMapValue("failed", Integer.toString(next.failed)),
						toJsMapValue("errored", Integer.toString(next.errored)),
						toJsMapValue("available", Integer.toString(next.passed + next.skipped + next.failed + next.errored)),
						toJsMapValue("executed", Integer.toString(next.passed + next.failed + next.errored))
						));
				if(classSummary.hasNext()) {
					out.println(",");
				} else {
					out.println();
				}
			}
			out.println("];");
			
			out.flush();
		}
		
	}
	
	static class SummaryBucket {
		public final String key;
		public int passed;
		public int skipped;
		public int failed;
		public int errored;
		public SummaryBucket(String key) {
			this.key = key;
		}
		public void include(TestCaseStatus status) {
			switch(status) {
			case Passed:
				passed++;
				break;
			case Skipped:
				skipped++;
				break;
			case Failed:
				failed++;
				break;
			case Errored:
				errored++;
				break;
			}
		}
	}
	
	private SummaryBucket getSummaryBucket(Map<String, SummaryBucket> map, String bucketName) {
		SummaryBucket bucket = map.get(bucketName);
		if (bucket == null) {
			bucket = new SummaryBucket(bucketName);
			map.put(bucketName, bucket);
		}
		return bucket;
	}
	
	private String toJsMap(String... mapValues) {
		StringBuilder sb = new StringBuilder();
		sb.append("{ ");
		for(int i = 0; i < mapValues.length; i++) {
			sb.append(mapValues[i]);
			if (i + 1 < mapValues.length)
				sb.append(", ");
		}
		sb.append("}");
		return sb.toString();
	}
	private String toJsMapValue(String key, String value) {
		value = value.replaceAll("\n", "\\n");
		value = value.replaceAll("\"", "\\\"");
		return "" + key + ": \"" + value + "\"";
	}
	//because of how JsonTable works, turn the map into a NVP array
	private String toJsNvpArray(Map<String, String> stringMap) {
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		
		Iterator<Map.Entry<String, String>> values = stringMap.entrySet().iterator();
		while(values.hasNext()) {
			Map.Entry<String, String> next = values.next();
			sb.append(toJsMap(
					toJsMapValue("name", next.getKey()),
					toJsMapValue("value", next.getValue())
			));
			if(values.hasNext()) {
				sb.append(",\n");
			} else {
				sb.append(" ]");
			}
		}
		return sb.toString();
	}
	
	public void writeStaticData(File htmlDir) throws IOException {
		List<String> files = Arrays.asList(
				"index.html",
				"main.css",
				"main.jsx",
				
				"react.15.4.2.js",
				"react-dom.15.4.2.js",
				
				"babel-5.8.34.js",
				
				"react-json-table.min.js"
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
			}
		}
		
	}
	
	public InputStream getTemplate(String templateName) {
		return getClass().getClassLoader().getResourceAsStream("template/" + templateName);
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
