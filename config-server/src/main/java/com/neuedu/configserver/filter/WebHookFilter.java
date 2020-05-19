package com.neuedu.configserver.filter;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

@WebFilter(urlPatterns = "/actuator/bus-refresh")
public class WebHookFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //使用HttpServletRequest包装原始请求达到修改post请求中body内容的目的
        try {
            chain.doFilter(new CustometRequestWrapper(request), response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
    }

    private static class CustometRequestWrapper extends HttpServletRequestWrapper {
        CustometRequestWrapper(ServletRequest request) {
            super((HttpServletRequest) request);
        }

        @Override
        public ServletInputStream getInputStream() {
            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return true;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                }

                @Override
                public int read() {
                    return -1;
                }
            };
        }
    }

}
