package org.kaib.discover.maven.process;

import org.kaib.discover.maven.config.Configuration;

public abstract class AbstractStep<I, O> implements Step<I, O>{
	
	private final Configuration configuration;
	
	public AbstractStep(Configuration theConfiguration) {
		
		this.configuration = theConfiguration;
	}

	public Configuration getConfiguration() {
		
		return configuration;
	}
}

