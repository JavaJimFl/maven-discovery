package org.kaib.discover.maven.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.kaib.discover.maven.RepositoryType;
import org.kaib.discover.maven.config.Configuration.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandLineUtils {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(CommandLineUtils.class);

	public static Configuration parseArgs(final String[] args) throws ParseException {

		LOG.info("Parsing command line arguments");

		// Create Options
		Options options = new Options();

		// The bare minimum required to run the application. Not required when properties file is provided, but must be
		// passed in when properties file is not provided.
		options.addOption("t", "token", true, "OAuth token");
		options.addOption("u", "url", true, "Base repository URL");

		// Must account for all arguments available from the command line.
		options.addOption("p", "properties", true, "Properties file location");

		// Used when the properties file argument is not provided.
		options.addOption("rn", "repoName", true, "Repository name");
		options.addOption("rt", "repoType", true, "Repository type");
		options.addOption("ro", "owner", true, "Repository owner");
		options.addOption("a", "artifactId", true, "Maven artifact ID");
		options.addOption("g", "groupId", true, "Maven group ID");
		options.addOption("v", "version", true, "Maven dependency version");
		options.addOption("s", "scope", true, "Maven artifact scope");
		options.addOption("c", "classifier", true, "Maven classifier");
		options.addOption("s", "repoSearchQuery", true, "Respository filter query");
		options.addOption("w", "workspaceDir", true, "Batch workspace directory");
		options.addOption("m", "mavenHome", true, "Maven installation directory");
		options.addOption("od", "outputDir", true, "Output file location");
		options.addOption("op", "outputFilePrefix", true, "Output file prefix");

		options.addOption("h", "help", false, "Print help");

		// Parse the command line arguments.
		CommandLineParser parser = new DefaultParser();
		final Builder builder = new Configuration.Builder();
		try {
			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption("p")) {
				LOG.info("Loading arguments from properties file: {}", cmd.getOptionValue("p"));
				loadPropertiesFile(cmd, builder);

				// Throw an exception if any other command line arguments are provided when properties file is provided.
				if (cmd.getOptions().length > 1) {
					throw new IllegalArgumentException(
							"No other command line arguments should be provided when -p <properties file> is provided.");
				}
			} else {
				LOG.info("No properties file argument declared; loading arguments from command line options");

				// Required arguments
				builder.withOAuthToken(getOauthToken(cmd));
				builder.withMavenInstallDir(getMavenInstallDir(cmd));
				builder.withWorkspaceDir(getWorkspaceDir(cmd));
				// Output directory and file prefix
				parseRepoArgs(cmd, builder);

				// Optional arguments
				builder.withRepoSearchQuery(cmd.getOptionValue("s"));
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to load properties file: " + e.getMessage(), e);
		}

		LOG.info("Command line arguments parsed successfully");
		final Configuration configuration = builder.build();
		LOG.info("Loaded configuration: {}", configuration);

		return configuration;
	}

	private static void parseRepoArgs(CommandLine cmd, Builder builder) {

		// Personal repositories don't require a GitHub URL.
		if (cmd.hasOption("rt")) {
			final RepositoryType repoType = RepositoryType.lookup(cmd.getOptionValue("rt"));
			if (RepositoryType.ENTERPRISE == repoType) {
				if (cmd.hasOption("u")) {
					builder.withBaseRepoUrl(cmd.getOptionValue("u"));
				} else {
					throw new IllegalArgumentException(
							"The base repository URL must be provided for enterprise repositories, e.g. -u <url>");
				}
				if (cmd.hasOption("rn")) {
					builder.withRepoName(cmd.getOptionValue("rn"));
				} else {
					throw new IllegalArgumentException(
							"The repository name must be provided for enterprise repositories, e.g. -rn <name>");
				}
				if (cmd.hasOption("o")) {
					builder.withRepoOwner(cmd.getOptionValue("o"));
				} else {
					throw new IllegalArgumentException(
							"The repository owner must be provided for enterprise repositories, e.g. -o <owner>");
				}
			}
		}
	}

	private static void parseRepoArgs(Properties props, Builder builder) {

		// Personal repositories don't require a GitHub URL.
		final String repoTypeStr = props.getProperty("repoType");
		Validate.notBlank(repoTypeStr,
				"The repository type must be provided in the properties file, e.g. repoType=<type>");

		final RepositoryType repoType = RepositoryType.lookup(repoTypeStr);
		if (RepositoryType.ENTERPRISE == repoType) {
			final String baseRepoUrl = props.getProperty("baseRepoUrl");
			if (StringUtils.isNotBlank(baseRepoUrl)) {
				builder.withBaseRepoUrl(baseRepoUrl);
			} else {
				throw new IllegalArgumentException(
						"The base repository URL must be provided in the properties file for enterprise "
								+ "repositories, e.g. -u <url>");
			}
			final String repoName = props.getProperty("repoName");
			if (StringUtils.isNotBlank(repoName)) {
				builder.withRepoName(repoName);
			} else {
				throw new IllegalArgumentException(
						"The repository name must be provided in the properties file for enterprise"
								+ " repositories, e.g. -rn <repoName>");
			}
			final String repoOwner = props.getProperty("repoOwner");
			if (StringUtils.isNotBlank(repoOwner)) {
				builder.withRepoOwner(repoOwner);
			} else {
				throw new IllegalArgumentException(
						"The repository owner must be provided in the properties file for enterprise"
								+ " repositories, e.g. -o <repoOwner>");
			}
		}

	}

	private static String getOauthToken(final CommandLine cmd) {

		return getRequiredArg(cmd, "t", "The OAuth token must be provided, e.g. -t <token>");
	}

	private static String getWorkspaceDir(final CommandLine cmd) {

		return getRequiredArg(cmd, "w", "The workspace directory must be provided, e.g. -w <dir>");
	}

	private static String getMavenInstallDir(final CommandLine cmd) {

		return getRequiredArg(cmd, "m", "The maven install directory must be provided, e.g. -m <dir>");
	}

	private static String getRequiredArg(final CommandLine cmd, final String optionName, final String errorMessage) {

		final String option = cmd.getOptionValue(optionName);
		if (StringUtils.isBlank(option)) {
			throw new IllegalArgumentException(errorMessage);
		} else {
			return option;
		}
	}

	private static String getRequiredArg(final Properties props, final String key, final String errorMessage) {

		final String option = props.getProperty(key);
		if (StringUtils.isBlank(option)) {
			throw new IllegalArgumentException(errorMessage);
		} else {
			return option;
		}
	}

	private static String getOauthToken(final Properties properties) {

		return getRequiredArg(properties, "oAuthToken",
				"The OAuth token must be provided in the properties file, e.g. oAuthToken=<token>");
	}

	private static String getWorkspaceDir(final Properties properties) {

		return getRequiredArg(properties, "workspaceDir",
				"The workspace directory must be provided in the properties file, e.g. workspaceDir=<dir>");
	}

	private static String getMavenInstallDir(final Properties properties) {

		return getRequiredArg(properties, "mavenHome",
				"The maven install directory must be provided in the properties file, e.g. mavenHome=<dir>");
	}
	
	private static String getOutputDir(final Properties properties) {

		return getRequiredArg(properties, "outputDir",
				"The output directory must be provided in the properties file, e.g. outputDir=<dir>");
	}
	
	private static String getOutputFilePrefix(final Properties properties) {

		return getRequiredArg(properties, "outputFilePrefix",
				"The output file prefix must be provided in the properties file, e.g. outputFilePrefix=<prefix>");
	}

	private static void loadPropertiesFile(final CommandLine cmd, final Builder builder) throws IOException {

		final Properties properties = new Properties();
		properties.load(new FileInputStream(cmd.getOptionValue("p")));

		// Required properties
		builder.withOAuthToken(getOauthToken(properties));
		builder.withWorkspaceDir(getWorkspaceDir(properties));
		builder.withMavenInstallDir(getMavenInstallDir(properties));
		builder.withOutputDir(getOutputDir(properties));
		builder.withOutputFilePrefix(getOutputFilePrefix(properties));
		parseRepoArgs(properties, builder);

		// Optional properties
		builder.withRepoSearchQuery(properties.getProperty("repoSearchQuery"));

	}
}
