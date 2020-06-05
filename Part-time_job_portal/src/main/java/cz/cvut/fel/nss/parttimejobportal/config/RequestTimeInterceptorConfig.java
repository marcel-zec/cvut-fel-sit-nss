package cz.cvut.fel.nss.parttimejobportal.config;

import cz.cvut.fel.nss.parttimejobportal.rest.interceptor.RequestTimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class RequestTimeInterceptorConfig extends WebMvcConfigurerAdapter {

    RequestTimeInterceptor requestTimeInterceptor;

    @Autowired
    public RequestTimeInterceptorConfig(RequestTimeInterceptor requestTimeInterceptor) {
        this.requestTimeInterceptor = requestTimeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestTimeInterceptor);
    }
}

