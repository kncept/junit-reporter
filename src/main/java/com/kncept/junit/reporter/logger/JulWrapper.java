package com.kncept.junit.reporter.logger;

import java.util.logging.Logger;

public class JulWrapper implements LogFactory {

	@Override
	public Log logger(String name) {
		return new JulLog(Logger.getLogger(name));
	}
	
	private class JulLog implements Log {
		private final Logger logger;
		public JulLog(Logger logger) {
			this.logger = logger;
		}
		@Override
		public void info(String msg) {
			logger.info(msg);
		}
		@Override
		public void debug(String msg) {
			logger.fine(msg);
		}
	}
	
}
