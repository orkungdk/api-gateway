/**
 * © 2020 Copyright Amadeus Unauthorised use and disclosure strictly forbidden.
 */
package tr.com.ogedik.apigateway.filter;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import tr.com.ogedik.apigateway.filter.interrupter.orchestrator.RequestInterrupterOrchestrator;

/**
 * @author orkun.gedik
 */
@Component
public class RequestFilter extends ZuulFilter {

  private static final Logger logger = LogManager.getLogger(RequestFilter.class);

  @Autowired
  private RequestInterrupterOrchestrator requestInterrupterOrchestrator;

  @Override
  public String filterType() {
    return FilterConstants.PRE_TYPE;
  }

  @Override
  public int filterOrder() {
    return 1;
  }

  @Override
  public boolean shouldFilter() {
    return true;
  }

  @Override
  public Object run() {
    HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
    logger.info("The request has been interrupted by {} filter for method: {} request to {}", filterType(),
        request.getMethod(), request.getRequestURL().toString());

    requestInterrupterOrchestrator.interruptOnRequestReceived(request);

    return null;
  }
}
