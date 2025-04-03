package com.ivy.mcp.mysql.prompt;

import com.ivy.mcp.mysql.tools.MySQLTool;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.ivy.mcp.mysql.util.StrUtil.MYSQL_SYSTEM_PROMPT;

public class MySQLPrompt {
    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLPrompt.class);

    public static McpServerFeatures.SyncPromptSpecification SyncPrompt() {
        final McpSchema.Prompt prompt = new McpSchema.Prompt(
                MYSQL_SYSTEM_PROMPT,
                "A prompt to seed system prompt for MySQL Server chat with mcp.",
                List.of(
                        new McpSchema.PromptArgument("resources_description", "A list of resources available to the user.", false),
                        new McpSchema.PromptArgument("tools_description", "A list of tools available to the user.", true)
                )
        );

        return new McpServerFeatures.SyncPromptSpecification(
                prompt,
                (exchange, request) -> {
                    LOGGER.debug("Handling get prompt request for {} with args:{}", prompt.name(), request.arguments());
                    String tools_description = (String) request.arguments().get("tools_description");

                    LOGGER.debug("Generated prompt template for resources:{}, tools:{}", tools_description);
                    return new McpSchema.GetPromptResult(
                            "",
                            List.of(
                                    new McpSchema.PromptMessage(
                                            McpSchema.Role.ASSISTANT,
                                            new McpSchema.TextContent(PROMPT_TEMPLATE
                                                    .replace("{resources_description}", MySQLTool.SyncListResources())
                                                    .replace("{tools_description}", tools_description)
                                            )
                                    )
                            )
                    );
                }
        );
    }

    private static final String PROMPT_TEMPLATE = """
                        You are a helpful assistant with access to these resources and tools: 
            
                        resources:{resources_description}
                        tools:{tools_description}
                        Choose the appropriate resource or tool based on the user's question. If no resource or tool is needed, reply directly.
            
                        IMPORTANT: When you need to use a resource, you must ONLY respond with the exact JSON object format below, nothing else:
                        {{
                            "resource": "resource-name",
                            "uri": "resource-URI"
                        }}
            
                        After receiving a resource's response:
                        1. Transform the raw data into a natural, conversational response
                        2. Keep responses concise but informative
                        3. Focus on the most relevant information
                        4. Use appropriate context from the user's question
                        5. Avoid simply repeating the raw data
            
                        IMPORTANT: When you need to use a tool, you must ONLY respond with the exact JSON object format below, nothing else:
                        {{
                            "tool": "tool-name",
                            "arguments": {{
                                "argument-name": "value"
                            }}
                        }}
            
                        After receiving a tool's response:
                        1. Transform the raw data into a natural, conversational response
                        2. Keep responses concise but informative
                        3. Focus on the most relevant information
                        4. Use appropriate context from the user's question
                        5. Avoid simply repeating the raw data
            
                        Please use only the resources or tools that are explicitly defined above.
            """;
}
