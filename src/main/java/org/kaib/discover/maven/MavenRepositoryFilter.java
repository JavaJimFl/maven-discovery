package org.kaib.discover.maven;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.kaib.discover.maven.config.Configuration;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHContentSearchBuilder;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedSearchIterable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MavenRepositoryFilter {

	Logger LOG = LoggerFactory.getLogger(MavenRepositoryFilter.class);

	private Configuration configuration;

	public MavenRepositoryFilter(final Configuration theConfiguration) {

		super();
		this.configuration = theConfiguration;
	}

	public Map<String, List<GHContent>> filter() throws IOException {

		// Authenticate with GitHub
		GitHub github = GitHub.connectUsingOAuth(configuration.getOAuthToken());

		// Define your search query
		String query = configuration.getRepoSearchQuery();
		LOG.info("Searching for repositories using query [{}]", query);

		// Search for code matching the query
		GHContentSearchBuilder searchBuilder = github.searchContent();
		PagedSearchIterable<GHContent> results = searchBuilder.q(query).list();
		

		// Iterate through the search results
		final Map<String, List<GHContent>> map = new TreeMap<>();
		for (GHContent content : results) {
			map.computeIfAbsent(content.getOwner().getFullName(), k -> new ArrayList<>()).add(content);
		}
		LOG.info("Mapped repositories for query [{}]", query);

		return map;
	}

}
