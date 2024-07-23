package org.kaib.discover.maven;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class ReportDependency {
	
//	@CsvBindByName(column = "Artifact Id", required = false)
//	@CsvBindByPosition(position = 5)
	private String artifactId;
	
//	@CsvBindByName(column = "Group Id", required = false)
//    @CsvBindByPosition(position = 4)
	private String groupId;
	
//	@CsvBindByName(column = "Last Committer", required = true)
//	@CsvBindByPosition(position = 2)
	private String lastCommitter;
	
//	@CsvBindByName(column = "Path", required = true)
//	@CsvBindByPosition(position = 3)
	private String path;
	
//    @CsvBindByName(column = "Repository Name", required = true)
//    @CsvBindByPosition(position = 0, required = true)
	private String repoName;
    
//    @CsvBindByName(column = "Repository Owner", required = true)
//    @CsvBindByPosition(position = 1)
	private String repoOwner;
    
//	@CsvBindByName(column = "Version", required = false)
//	@CsvBindByPosition(position = 6)
	private String version;

	public ReportDependency() {
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

	public void setLastCommitter(String lastCommitter) {
		this.lastCommitter = lastCommitter;
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