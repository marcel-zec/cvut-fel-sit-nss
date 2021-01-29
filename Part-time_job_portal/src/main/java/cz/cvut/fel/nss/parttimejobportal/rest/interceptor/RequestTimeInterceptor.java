package cz.cvut.fel.nss.parttimejobportal.rest.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.cvut.fel.nss.parttimejobportal.service.OfferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * RequestTimeInterceptor - class measure time of requests and logs it.
 */
@Component
public class RequestTimeInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(RequestTimeInterceptor.class);
    private long startTime;
    private long stopTime;

    /**
     * preHandle method start measure nanoTime at the beginning of request.
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        startTime = System.nanoTime();
        return true;
    }

    /**
     * postHandle method end measure nanoTime at the end of request.
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        stopTime = System.nanoTime();
    }

    /**
     * afterCompletion count difference between start and end time and log this time.
     * @param request
     * @param response
     * @param handler
     * @param exception
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
        double time = (double) (stopTime - startTime) / 1_000_000_000;
        LOG.info("Request time: "+ time + " seconds.");
    }
}