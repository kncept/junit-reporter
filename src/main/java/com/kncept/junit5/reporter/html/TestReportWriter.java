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

import com.kncept.junit5.reporter.domain.CssRagStatus;
import com.kncept.junit5.reporter.domain.TestCase;
import com.kncept.junit5.reporter.domain.TestCaseStatus;
import com.kncept.junit5.reporter.xml.XMLTestResults;

public class TestReportWriter {
	private final String category;
	
	//Going to assume that the system properties DO NOT CHANEG between tests.
	//TODO: Work out how to make this more flexible
	public LinkedHashMap<String, String> systemProperties = new LinkedHashMap<>();
	public LinkedHashMap<String, String> testsuiteProperties = new LinkedHashMap<>();
	public List<TestCase> testcases = new ArrayList<>();
	
	public TestReportWriter(String category) {
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
	
	public void write(File outputDir, CssRagStatus ragStatusSettings) throws IOException {
		if (testcases.isEmpty())
			return;
		
		//sort by test name
		Collections.sort(testcases, (t1, t2) -> {return String.CASE_INSENSITIVE_ORDER.compare(t1.getName(), t2.getName());});
		
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
				List<String> attrs = new ArrayList<>();
				attrs.add(toJsMapValue("testClass", next.getClassname()));
				attrs.add(toJsMapValue("testName", next.getName()));
				attrs.add(toJsMapValue("duration", next.getDuration().toPlainString()));
				attrs.add(toJsMapValue("status", next.getStatus().name()));
				
				if (next.getUnsuccessfulMessage() != null)
					attrs.add(toJsMapValue("unsuccessfulMessage", next.getUnsuccessfulMessage()));
				if (next.getStackTrace() != null)
					attrs.add(toJsMapValue("stackTrace", next.getStackTrace()));
				
				attrs.add(toArrayValue("systemOut", next.getSystemOut()));
				attrs.add(toArrayValue("systemErr", next.getSystemErr()));
				
				out.print(toJsMap(attrs.toArray(new String[attrs.size()])));
				
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
	public String addDelimiters(String value) {
		value = value.replaceAll("\\\\", "\\\\\\\\"); //delimiter... always *start* with this
		value = value.replaceAll("\n", "\\\\n"); //newline character
		value = value.replaceAll("\r", "\\\\r"); //carriage return character
		value = value.replaceAll("\t", "\\\\t"); //tab character
		value = value.replaceAll("\b", "\\\\b"); //backspace character
		value = value.replaceAll("\f", "\\\\f"); //form feed character
		value = value.replaceAll("\"", "\\\\\"");
		return value;
	}
	private String toJsMapValue(String key, String value) {
		return key + ": \"" + addDelimiters(value) + "\"";
	}
	private String toArrayValue(String key, List<String> values) {
		StringBuilder sb = new StringBuilder("[ ");
		for(int i = 0; i < values.size(); i++) {
			if (i != 0)
				sb.append(", ");
			sb.append("\"" + addDelimiters(values.get(i)) + "\"");
		}
		sb.append("]");
		return key + ": " + sb.toString();
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
			}
		}
		sb.append(" ]");
		return sb.toString();
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
