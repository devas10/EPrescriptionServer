package com.Devas.BackendPrescription.security;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

public interface ResourceRenderer {
    void addResourceHandlers(ResourceHandlerRegistry registry);
}
