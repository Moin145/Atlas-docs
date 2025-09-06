import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class JUnitTestRunner {
    public static void main(String[] args) {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(JUnitTest.class))
                .build();

        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();

        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        System.out.println("Total Tests Run: " + listener.getSummary().getTestsFoundCount());
        System.out.println("Tests Passed: " + (listener.getSummary().getTestsSucceededCount()));
        System.out.println("Tests Failed: " + (listener.getSummary().getTestsFailedCount()));
    }
}
