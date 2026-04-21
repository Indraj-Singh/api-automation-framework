package filters;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class RetryFilter implements Filter {

    private static final int MAX_RETRIES = 3;
    private static final long WAIT_TIME = 1000;


    @Override
    public Response filter(FilterableRequestSpecification filterableRequestSpecification,
                           FilterableResponseSpecification filterableResponseSpecification,
                           FilterContext filterContext) {

        Response response = null;
        int attempt = 0;

        while (attempt < MAX_RETRIES) {

            response = filterContext.next(filterableRequestSpecification, filterableResponseSpecification);
            int statusCode = response.getStatusCode();

            if (statusCode >= 500 || statusCode == 429) {
                attempt++;

                System.out.println("Retry attempt " + attempt +
                        " due to status code: " + statusCode);
                try {
                    Thread.sleep(WAIT_TIME);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

            } else {
                return response;
            }
        }

        return response;

    }
}
