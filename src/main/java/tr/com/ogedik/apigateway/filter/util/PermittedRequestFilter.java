package tr.com.ogedik.apigateway.filter.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import tr.com.ogedik.apigateway.constants.ApiGatewayConstants;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author orkun.gedik
 */
public class PermittedRequestFilter {

    private static PermittedRequestFilter instance;
    private List<Matcher> matchers;

    //TODO: Add token authentication for time tracker paths.
    private PermittedRequestFilter() {
        matchers = new ArrayList<>();
        matchers.add(new Matcher(HttpMethod.GET, ApiGatewayConstants.Paths.API + ApiGatewayConstants.Paths.AUTHENTICATE));
        matchers.add(new Matcher(HttpMethod.GET, ApiGatewayConstants.Paths.API + ApiGatewayConstants.Paths.JIRA_CONFIG));
        matchers.add(new Matcher(HttpMethod.POST, ApiGatewayConstants.Paths.API + ApiGatewayConstants.Paths.AUTHENTICATE));
        matchers.add(new Matcher(HttpMethod.POST, ApiGatewayConstants.Paths.API + ApiGatewayConstants.Paths.REGISTRATION));
        matchers.add(new Matcher(HttpMethod.POST, ApiGatewayConstants.Paths.API + ApiGatewayConstants.Paths.JIRA_CONNECTION_TEST));
        matchers.add(new Matcher(HttpMethod.POST, ApiGatewayConstants.Paths.API + ApiGatewayConstants.Paths.SETUP));
        matchers.add(new Matcher(HttpMethod.GET, ApiGatewayConstants.Paths.API + ApiGatewayConstants.Paths.WORKLOGS));
        matchers.add(new Matcher(HttpMethod.POST, ApiGatewayConstants.Paths.API + ApiGatewayConstants.Paths.WORKLOGS));
    }

    public static PermittedRequestFilter getInstance() {
        if (instance == null) {
            instance = new PermittedRequestFilter();
        }

        return instance;
    }

    public boolean isAuthenticationRequired(HttpServletRequest httpServletRequest) {
        return matchers.stream()
                .filter(matcher -> httpServletRequest.getRequestURL().toString().contains(matcher.requestPath))
                .noneMatch(matcher -> httpServletRequest.getMethod().equals(matcher.httpMethod.name()));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    class Matcher {
        private HttpMethod httpMethod;
        private String requestPath;
    }
}
