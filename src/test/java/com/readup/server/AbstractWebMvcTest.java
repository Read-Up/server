package com.readup.server;

import org.springframework.context.annotation.Import;

import com.readup.server.common.config.SecurityTestConfig;

@Import(SecurityTestConfig.class)
public abstract class AbstractWebMvcTest {
}
