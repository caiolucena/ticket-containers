package docker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import docker.services.FileManipulator;
import docker.services.impl.FileManipulatorImpl;

public class ActiveMicroserviceRetriever {

	public static void main(String[] args) throws IOException {

		final String logsBeforeTest = "target/logs_before.txt";
		final String logsAfterTest = "target/logs_after.txt";
		final String resultLogs = "target/result_logs.txt";
		final String microServices = "input/microservices.txt";

		final FileManipulator fileManipulator = new FileManipulatorImpl();
		
		List<String> microservices = fileManipulator.readMicroservicesFile(microServices);

		final File inputFile = new File(resultLogs);
		final String decoded = URLDecoder.decode(inputFile.getAbsolutePath(), "UTF-8");
		
		BufferedWriter  writer = fileManipulator.createOutputFile(resultLogs);

		final StringBuilder resultingLogs = fileManipulator.getLogsFromTests(logsBeforeTest, logsAfterTest);

		writer.write(resultingLogs.toString());
		writer.close();
	
		final BufferedReader reader = new BufferedReader(new FileReader(decoded));
		
		StringBuilder touchedMicroservices = new StringBuilder();
		
		String line ="";
		while (line != null) {
			for (String microservice : microservices) {

				if (line.contains(microservice)) {
					touchedMicroservices.append(microservice + "\n");					
					break;
				}
			}
			line = reader.readLine();

		}

		 writer = fileManipulator.createOutputFile("target/touchedMicroservices");

		writer.write(touchedMicroservices.toString());
		writer.close();

	}

}
