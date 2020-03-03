package docker.typescript;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import docker.services.ComposeRunner;
import docker.services.FileManipulator;
import docker.services.impl.ComposeRunnerImpl;
import docker.services.impl.FileManipulatorImpl;

public class TestsRunner {

	public static void main(String[] args) throws IOException {

		final String testsFileName = "input/tests-prefix";
		final String testsPath = "test/**/**/*.spec.ts";
		final String testsOutputPrefix = "target/tests/";
		final String testOutputSufix = ".out";

		final String logsBeforeTest = "logs_before.txt";
		final String logsAfterTest = "logs_after.txt";

		final FileManipulator fileManipulator = new FileManipulatorImpl();
		final ComposeRunner composeRunner = new ComposeRunnerImpl();

		final List<String> testsPattern = fileManipulator.readTestsPatternFile(testsFileName);

		BufferedWriter writer = fileManipulator
				.createOutputFile(testsOutputPrefix + testsPattern.get(0) + testOutputSufix);

		composeRunner.retrieveLogs(logsBeforeTest);

		final String diretorio = "cd ~/Documents/ocariot/repositories/system-test";
		final String runTest = "npm run test:single " + "-- --grep " + testsPattern.get(0) + " " + testsPath;

		final String command = diretorio + " && " + runTest;

		// chama o gcc

		final Process process = new ProcessBuilder(new String[] { "bash", "-c", command }).redirectErrorStream(true)
				.start();

		final BufferedReader commandBr = new BufferedReader(new InputStreamReader(process.getInputStream()));

		final StringBuilder output = fileManipulator.writeTestFile(commandBr);
		writer.write(output.toString());
		writer.close();

		composeRunner.retrieveLogs(logsAfterTest);

	}

}
