package com.kncept.junit.reporter.json;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class JsonUtils {
	private JsonUtils() {}
	
	public static String toJsMap(String... mapValues) {
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
	
	static String addDelimiters(String value) {
		value = value.replaceAll("\\\\", "\\\\\\\\"); //delimiter... always *start* with this
		value = value.replaceAll("\n", "\\\\n"); //newline character
		value = value.replaceAll("\r", "\\\\r"); //carriage return character
		value = value.replaceAll("\t", "\\\\t"); //tab character
		value = value.replaceAll("\b", "\\\\b"); //backspace character
		value = value.replaceAll("\f", "\\\\f"); //form feed character
		value = value.replaceAll("\"", "\\\\\"");
		return value;
	}

	public static String toJsMapValue(String key, String value) {
		return key + ": \"" + addDelimiters(value) + "\"";
	}

	public static String toJsMapValue(String key, boolean value) {
		return key + ": " + Boolean.toString(value) ;
	}

	public static String toJsMapValue(String key, int value) {
		return key + ": " + value;
	}
	
	public static String toJsMapEmbeddedValue(String key, String value) {
		return key + ": " + value;
	}
	
	public static String toJsMapArrayValue(String key, List<String> values) {
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
	public static String toJsNvpArray(Map<String, String> stringMap) {
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

}
