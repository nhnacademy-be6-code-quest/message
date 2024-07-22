package com.nhnacademy.message.client;

import com.nhnacademy.message.dto.DoorayMessageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "dooray", url = "https://hook.dooray.com/services/3204376758577275363/3853526276004099466/ge74CLCLQGOBhr9Bc3TMzg")
public interface DoorayClient {
    @PostMapping
    void sendMessage(@RequestBody DoorayMessageRequest message);
}
