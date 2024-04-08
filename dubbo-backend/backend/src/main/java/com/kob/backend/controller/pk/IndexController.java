package com.kob.backend.controller.pk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zeroac
 */
@Controller
@RequestMapping("/pk")
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    /**
     * Handles the request for "/pk/index" and returns "pk/index".
     *
     * @return a string representing the view "pk/index"
     */
    @RequestMapping(value = "/index")
    public String index() {
        logger.debug("This is a debug message");
        logger.info("This is an info message");
        logger.warn("This is a warning message");
        logger.error("This is an error message");
        return "pk/index";
    }
}
