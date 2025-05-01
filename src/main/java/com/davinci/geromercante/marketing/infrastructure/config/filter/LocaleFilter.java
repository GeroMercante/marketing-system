package com.davinci.geromercante.marketing.infrastructure.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.io.IOException;
import java.util.Locale;

@Component
public class LocaleFilter implements Filter {

    private final LocaleResolver localeResolver;

    public LocaleFilter(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String language = httpRequest.getHeader("Lang");

        if (language != null && (language.equals("en") || language.equals("es"))) {
            localeResolver.setLocale(httpRequest, httpResponse, Locale.forLanguageTag(language));
        } else {
            localeResolver.setLocale(httpRequest, httpResponse, Locale.forLanguageTag("es"));
        }

        chain.doFilter(request, response);
    }

}