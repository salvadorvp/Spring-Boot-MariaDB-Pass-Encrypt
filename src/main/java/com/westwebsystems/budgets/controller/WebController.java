package com.westwebsystems.budgets.controller;

import com.westwebsystems.budgets.helputils.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class WebController {

    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

    @Value("${app.name:Budgets Application}")
    private String appName;

    @Value("${app.description:Application for managing financial budgets.}")
    private String appDescription;

    @GetMapping({"/", "/homepage"})
    public String index(Model model, HttpServletRequest request) {
        model.addAttribute("appName", appName);
        model.addAttribute("appDescription", appDescription);
        String clientIp = IpUtil.getClientIp(request);
        logger.info("---> Loading root page, client IP: {}", clientIp);
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpServletRequest request) {
        String clientIp = IpUtil.getClientIp(request);
        logger.info("---> Loading dashboard page, client IP: {}", clientIp);
        return "dashboard";
    }

    @GetMapping("/logout-success")
    public String logout() {
        return "logout";
    }

    @GetMapping("/server-error")
    public String error() {
        return "error";
    }

}
