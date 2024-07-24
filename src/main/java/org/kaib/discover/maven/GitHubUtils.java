package org.kaib.discover.maven;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.kohsuke.github.GHContent;

public class GitHubUtils {

	public static void writeFileContentToBaseDir(GHContent content, File destination) throws IOException {
		
        // Read the file content and write to the file at the full path.
        try (InputStream inputStream = content.read();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             BufferedWriter writer = new BufferedWriter(new FileWriter(destination, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        }

        System.out.println("File written to: " + destination.getAbsolutePath());
    }
}
