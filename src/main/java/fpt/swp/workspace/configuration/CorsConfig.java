package fpt.swp.workspace.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;


import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsConfig extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.addHeader("Access-Control-Max-Age", "3600");
        response.addHeader("Access-Control-Allow-Headers",
                "Origin, Content-Type, Accept, Authorization, X-Requested-With");
        response.addHeader("Access-Control-Expose-Headers", "xsrf-token");
        if("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        }else {
            filterChain.doFilter(request, response);
        }



    }
}
