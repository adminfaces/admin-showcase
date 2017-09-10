package com.github.adminfaces.showcase.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rmpestano on 22/06/17.
 */
@WebFilter(urlPatterns = "/*")
public class BlackListFilter implements Filter {

    private static final List<String> BLACK_LIST  = Arrays.asList("5.255.250","141.8.143","77.88.47","84.201.133","100.43.85","199.21.99","66.249.65");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String ip = servletRequest.getRemoteAddr();

        if(isBlocked(ip)) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        filterChain.doFilter(request,response);

    }

    @Override
    public void destroy() {

    }

    public static boolean isBlocked(String ip) {
        for (String blockedIp : BLACK_LIST) {
            if(ip.startsWith(blockedIp)) {
                return true;
            }
        }
        return false;
    }

}
