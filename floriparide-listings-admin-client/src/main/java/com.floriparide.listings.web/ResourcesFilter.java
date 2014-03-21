package com.floriparide.listings.web;

import java.io.*;
import java.net.URL;
import javax.servlet.*;
import javax.servlet.http.*;
/**
 * @author Andrei Tupitcyn
 */
public class ResourcesFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        if (!path.equals("/")) {
            String staticPath = "/static" + path;
            URL classpath = getClass().getClassLoader().getResource(staticPath);
            if (classpath != null) {
                request.getRequestDispatcher(staticPath).forward(request, response);
                return;
            }
        }
        request.getRequestDispatcher("/app" + path).forward(request, response);
    }

    @Override
    public void destroy() {

    }
}
