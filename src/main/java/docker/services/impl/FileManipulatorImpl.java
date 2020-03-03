package docker.services.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import docker.services.FileManipulator;

public class FileManipulatorImpl implements FileManipulator {

	final String prefix = "target/";

	public BufferedReader readInputFile(final String inputFileName)
			throws UnsupportedEncodingException, FileNotFoundException {

		final File inputFile = new File(inputFileName);
		final String decoded = URLDecoder.decode(inputFile.getAbsolutePath(), "UTF-8");
		final BufferedReader reader = new BufferedReader(new FileReader(decoded));

		return reader;

	}

	public List<String> readTestsPatternFile(final String inputFileName)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {

		final BufferedReader reader = this.readInputFile(inputFileName);
		List<String> testsPattern = new ArrayList<String>();
		String line;

		while ((line = reader.readLine()) != null) {
			testsPattern.add(line);
		}

		return testsPattern;

	}

	public BufferedWriter createOutputFile(final String outputFileName) throws IOException {

		final FileWriter outputFile = new FileWriter(outputFileName);
		final BufferedWriter writer = new BufferedWriter(outputFile);
		return writer;

	}

	public List<String> readMicroservicesFile(final String microservicesFileName) throws IOException {

		final BufferedReader reader = this.readInputFile(microservicesFileName);
		List<String> microservices = new ArrayList<String>();
		String line;

		while ((line = reader.readLine()) != null) {
			microservices = Arrays.asList(line.split("\\s*,\\s*"));
		}

		return microservices;

	}

	private File getFileFromResources(String fileName) {

		ClassLoader classLoader = getClass().getClassLoader();

		URL resource = classLoader.getResource(fileName);
		if (resource == null) {
			throw new IllegalArgumentException("Arquivo não encontrado!");
		} else {
			return new File(resource.getFile());
		}

	}

	public StringBuilder writeDockerCompose(BufferedReader reader, List<String> microservices,
			List<String> activeMicroservices) throws IOException {

		StringBuilder output = new StringBuilder();

		String line;
		Boolean isMicroserviceActive = false;
		Boolean isNetwork = false;

		while ((line = reader.readLine()) != null) {

			if (line.startsWith("version") || line.startsWith("services")) {

				output = output.append(line);
				output = output.append(System.getProperty("line.separator"));

			}

			for (int i = 0; i < microservices.size(); i++) {

				if (line.startsWith("  " + microservices.get(i))) {
					isMicroserviceActive = false;
					break;
				}
			}

			for (int i = 0; i < activeMicroservices.size(); i++) {

				if (line.startsWith("  " + activeMicroservices.get(i)) || line.endsWith("-mongo:")) {
					isMicroserviceActive = true;
					break;
				}
			}
			if (line.startsWith("networks")) {
				isNetwork = true;
			}

			if (isMicroserviceActive || isNetwork) {
				// System.out.println(line);
				output = output.append(line);
				output = output.append(System.getProperty("line.separator"));
			}

		}

		return output;
	}

	public StringBuilder writeTestFile(BufferedReader reader) throws IOException {

		final StringBuilder output = new StringBuilder();
		String commandLine = null;

		while ((commandLine = reader.readLine()) != null) {

			output.append(commandLine);

		}

		return output;

	}

	public StringBuilder getLogsFromTests(String logsBeforeTest, String logsAfterTest) throws IOException {

		BufferedReader readerBeforeTest = new BufferedReader(new FileReader(logsBeforeTest));

		BufferedReader readerAfterTest = new BufferedReader(new FileReader(logsAfterTest));

		String lineBeforeTest = readerBeforeTest.readLine();

		String lineAfterTest = readerAfterTest.readLine();

		StringBuilder output = new StringBuilder();

		while ((lineBeforeTest = readerBeforeTest.readLine()) != null) {

			lineAfterTest = readerAfterTest.readLine();
		}

		while ((lineAfterTest = readerAfterTest.readLine()) != null) {

			output.append(lineAfterTest + "\n");
		}

		readerAfterTest.close();
		readerBeforeTest.close();
		return output;

	}

}