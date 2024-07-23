package org.kaib.discover.maven;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kaib.discover.maven.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVWriter;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

public class MavenDependencyWriter {

	Logger LOG = LoggerFactory.getLogger(MavenDependencyWriter.class);

	private Configuration configuration;

	public MavenDependencyWriter(Configuration theConfiguration) {

		super();
		this.configuration = theConfiguration;
	}

	public void write(List<FoundDependency> foundDependencies) throws Exception {

		ensureOutputDirectoryExists();

		final String outputPath = buildOutputPath();
		LOG.info("Writing {} dependencies to: {}", foundDependencies.size(), outputPath);

        Map<String, String> columnMapping = new HashMap<>();
        columnMapping.put("artifactId", "Artifact Id");
        columnMapping.put("groupId", "Group Id");
        columnMapping.put("lastCommitter", "Last Committer");
        columnMapping.put("path", "Path");
        columnMapping.put("repoName", "Repository Name");
        columnMapping.put("Repository Owner", "repoOwner");
        columnMapping.put("version", "Version");

        HeaderColumnNameTranslateMappingStrategy<ReportDependency> strategy = new HeaderColumnNameTranslateMappingStrategy<>();
        strategy.setType(ReportDependency.class);
        strategy.setColumnMapping(columnMapping);

		final List<ReportDependency> reportDependencies = DependencyMapper.map(foundDependencies);
		try (CSVWriter writer = new CSVWriter(new FileWriter(outputPath))) {

			// @formatter:off
			new StatefulBeanToCsvBuilder<ReportDependency>(writer)
				.withApplyQuotesToAll(true)
				.withMappingStrategy(strategy)
				.build()
				.write(reportDependencies);
			// @formatter:on
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
