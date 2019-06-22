package com.kncept.junit.reporter.logger;

public class MavenWrapper implements LogFactory, Log{

	private final org.apache.maven.plugin.logging.Log log;
	
	public MavenWrapper(org.apache.maven.plugin.logging.Log log) {
		this.log = log;
	}
	
	@Override
	public Log logger(String name) {
		return this;
	}
	
	@Override
	public void info(String msg) {
		log.info(msg);
	}
	
	@Override
	public void debug(String msg) {
		log.debug(msg);
	}
	
}
