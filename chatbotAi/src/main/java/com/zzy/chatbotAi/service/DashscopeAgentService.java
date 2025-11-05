package com.zzy.chatbotAi.service;

import com.alibaba.cloud.ai.dashscope.agent.DashScopeAgent;
import com.alibaba.cloud.ai.dashscope.agent.DashScopeAgentOptions;
import com.alibaba.cloud.ai.dashscope.api.DashScopeAgentApi;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class DashscopeAgentService {
    private DashScopeAgent dashScopeAgent;

    @Value("${spring.ai.dashscope.agent.app-id}")
    private String appId;

    public DashscopeAgentService(DashScopeAgentApi dashscopeAgentApi) {
        this.dashScopeAgent = new DashScopeAgent(
                dashscopeAgentApi,
                DashScopeAgentOptions.builder()
                        .withIncrementalOutput(true)
                        .withHasThoughts(true)
                        .build()
        );
    }

    // stream
    public Flux<String> stream (String query) {
        return dashScopeAgent.stream(
                new Prompt(query, DashScopeAgentOptions.builder().withAppId(appId).build())
        ).map(response -> {
            if (response == null || response.getResult() == null) {
                return "chat response is null";
            }
            AssistantMessage app_output = response.getResult().getOutput();

            return app_output.getText();
        });
    }
}
