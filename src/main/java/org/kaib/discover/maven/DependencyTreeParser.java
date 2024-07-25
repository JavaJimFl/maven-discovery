package org.kaib.discover.maven;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Note that the verbose
 */
public class DependencyTreeParser {
	
	public static void main(String[] args) throws TransformerException, XPathExpressionException {
		Path dependencyTree = Paths
				.get("C:\\temp\\workspace\\apache\\airavata-sandbox\\gsoc2022\\smilesdb\\Server\\dependencies.txt");

		final Document document = DependencyTreeParser.parse(dependencyTree);
		
		// Here you can write the DOM to an XML file or process it as needed
		// For example, using a transformer to print it
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(2));
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(System.out);
		transformer.transform(source, result);
		


	}

	public static Document parse(final Path textDependencyTreePath) {

		Document doc = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.newDocument();

			Element rootElement = doc.createElement("project");
			doc.appendChild(rootElement);

			Queue<String> lines = enqueueDependencies(textDependencyTreePath);
			parseLines(lines, doc, rootElement, 0);



		} catch (ParserConfigurationException e) {
			throw new ParseException("Parser not configured correctly", e);
		} catch (IOException e) {
			throw new ParseException("Failed to read dependency tree file", e);
		}

		return doc;
	}

	private static final String DEPENDENCY_REGEX = "^([+\\-\\|\\\\ ]+)([^:\\s]+):([^:\\s]+):([^:\\s]+):([^:\\s]+)(?::([^:\\s]+))?";
	private static final Pattern DEPENDENCY_PATTERN = Pattern.compile(DEPENDENCY_REGEX);

	private static final Pattern CURRENT_DEPTH_PATTERN = Pattern.compile("^([+\\-\\|\\\\ ]*)[a-z].*");

//	private static int getDepth(final Matcher matcher) {
//		final int currentDepth;
//		final Matcher matcher = CURRENT_DEPTH_PATTERN.matcher(line);
//		if (matcher.matches()) {
//			currentDepth = matcher.group(1).length();
//		} else {
//			currentDepth = 0;
//		}
//
//		return currentDepth;
//	}

	/**
	 * Recursive
	 * 
	 * @param doc    creates the dependency element
	 * @param parent the element to which dependency element will be appended
	 * @param line   contains the dependency to parse
	 * @param depth  the current depth in the dependency tree
	 */
	private static void parseLines(Queue<String> lines, Document doc, Element parent, int depth) {
		while (!lines.isEmpty()) {
			String line = lines.peek();
			final Matcher matcher = DEPENDENCY_PATTERN.matcher(line);
			if (matcher.matches()) {
				int newDepth = matcher.group(1).length();
				if (newDepth < depth) {
					return;
				}

				// Remove the line from the queue as it is being processed
				lines.poll();

				final String groupId = matcher.group(2);
				final String artifactId = matcher.group(3);
				final String packaging = matcher.group(4);
				final String version = matcher.group(5);
				// This will be null if not present
				final String scope = matcher.group(6);

				Element dependency = doc.createElement("dependency");
				dependency.setAttribute("groupId", groupId);
				dependency.setAttribute("artifactId", artifactId);
				dependency.setAttribute("packaging", packaging);
				dependency.setAttribute("version", version);
				if (scope != null) {
					dependency.setAttribute("scope", scope);
				}

				if (newDepth >= depth) {
					parent.appendChild(dependency);
				} else if (newDepth < depth) {
					Element lastChild = (Element) parent.getLastChild();
					if (lastChild != null) {
						lastChild.appendChild(dependency);
					}
				}
				parseLines(lines, doc, dependency, newDepth + 1);
			}
		}
	}

	private static int countLeadingChars(final String string, final char leadingChar) {

		int count = 0;
		for (char aChar : string.toCharArray()) {
			if (aChar == leadingChar) {
				count++;
			} else {
				break;
			}
		}

		return count;
	}

	private static Queue<String> enqueueDependencies(final Path filePath) throws IOException {

		Queue<String> queue = new LinkedList<>();
		BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()));
		String line;
		int count = 0;
		while ((line = reader.readLine()) != null) {

			if (count > 0 && !line.trim().isEmpty() && !line.contains("(")) {
				queue.add(line);
			}
			count++;
		}
		reader.close();
		return queue;
	}

}
