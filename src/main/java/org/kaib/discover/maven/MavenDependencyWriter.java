package org.kaib.discover.maven;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.kaib.discover.maven.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MavenDependencyWriter {

	Logger LOG = LoggerFactory.getLogger(MavenDependencyWriter.class);

	private Configuration configuration;

	private static final String[] CSV_HEADER = { "Repository Name", "Repository Owner", "Last Committer", "Path",
			"Group Id", "Artifact Id", "Version", "isTransitive"};

	public MavenDependencyWriter(Configuration theConfiguration) {

		super();
		this.configuration = theConfiguration;
	}

	public void write(List<FoundDependency> foundDependencies) throws Exception {

		ensureOutputDirectoryExists();

		final String outputPath = buildOutputPath();
		LOG.info("Writing {} dependencies to: {}", foundDependencies.size(), outputPath);

		final List<ReportDependency> reportDependencies = DependencyMapper.map(foundDependencies);
		CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setHeader(CSV_HEADER).build();
		try (FileWriter writer = new FileWriter(outputPath);
				CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {
			for (ReportDependency reportDependency : reportDependencies) {
				csvPrinter.printRecord(reportDependency.getRepoName(), 
						reportDependency.getRepoOwner(),
						reportDependency.getLastCommitter(), 
						reportDependency.getPath(),
						reportDependency.getGroupId(),
						reportDependency.getArtifactId(),
						reportDependency.getVersion());
			}
		} catch (IOException e) {
			LOG.error("Failed to write dependencies to: {}", outputPath, e);
			throw e;
		}

	}

	private String buildOutputPath() {

		return configuration.getOutputDir() + "/" + buildOutputFileName();
	}

	private String buildOutputFileName() {

		return this.configuration.getOutputFilePrefix() + "-" + format(LocalDateTime.now()) + ".csv";
	}

	private static String format(final LocalDateTime localDateTime) {

		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH_mm.SSS");

		return localDateTime.format(formatter);
	}

	public void ensureOutputDirectoryExists() {

		String outputDir = configuration.getOutputDir();
		File directory = new File(outputDir);

		if (directory != null && !directory.exists()) {
			boolean dirsCreated = directory.mkdirs();
			if (!dirsCreated) {
				throw new RuntimeException("Failed to create output file directories: " + directory.getAbsolutePath());
			}
		}
	}
}
