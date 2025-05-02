package com.readup.server;

import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

@AutoConfigureWireMock(port = 0, stubs = "classpath:mappings")
public abstract class WireMockSupport {
}
