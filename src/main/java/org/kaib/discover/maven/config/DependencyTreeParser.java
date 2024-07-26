package org.kaib.discover.maven.config;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DependencyTreeParser {
    public static void main(String[] args) {
        String[] lines = {
            "+- org.springframework:spring-core:jar:5.3.8:compile",
            "|  \\- org.springframework:spring-jcl:jar:5.3.8:compile",
            "+- org.springframework:spring-context:jar:5.3.8:compile",
            "|  +- org.springframework:spring-aop:jar:5.3.8:compile",
            "|  +- org.springframework:spring-beans:jar:5.3.8:compile",
            "|  \\- org.springframework:spring-expression:jar:5.3.8:compile",
            "\\- junit:junit:jar:4.13.2:test",
            "   \\- org.hamcrest:hamcrest-core:jar:1.3:test",
            "+- org.example:example-dependency:jar:tests:1.2.3:compile",
            "|  \\- org.example:example-transitive:jar:tests:1.2.3:compile"
        };

        // Regular expression to match the dependency lines
        String regex = "^\\s*([+|\\\\-]+)\\s*([^\\s:]+):([^\\s:]+):([^\\s:]+)(?::([^\\s:]+))?:(\\S+):([^\\s:]+)\\s*$";
        Pattern pattern = Pattern.compile(regex);

        // Apply the regex to each line and print the matches
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                String depth = matcher.group(1);
                String groupId = matcher.group(2);
                String artifactId = matcher.group(3);
                String packaging = matcher.group(4);
                String classifier = matcher.group(5);  // Optional classifier
                String version = matcher.group(6);
                String scope = matcher.group(7);

                System.out.println("Depth: '" + depth + "'");
                System.out.println("Group ID: '" + groupId + "'");
                System.out.println("Artifact ID: '" + artifactId + "'");
                System.out.println("Packaging: '" + packaging + "'");
                System.out.println("Classifier: '" + classifier + "'");
                System.out.println("Version: '" + version + "'");
                System.out.println("Scope: '" + scope + "'");
                System.out.println();
            }
        }
    }
}
