package docker.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import docker.services.ComposeRunner;

public class ComposeRunnerImpl implements ComposeRunner {

	final String logstashContainer = "logstash";

	public void runCompose(final String composeFileName) throws IOException {

		String command = "docker-compose -f " + composeFileName + " up";
		Process process = new ProcessBuilder(new String[] { "bash", "-c", command }).redirectErrorStream(true).start();

		ArrayList<String> outputCommand = new ArrayList<String>();
		BufferedReader commandBr = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String commandLine = null;

		while ((commandLine = commandBr.readLine()) != null) {
			outputCommand.add(commandLine);
			System.out.println("line " + commandLine);

		}

	}

	public void killAllContainers(final String composeFileName, List<String> activeMicroservices) throws IOException {

		for (int i = 0; i < activeMicroservices.size(); i++) {

			String command = "docker-compose -f " + composeFileName + " kill " + activeMicroservices.get(i);

			Process process = new ProcessBuilder(new String[] { "bash", "-c", command }).redirectErrorStream(true)
					.start();

			ArrayList<String> outputCommand = new ArrayList<String>();
			BufferedReader commandBr = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String commandLine = null;

			while ((commandLine = commandBr.readLine()) != null) {
				outputCommand.add(commandLine);
				System.out.println(commandLine);

			}

		}

	}

	public void retrieveLogs(final String fileName) throws IOException {

		String getLogs = "docker logs " + logstashContainer + " > " + fileName;
		final String diretorio = "cd ~/Documents/tcc/repositories/docker-script/target";

		final String command = diretorio + " && " + getLogs;

		final Process process = new ProcessBuilder(new String[] { "bash", "-c", command }).redirectErrorStream(true)
				.start();

	}

}
