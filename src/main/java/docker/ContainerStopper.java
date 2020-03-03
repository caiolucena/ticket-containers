package docker;

import java.io.IOException;
import java.util.List;

import docker.services.ComposeRunner;
import docker.services.FileManipulator;
import docker.services.impl.ComposeRunnerImpl;
import docker.services.impl.FileManipulatorImpl;

public class ContainerStopper {

	public static void main(String[] args) throws IOException {

		final String activeMicroservicesFileName = "input/active-microservices.txt";
		final String outputFileName = "target/docker-compose.yml";

		final FileManipulator fileManipulator = new FileManipulatorImpl();
		final ComposeRunner composeRunner = new ComposeRunnerImpl();

		final List<String> activeMicroservices = fileManipulator.readMicroservicesFile(activeMicroservicesFileName);

		composeRunner.killAllContainers(outputFileName, activeMicroservices);

	}

}
