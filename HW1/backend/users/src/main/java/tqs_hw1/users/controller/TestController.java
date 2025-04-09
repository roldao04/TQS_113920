package tqs_hw1.users.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "http://localhost:8090", allowedHeaders = "*")
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        logger.info("Test endpoint called");
        try {
            return ResponseEntity.ok("Test endpoint working!");
        } catch (Exception e) {
            logger.error("Error in test endpoint: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 