package com.kncept.junit5.reporter.html;

import static com.kncept.junit5.reporter.json.JsonUtils.toJsMap;
import static com.kncept.junit5.reporter.json.JsonUtils.toJsMapValue;

import java.math.BigDecimal;

import com.kncept.junit5.reporter.domain.TestCase;

public class SummaryBucket {
	private final String key;
	private int passed;
	private int skipped;
	private int failed;
	private int errored;
	private BigDecimal duration = BigDecimal.ZERO;
	public SummaryBucket(String key) {
		this.key = key;
	}
	public void include(TestCase test) {
		switch(test.getStatus()) {
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
		duration = duration.add(test.getDuration());
	}
	
	public String toString() {
		return toJsMap(
				toJsMapValue("key", key),
				toJsMapValue("passed", Integer.toString(passed)),
				toJsMapValue("skipped", Integer.toString(skipped)),
				toJsMapValue("failed", Integer.toString(failed)),
				toJsMapValue("errored", Integer.toString(errored)),
				toJsMapValue("available", Integer.toString(passed + skipped + failed + errored)),
				toJsMapValue("executed", Integer.toString(passed + failed + errored)),
				toJsMapValue("duration", duration.toString())
				);
	}
}