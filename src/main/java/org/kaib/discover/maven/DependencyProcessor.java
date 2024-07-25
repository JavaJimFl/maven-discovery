package org.kaib.discover.maven;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DependencyProcessor {

	private final XPathExpression xPathExpression;
	
	private final Pattern groupIdPattern;

	public DependencyProcessor() {
		
//		final String groupIdPattern = Configuration.INSTANCE.getSetting(ConfigKey.GROUP_ID_PATTERN);
		final String groupIdPattern = "org\\.mongodb";
		this.groupIdPattern = Pattern.compile(groupIdPattern);
		
		final XPath xpath = XPathFactory.newInstance().newXPath();
		try {
			xPathExpression = xpath.compile("//Dependency");
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}

	public NodeList evaluateXPath(Document document) throws XPathExpressionException {
		
		return (NodeList) this.xPathExpression.evaluate(document, XPathConstants.NODESET);
	}

	public List<Dependency> process(Document document) {
		try {
			NodeList nodes = evaluateXPath(document);

			// Add other filters here? The equivalent of an AND operation?
			NodeList filteredNodes = filterNodesByGroupId(nodes, groupIdPattern);
			return mapNodeListToDependencies(filteredNodes);
		} catch (XPathExpressionException e) {
			// Handle exception
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public Dependency mapNodeToDependency(Node node) {
		Dependency dependency = new Dependency();
		NamedNodeMap attributes = node.getAttributes();

		for (int i = 0; i < attributes.getLength(); i++) {
			Node attr = attributes.item(i);
			String attrName = attr.getNodeName();
			String attrValue = attr.getNodeValue();

			switch (attrName) {
			case "groupId":
				dependency.setGroupId(attrValue);
				break;
			case "artifactId":
				dependency.setArtifactId(attrValue);
				break;
			case "version":
				dependency.setVersion(attrValue);
				break;
			case "scope":
				dependency.setScope(attrValue);
				break;
			case "type":
				dependency.setType(attrValue);
				break;
			case "classifier":
				dependency.setClassifier(attrValue);
				break;
			}
		}

		Node parentNode = node.getParentNode();
		if (parentNode != null && "dependency".equals(parentNode.getNodeName())) {
			dependency.setTransitive(true);
			String path = getDependencyPath(node);
			System.out.println(path);
		} else {
			dependency.setTransitive(false);
		}

		return dependency;
	}

	public NodeList filterNodesByGroupId(NodeList nodes, Pattern pattern) {
		List<Node> filteredNodes = new ArrayList<>();

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			NamedNodeMap attributes = node.getAttributes();
			Node groupIdNode = attributes.getNamedItem("groupId");

			if (groupIdNode != null) {
				Matcher matcher = pattern.matcher(groupIdNode.getNodeValue());
				if (matcher.find()) {
					filteredNodes.add(node);
				}
			}
		}

		return new NodeList() {
			@Override
			public Node item(int index) {
				return filteredNodes.get(index);
			}

			@Override
			public int getLength() {
				return filteredNodes.size();
			}
		};
	}

	public List<Dependency> mapNodeListToDependencies(NodeList nodes) {
		List<Dependency> dependencies = new ArrayList<>();

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			Dependency dependency = mapNodeToDependency(node);
			dependencies.add(dependency);
		}

		return dependencies;
	}

	public String getDependencyPath(Node node) {
		List<String> path = new ArrayList<>();

		while (node != null && "dependency".equals(node.getNodeName())) {
			NamedNodeMap attributes = node.getAttributes();
			String groupId = attributes.getNamedItem("groupId").getNodeValue();
			String artifactId = attributes.getNamedItem("artifactId").getNodeValue();
			String version = attributes.getNamedItem("version").getNodeValue();
			path.add(groupId + ":" + artifactId + ":" + version);
			node = node.getParentNode();
		}

		Collections.reverse(path);
		return String.join(" -> ", path);
	}
}
