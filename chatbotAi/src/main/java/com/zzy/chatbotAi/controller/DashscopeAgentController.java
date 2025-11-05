package com.zzy.chatbotAi.controller;

import com.zzy.chatbotAi.service.DashscopeAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/bailian/agent")
public class DashscopeAgentController {

    @Autowired
    DashscopeAgentService dashscopeAgentService;

    @PostMapping("/stream")
    public Flux<String> stream(@RequestBody Map<String, Object> params) {
        String query = params.get("query").toString();
        return dashscopeAgentService.stream(query);
    }
}
