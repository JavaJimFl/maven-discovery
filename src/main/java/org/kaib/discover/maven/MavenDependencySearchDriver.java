package org.kaib.discover.maven;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.ParseException;
import org.kaib.discover.maven.config.CommandLineUtils;
import org.kaib.discover.maven.config.Configuration;
import org.kohsuke.github.GHContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MavenDependencySearchDriver {

	private static final Logger LOG = LoggerFactory.getLogger(MavenDependencySearchDriver.class);

	public static void main(String[] args) {
		try {
			final Configuration config = CommandLineUtils.parseArgs(args);
			LOG.info("Parsed command line arguments: {}", config);
			
			final MavenRepositoryFilter filter = new MavenRepositoryFilter(config);
			final Map<String, List<GHContent>> filtered = filter.filter();
			
			final MavenDependencyFinder finder = new MavenDependencyFinder(config);
			final List<FoundDependency> foundDependencies = finder.find(filtered);
			
			final MavenDependencyWriter writer = new MavenDependencyWriter(config);
			writer.write(foundDependencies);
			
			LOG.info("Wrote found dependencies to: {}", config.getWorkspaceDir());

		} catch (ParseException e) {
			LOG.error("Failed to parse commmand-line arguments", e);
			System.exit(-1);
		} catch (IOException e) {
			LOG.error("An error occurred while processing dependencies", e);
			System.exit(-1);
		} catch (Exception e) {
			LOG.error("An unexpected error occurred", e);
			System.exit(-1);
		}
	}

}
