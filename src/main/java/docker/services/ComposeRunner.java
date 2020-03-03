package docker.services;

import java.io.IOException;
import java.util.List;

public interface ComposeRunner {

	void runCompose(final String composeFileName) throws IOException;

	void killAllContainers(final String composeFileName, final List<String> activeMicroservices) throws IOException;

	void retrieveLogs(final String fileName) throws IOException;
}
