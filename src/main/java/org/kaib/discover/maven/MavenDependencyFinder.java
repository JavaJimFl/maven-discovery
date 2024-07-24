package org.kaib.discover.maven;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHCommit.ShortInfo;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.PagedIterable;

public class MavenDependencyFinder {

	public MavenDependencyFinder() {
		super();
	}

	public List<FoundDependency> find(final Map<String, List<GHContent>> ghContentMap) throws IOException {

		final List<FoundDependency> foundDependencies = new ArrayList<>();
		for (String key : ghContentMap.keySet()) {
			for (final GHContent content : ghContentMap.get(key)) {
				foundDependencies.add(find(key, content));
			}
		}

		return foundDependencies;

	}

	public FoundDependency find(final String repoName, final GHContent content) throws IOException {

		final FoundDependency foundDependency = new FoundDependency();
		foundDependency.setRepoName(repoName);
		foundDependency.setRepoOwner(content.getOwner().getOwnerName());

		final String relativePomPath = content.getPath();
		foundDependency.setPath(content.getPath());
		foundDependency.setLastCommitter(getLastCommiter(content.getOwner()));

		final String workspaceDir = Configuration.INSTANCE.getSetting(ConfigKey.WORKSPACE_DIR);
		final File stagingFile = Paths.get(workspaceDir, repoName, relativePomPath).toFile();
		stagingFile.getParentFile().mkdirs();

		GitHubUtils.writeFileContentToBaseDir(content, stagingFile);

		return foundDependency;
	}

	private String getLastCommiter(GHRepository repository) throws IOException {

		PagedIterable<GHCommit> commits = repository.listCommits();

		// Get the first page's first commit (most recent commit)
		GHCommit lastCommit = commits.iterator().next();
		ShortInfo commitInfo = lastCommit.getCommitShortInfo();

		return commitInfo.getCommitter().getName();
	}

//	public static void main(String[] args) throws Exception {
//
//		final Path textDependencyTreePath = Paths.get("C:/temp/myfile.txt");
//		final Document document = parseTextDependencyTree(textDependencyTreePath);
//		TransformerFactory transformerFactory = TransformerFactory.newInstance();
//		Transformer transformer = transformerFactory.newTransformer();
//        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//		DOMSource source = new DOMSource(document);
//		StreamResult result = new StreamResult(System.out);
//		transformer.transform(source, result);
//
//	}
//
//	private static Document parseTextDependencyTree(final Path textDependencyTreeFile) {
//
//		return DependencyTreeParser.parse(textDependencyTreeFile);
//	}

}
