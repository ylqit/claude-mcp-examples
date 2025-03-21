package com.ivy.mcp.stdio.server.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class McpResourcesProvider {

    @Bean
    public List<McpServerFeatures.SyncResourceRegistration> syncResourceRegistrations() {

        var systemInfoResource = new McpSchema.Resource(
                "system://info",
                "System Information",
                "Provides basic system information including Java version, OS, etc.",
                "application/json", null
        );

        var resourceRegistration = new McpServerFeatures.SyncResourceRegistration(systemInfoResource, (request) -> {
            try {
                var systemInfo = Map.of(
                        "javaVersion", System.getProperty("java.version"),
                        "osName", System.getProperty("os.name"),
                        "osVersion", System.getProperty("os.version"),
                        "osArch", System.getProperty("os.arch"),
                        "processors", Runtime.getRuntime().availableProcessors(),
                        "timestamp", System.currentTimeMillis());

                String jsonContent = new ObjectMapper().writeValueAsString(systemInfo);

                return new McpSchema.ReadResourceResult(
                        List.of(new McpSchema.TextResourceContents(request.uri(), "application/json", jsonContent)));
            }
            catch (Exception e) {
                throw new RuntimeException("Failed to generate system info", e);
            }
        });

        return List.of(resourceRegistration);
    }
}
