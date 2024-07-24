package org.kaib.discover.maven;

public enum ConfigKey {

	BASE_REPO_URL("baseRepoUrl"),

	MAVE_HOME("mavenHome"),
	
	OUTPUT_FILE_PREFIX("outputFilePrefix"),

	OAUTH_TOKEN("oAuthToken"),

	OUTPUT_DIR("outputDir"),

	REPO_NAME("repoName"),

	REPO_OWNER("repoOwner"),
	
	REPO_SEARCH_QUERY("repoSearchQuery"),

	REPO_TYPE("repoType"),

	WORKSPACE_DIR("workspaceDir");

	private final String type;

	private ConfigKey(final String theType) {
		this.type = theType;
	}

	public String getKey() {
		return type;
	}

	public static ConfigKey lookup(final String theKey) {

		for (ConfigKey value : ConfigKey.values()) {
			if (value.getKey().equalsIgnoreCase(theKey)) {
				return value;
			}
		}

		throw new IllegalArgumentException("Unknown configuration key: " + theKey);
	}
}
