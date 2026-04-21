package filters;


import com.aventstack.extentreports.ExtentTest;
import extentReport.ExtentManager;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.filter.Filter;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class CustomFilter implements Filter {



    @Override
    public Response filter(FilterableRequestSpecification filterableRequestSpecification,
                           FilterableResponseSpecification filterableResponseSpecification,
                           FilterContext filterContext) {

        // Request log
        String requestLog = "Request:\n" +
                "URI: " + filterableRequestSpecification.getURI() +
                "\nMethod: " + filterableRequestSpecification.getMethod() +
                "\nHeaders: " + filterableRequestSpecification.getHeaders() +
                "\nBody: " + filterableRequestSpecification.getBody();

        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.info(requestLog);
        }

        Response response = filterContext.next(filterableRequestSpecification, filterableResponseSpecification);

        String responseLog = "Response:\n" +
                "Status Code: " + response.getStatusCode() +
                "\nBody: " + response.asString();

        if (test != null) {
            test.info("Response:\n" + responseLog);
        }


        return response;
    }
}
