package com.varun.ratelimit;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class GreetingController {

    @GetMapping("/greeting")
    @RateLimiter(name = "greetingRateLimit", fallbackMethod = "greetingFallBack")
    public ResponseEntity<String> greeting(){
      log.info("greet called");
        return ResponseEntity.ok().body(" Hello World !");
    }

    public ResponseEntity<String> greetingFallBack(RequestNotPermitted ex) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Retry-After", "1");
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).headers(httpHeaders).body("Retry after 1s");
    }

    @GetMapping("/calculateSum")
    @RateLimiter(name = "calculateSumLimiter", fallbackMethod = "calculateSumFallBack")
    public ResponseEntity<String> calculateSum(@RequestParam(name = "arg1") String arg1, @RequestParam(name = "arg2") String arg2){
        log.info("calculate sum called");
        Integer result = Integer.parseInt(arg1) + Integer.parseInt(arg2);
        return ResponseEntity.ok().body(" Result : " + result);
    }

    public ResponseEntity<String> calculateSumFallBack(String arg1, String args2,RequestNotPermitted ex) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Retry-After", "1");
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).headers(httpHeaders).body("Retry after 1s");
    }
}
