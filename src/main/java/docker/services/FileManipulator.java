package docker.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface FileManipulator {

	BufferedReader readInputFile(String inputFileName) throws UnsupportedEncodingException, FileNotFoundException;

	List<String> readTestsPatternFile(String inputFileName)
			throws UnsupportedEncodingException, FileNotFoundException, IOException;

	BufferedWriter createOutputFile(String outputFileName) throws IOException;

	List<String> readMicroservicesFile(String microservicesFileName)
			throws UnsupportedEncodingException, FileNotFoundException, IOException;

	StringBuilder writeDockerCompose(BufferedReader reader, List<String> microservices,
			List<String> activeMicroservices) throws IOException;

	public StringBuilder writeTestFile(BufferedReader reader) throws IOException;
	
	public StringBuilder getLogsFromTests(String logsBeforeTest, String logsAfterTest) throws IOException;

}
