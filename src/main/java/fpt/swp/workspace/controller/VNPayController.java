package fpt.swp.WorkSpace.controller;

import fpt.swp.WorkSpace.service.VNPAYService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/vnpay")
public class VNPayController {
    @Autowired
    private VNPAYService vnPayService;

    @PostMapping("/createOrderTopUp")
    public String createOrderTopUp(@RequestParam int amount, @RequestParam String userId, HttpServletRequest request) {
        return vnPayService.createRecharge(request, amount, userId);
    }

    @GetMapping("/return/orderReturn")
    public ResponseEntity<Map<String, Object>> orderReturn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return vnPayService.returnRecharge(request, response);
    }
}
