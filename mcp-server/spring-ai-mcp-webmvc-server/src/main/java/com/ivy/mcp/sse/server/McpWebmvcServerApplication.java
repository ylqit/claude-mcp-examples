package com.ivy.mcp.sse.server;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class McpWebmvcServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(McpWebmvcServerApplication.class, args);
    }

    @Bean
	public List<ToolCallback> myTools(MyTools myTools) {
		return List.of(ToolCallbacks.from(myTools));
	}

    @Bean
    public ToolCallbackProvider myToolsProvider(MyTools myTools) {
        return MethodToolCallbackProvider.builder().toolObjects(myTools).build();
    }


    @Service
    public static class MyTools {

        @Tool(description = "add two numbers")
        public Integer add(@ToolParam(description = "first number") int a,
                           @ToolParam(description = "second number") int b) {

            return a + b;
        }

        @Tool(description = "get current time")
        public LocalDateTime getCurrentTime() {
            return LocalDateTime.now();
        }
    }
}
