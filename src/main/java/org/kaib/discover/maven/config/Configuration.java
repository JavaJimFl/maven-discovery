package org.kaib.discover.maven.config;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Configuration {

	private String oAuthToken;

	private String baseRepoUrl;

	private String outputDir;
	
	private String outputFilePrefix;

	private String repoName;

	private String repoOwner;

	private String repoSearchQuery;

	private String workspaceDir;

	private String mavenInstallDir;

	private Configuration(final Builder builder) {

		this.oAuthToken = builder.bldrOAuthToken;
		this.baseRepoUrl = builder.bldrBaseRepoUrl;
		this.outputDir = builder.bldrOutputDir;
		this.outputFilePrefix = builder.bldrOutputFilePrefix;
		this.mavenInstallDir = builder.bldrMavenInstallDir;
		this.repoName = builder.bldrRepoName;
		this.repoOwner = builder.bldrRepoOwner;
		this.repoSearchQuery = builder.bldrRepoSearchQuery;
		this.workspaceDir = builder.bldrWorkspaceDir;
	}

	public static class Builder {

		private String bldrOAuthToken;
		private String bldrBaseRepoUrl;
		private String bldrOutputDir;
		private String bldrOutputFilePrefix;
		private String bldrRepoName;
		private String bldrRepoOwner;
		private String bldrRepoSearchQuery;
		private String bldrWorkspaceDir;
		private String bldrMavenInstallDir;
		
		public Builder withOutputFilePrefix(String outputFilePrefix) {
			this.bldrOutputFilePrefix = outputFilePrefix;
			return this;
		}

		public Builder withOutputDir(String outputDir) {
			this.bldrOutputDir = outputDir;
			return this;
		}

		public Builder withMavenInstallDir(String mavenInstallDir) {
			this.bldrMavenInstallDir = mavenInstallDir;
			return this;
		}

		public Builder withWorkspaceDir(String workspaceDir) {
			this.bldrWorkspaceDir = workspaceDir;
			return this;
		}

		public Builder withOAuthToken(String oAuthToken) {
			this.bldrOAuthToken = oAuthToken;
			return this;
		}

		public Builder withBaseRepoUrl(String baseRepoUrl) {
			this.bldrBaseRepoUrl = baseRepoUrl;
			return this;
		}

		public Builder withRepoName(String repoName) {
			this.bldrRepoName = repoName;
			return this;
		}

		public Builder withRepoOwner(String repoOwner) {
			this.bldrRepoOwner = repoOwner;
			return this;
		}

		public Builder withRepoSearchQuery(String repoSearchQuery) {
			this.bldrRepoSearchQuery = repoSearchQuery;
			return this;
		}

		public Configuration build() {
			return new Configuration(this);
		}
	}

	public String getOAuthToken() {
		return oAuthToken;
	}

	public String getBaseRepoUrl() {
		return baseRepoUrl;
	}
	
	public String getOutputDir() {
		return outputDir;
	}
	
	public String getOutputFilePrefix() {
		return outputFilePrefix;
	}

	public String getRepoName() {
		return repoName;
	}

	public String getRepoOwner() {
		return repoOwner;
	}

	public String getRepoSearchQuery() {
		return repoSearchQuery;
	}

	public String getWorkspaceDir() {
		return workspaceDir;
	}

	public String getMavenInstallDir() {
		return mavenInstallDir;
	}

	@Override
	public String toString() {
		// @formatter:off
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("oAuthToken", oAuthToken)
				.append("baseRepoUrl", baseRepoUrl)
				.append("mavenInstallDir", mavenInstallDir)
				.append("outputDir", outputDir)
				.append("outputFilePrefix", outputFilePrefix)
				.append("repoName", repoName).append("repoOwner", repoOwner)
				.append("repoSearchQuery", repoSearchQuery)
				.append("workspaceDir", workspaceDir)
				.toString();
		// @formatter:on
	}
}
