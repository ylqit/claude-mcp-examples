package com.ivy.mcp.stdio.server.tools;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Function;

/**
 * @author fangjie
 */
@Configuration
public class McpToolProvider {
    /**
     *  注册工具第一种方式
     *
     * @param myTools
     * @return
     */
//     @Bean
//     public ToolCallbackProvider tools(MyTools myTools) {
//         return MethodToolCallbackProvider.builder().toolObjects(myTools).build();
//     }


    /**
     * 注册工具第二种方式
     * @return
     */
//     @Bean
//     public ToolCallbackProvider tools() {
//         return ToolCallbackProvider.from(
//                 FunctionToolCallback.builder("toUpperCase", (Function<ToUpperCaseInput, String>) input -> input.input().toUpperCase())
//                         .description("convert string to uppercase")
//                         .inputType(ToUpperCaseInput.class)
//                         .build()
//         );
//     }

    /**
     * 注册工具第三种方式
     *
     * @return
     */
    // @Bean
    // public List<ToolCallback> tools() {
    //     return List.of(
    //         FunctionToolCallback.builder("toUpperCase", (Function<ToUpperCaseInput, String>) input -> input.input().toUpperCase())
    //                     .description("convert string to uppercase")
    //                     .inputType(ToUpperCaseInput.class)
    //                     .build()
    //     );
    // }

    /**
     * 注册工具
     *
     * @param myTools 工具类
     * @return 工具列表
     */
    @Bean
    public List<ToolCallback> tools(MyTools myTools) {
        List<ToolCallback> toolCallbacks = new java.util.ArrayList<>(List.of(ToolCallbacks.from(myTools)));
        FunctionToolCallback<ToUpperCaseInput, String> upperCaseTool =
                FunctionToolCallback.builder(
                                "toUpperCase",
                                (Function<ToUpperCaseInput, String>) input -> input.input().toUpperCase())
                        .description("convert string to uppercase")
                        .inputType(ToUpperCaseInput.class)
                        .build();

        toolCallbacks.add(upperCaseTool);

        return toolCallbacks;
    }

    public record ToUpperCaseInput(String input) {
    }

}
