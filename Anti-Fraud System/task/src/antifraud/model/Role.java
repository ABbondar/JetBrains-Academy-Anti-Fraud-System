package antifraud.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMINISTRATOR, MERCHANT, SUPPORT;

    @Override
    public String getAuthority() {
        return name();
    }
}