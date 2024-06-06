package com.westwebsystems.budgets.controller;

import com.westwebsystems.budgets.helputils.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import java.util.List;
import com.westwebsystems.budgets.config.UserRepository;
import com .westwebsystems.budgets.model.EndUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class EndUserController {
    private static final Logger logger = LoggerFactory.getLogger(EndUserController.class);

    private final UserRepository userRepository;

    @Autowired
    public EndUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/enduser")
    public String getEndUsers(Model model, HttpServletRequest request) {
        List<EndUser> endUsers = userRepository.findAll();
        logger.info("Number of fetched end users: " + endUsers.size());
        model.addAttribute("endusers", endUsers);
        String clientIp = IpUtil.getClientIp(request);
        logger.info("---> Loading enduser page, client IP: {}", clientIp);
        return "enduser";
    }
}
