package org.kaib.discover.maven;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class FoundDependency {

	private String artifactId;
	private String groupId;
	private String lastCommitter;
	private String path;
	private String repoName;
	private String repoOwner;
	private String version;

	public FoundDependency() {
		super();
	}

	public String getArtifactId() {
		return artifactId;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getLastCommitter() {
		return lastCommitter;
	}

	public String getPath() {
		return path;
	}

	public String getRepoName() {
		return repoName;
	}

	public String getRepoOwner() {
		return repoOwner;
	}

	public String getVersion() {
		return version;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public void setLastCommitter(String lastCommiter) {
		this.lastCommitter = lastCommiter;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	public void setRepoOwner(String repoOwner) {
		this.repoOwner = repoOwner;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		// @formatter:off
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("artifactId", artifactId)
				.append("groupId", groupId)
				.append("lastCommitter", lastCommitter)
				.append("path", path)
				.append("repoName", repoName)
				.append("repoOwner", repoOwner)
				.append("version", version)
				.toString();
		// @formatter:on
	}

}