package org.kaib.discover.maven;

import java.util.List;
import java.util.stream.Collectors;

public class DependencyMapper {
	
	public static List<ReportDependency> map(final List<FoundDependency> sources) {
		
		return sources.stream().map(source -> {
			
			ReportDependency target = new ReportDependency();
			target.setRepoName(source.getRepoName());
			target.setRepoOwner(source.getRepoOwner());
			target.setGroupId(source.getGroupId());
			target.setArtifactId(source.getArtifactId());
			target.setVersion(source.getVersion());
			target.setPath(source.getPath());
			target.setLastCommitter(source.getLastCommitter());
			
			return target;
		}).collect(Collectors.toList());
	}

}
