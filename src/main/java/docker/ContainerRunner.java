package docker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import docker.services.ComposeRunner;
import docker.services.FileManipulator;
import docker.services.impl.ComposeRunnerImpl;
import docker.services.impl.FileManipulatorImpl;

public class ContainerRunner {

	public static void main(String[] args) throws IOException {

		final String run = args[1];

		final String inputFileName = (new java.io.File("./")).getCanonicalPath()
				+ "/src/main/resources/input/docker-compose.yml";
		final String microservicesFileName = (new java.io.File("./")).getCanonicalPath()
				+ "/src/main/resources/input/microservices.txt";
		final String activeMicroservicesFileName = (new java.io.File("./")).getCanonicalPath()
				+ "/src/main/resources/input/active-microservices.txt";
		final String outputFileName = (new java.io.File("./")).getCanonicalPath() + "/target/docker-compose.yml";

		final FileManipulator fileManipulator = new FileManipulatorImpl();
		final ComposeRunner composeRunner = new ComposeRunnerImpl();

		System.out.println(run);
		
		// Se não for o primeiro run, ele pega os microserviços ativos 
		// E então gera o compose somente com os serviços ativos e então o executa
		
		if (!run.equals("1")) {
			final BufferedReader reader = fileManipulator.readInputFile(inputFileName);
			final BufferedWriter writer = fileManipulator.createOutputFile(outputFileName);

			final List<String> microservices = fileManipulator.readMicroservicesFile(microservicesFileName);
			final List<String> activeMicroservices = fileManipulator.readMicroservicesFile(activeMicroservicesFileName);

			final StringBuilder output = fileManipulator.writeDockerCompose(reader, microservices, activeMicroservices);

			writer.write(output.toString());
			writer.close();
			//composeRunner.runCompose(outputFileName);

		} else {
			// Se for o primeiro run, ele copia o arquivo docker-compose completo para a pasta target
			// E então executa o compose
			
			File original= new File(inputFileName);
			File copied = new File(outputFileName);
			FileUtils.copyFile(original, copied);
			
			//composeRunner.runCompose(outputFileName);
			
		}
		List<ITestResult> results = runTest(TestServiceLogin1.class);

	}
	
	public static List<ITestResult> runTest(Class test) {
		List<ITestResult> results = new ArrayList<ITestResult>();
		TestListenerAdapter tla = new TestListenerAdapter();
		TestNG testng = new TestNG();
		//, TestFlowTwoPay.class
		testng.setTestClasses(new Class[] { test });
		testng.addListener(tla);
		testng.run();
		
		if(!tla.getPassedTests().isEmpty())
			results = tla.getPassedTests();
		else results = tla.getFailedTests();
		
		return results;
	}

}
