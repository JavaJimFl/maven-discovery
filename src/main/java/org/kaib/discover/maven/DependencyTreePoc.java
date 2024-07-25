package org.kaib.discover.maven;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.w3c.dom.Document;

public class DependencyTreePoc {

	public static void main(String[] args) throws MavenInvocationException, TransformerException {

		Path pomFilePath = Paths
				.get("C:\\temp\\workspace\\apache\\airavata-sandbox\\gsoc2022\\smilesdb\\Server\\pom.xml");
		final Path dependencyTree = buildDependencyTree(pomFilePath);

		final Document document = DependencyTreeParser.parse(dependencyTree);
		
		
        final DependencyProcessor processor = new DependencyProcessor();
        final List<Dependency> dependencies = processor.process(document);
        System.out.println("Dependencies " + dependencies);
		
		// Here you can write the DOM to an XML file or process it as needed
		// For example, using a transformer to print it
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(System.out);
		transformer.transform(source, result);
	}

	private static Path buildDependencyTree(Path pomFilePath) {

		final Path parentDir = pomFilePath.getParent();
		final Path logFilePath = parentDir.resolve("maven-dependencyTree.log");
		try (FileOutputStream fos = new FileOutputStream(logFilePath.toFile());
				PrintStream printStream = new PrintStream(fos, true)) {

			// Redirect everything Maven generates to the log file.
			System.setOut(printStream);
			System.setErr(printStream);

			InvocationRequest request = new DefaultInvocationRequest();
			request.setPomFile(pomFilePath.toFile());
			request.addArg("dependency:tree");

			Properties properties = new Properties();
			properties.setProperty("outputFile", "dependencies.txt");
			request.setProperties(properties);

			Invoker invoker = new DefaultInvoker();
			invoker.setMavenHome(new File("C:\\tools\\Apache\\apache-maven-3.8.8"));

			InvocationOutputHandler outputHandler = line -> {
				printStream.println(line);
				printStream.flush();
			};

			request.setOutputHandler(outputHandler);
			request.setErrorHandler(outputHandler);

			InvocationResult result = invoker.execute(request);
			if (result.getExitCode() != 0) {
				throw new IllegalStateException("Build failed.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MavenInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parentDir.resolve("dependencies.txt");
	}

}
