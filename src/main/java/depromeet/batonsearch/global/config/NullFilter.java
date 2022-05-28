package depromeet.batonsearch.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class NullFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new RequestWrapper((HttpServletRequest) request), response);
    }

    private static class RequestWrapper extends HttpServletRequestWrapper {
        public RequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            String value = super.getParameter(name);

            if (value == null)
                return null;
            else if (value.equals("null"))
                return null;
            else
                return value;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            Map<String, String[]> parameterMap = super.getParameterMap();

            if (parameterMap == null)
                return null;

            for (String key: parameterMap.keySet()) {
                String[] strings = parameterMap.get(key);
                parameterMap.replace(key, Arrays.stream(strings).filter(x -> !x.equals("null")).toArray(String[]::new));
            }

            return parameterMap;
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] parameterValues = super.getParameterValues(name);
            if (parameterValues == null)
                return null;
            else
                return Arrays.stream(parameterValues).filter(x -> !x.equals("null")).toArray(String[]::new);
        }
    }
}
