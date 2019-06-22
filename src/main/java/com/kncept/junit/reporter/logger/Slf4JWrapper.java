package com.kncept.junit.reporter.logger;

import org.gradle.api.logging.Logging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4JWrapper implements LogFactory {

	@Override
	public Log logger(String name) {
		return new Slf4JLog(LoggerFactory.getLogger(name));
	}
	
	private class Slf4JLog implements Log {
		private final Logger logger;
		public Slf4JLog(Logger logger) {
			this.logger = logger;
		}
		@Override
		public void info(String msg) {
			logger.info(Logging.LIFECYCLE, msg);
		}
		@Override
		public void debug(String msg) {
			logger.debug(msg);
		}
	}
	
}
