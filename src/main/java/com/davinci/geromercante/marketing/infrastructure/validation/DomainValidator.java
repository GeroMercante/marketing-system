package com.davinci.geromercante.marketing.infrastructure.validation;

import com.davinci.geromercante.marketing.common.util.MessageUtil;
import com.davinci.geromercante.marketing.infrastructure.config.constants.AppConstant;
import com.davinci.geromercante.marketing.infrastructure.exception.MarketingException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DomainValidator {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static final String URL_REGEX = "^(https?://)([\\w.-]+)(:[0-9]+)?(/.*)?(\\?.*)?$";
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    private final MessageUtil messageUtil;

    @Lazy
    public DomainValidator(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    public void validatePageNumberAndSize(int page, int size) throws MarketingException {
        if (page < 0) {
            throw new MarketingException(messageUtil.getMessage("pageable.less.zero"));
        }

        if (size < 0) {
            throw new MarketingException(messageUtil.getMessage("number.less.zero"));
        }

        if (size > AppConstant.MAX_PAGE_SIZE) {
            throw new MarketingException(messageUtil.getMessage("pageable.must", AppConstant.MAX_PAGE_SIZE));
        }
    }

    public void validateEmail(String email) throws MarketingException {
        if (email == null || email.isEmpty()) {
            throw new MarketingException(messageUtil.getMessage("validator.email.empty"));
        }

        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            throw new MarketingException(messageUtil.getMessage("validator.email.invalid"));
        }
    }

    public void validateUrl(String url) throws MarketingException {
        if (url == null || url.isEmpty()) {
            throw new MarketingException(messageUtil.getMessage("validator.url.empty"));
        }

        Matcher matcher = URL_PATTERN.matcher(url);
        if (!matcher.matches()) {
            throw new MarketingException(messageUtil.getMessage("validator.url.invalid"));
        }
    }

    public void validateId(Long id) throws MarketingException {
        if (id == null || id < 0) {
            throw new MarketingException(messageUtil.getMessage("validator.id.not.valid"));
        }
    }
}
