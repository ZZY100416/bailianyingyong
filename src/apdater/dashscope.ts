import {type ChatAdapter, type StreamingAdapterObserver} from '@nlux/react';

const demoProxyServerUrl = "http://localhost:9988/bailian/agent/stream";

// Adapter to send query to the server and receive a stream of chunks as response
export const dashscopeAiAdapter: () => ChatAdapter = () => ({
    streamText: async (
        prompt: string,
        observer: StreamingAdapterObserver,
    ) => {
        const body = {
            query: prompt
        };
        const response = await fetch(demoProxyServerUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(body),
        });

        if (response.status !== 200) {
            observer.error(new Error('Failed to connect to the server'));
            return;
        }

        if (!response.body) {
            return;
        }

        console.log("======body=====", response.body);

        // 获取读取器以处理流数据
        const reader = response.body.getReader();
        const textDecoder = new TextDecoder();  // 创建文本解码器

        let doneStream = false;    // 流是否结束的标志

        // 循环读取流数据，直到流结束
        while (!doneStream) {
            // 读取流数据块
            const { done, value } = await reader.read();
            console.log("======done=====", done);  // 调试日志，显示流是否结束
            
            if (done) {
                doneStream = true;  // 如果流结束，设置标志为true
            } else {
                // 解码二进制数据为文本
                const chunk = textDecoder.decode(value, { stream: true });
                // 按行分割数据，并过滤空行
                const lines = chunk.split('\n').filter(line => line.trim());
                console.log("======lines=====", lines);  // 调试日志，显示处理的数据行

               // 处理每一行数据
                for (const line of lines) {
                    observer.next(line);
                }
            }
            
        }

        observer.complete();
    },
});
