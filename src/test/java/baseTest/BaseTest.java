package baseTest;

import auth.TokenManager;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;


@Listeners(listeners.TestListener.class)
public class BaseTest {


    @BeforeSuite(alwaysRun = true)
    @Parameters({"env"})
    public void setUp(@Optional("qa") String envXml){

        String env = System.getProperty("env",envXml);
        System.setProperty("env",env);

        TokenManager.getInstance().getToken();

    }
}
