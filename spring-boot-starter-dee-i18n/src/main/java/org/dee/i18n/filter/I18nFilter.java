package org.dee.i18n.filter;

import org.dee.i18n.utils.I18nMessageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class I18nFilter extends OncePerRequestFilter {

    @Value("${org.dee.i18n.langage-type}")
    private String langageType;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String langage = request.getHeader(langageType);
        I18nMessageUtil.setLangage(langage);
        chain.doFilter(request, response);
    }

}
