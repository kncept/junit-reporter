package com.kncept.junit.reporter;

import static com.kncept.junit.reporter.json.JsonUtils.toJsMap;
import static com.kncept.junit.reporter.json.JsonUtils.toJsMapValue;

import java.math.BigDecimal;

import com.kncept.junit.reporter.domain.TestCase;

public class SummaryBucket {
	public final String key;
	public int passed;
	public int skipped;
	public int failed;
	public int errored;
	public BigDecimal duration = BigDecimal.ZERO;
	
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
	
	public void include(SummaryBucket bucket) {
		passed += bucket.passed;
		skipped += bucket.skipped;
		failed += bucket.failed;
		errored += bucket.errored;
		duration = duration.add(bucket.duration);
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