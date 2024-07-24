package org.kaib.discover.maven;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileUtils {

	private FileUtils() {
		super();
	}

	public void createStagingDirectory(final String repoName, final String workspaceDir,
			final String pomRelativePath) {

		final Path path = Paths.get(workspaceDir, repoName, pomRelativePath);
		path.toFile().getParentFile().mkdirs();
	}

}
