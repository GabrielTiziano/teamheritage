package com.gabriel.tiziano.teamheritage.config.security.annotations.club;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyAuthority('SCOPE_club:write', 'SCOPE_admin:all')")
public @interface CanWriteClub {
}
