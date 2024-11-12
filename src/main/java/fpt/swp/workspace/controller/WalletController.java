package fpt.swp.workspace.controller;

import fpt.swp.workspace.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    @Autowired
    private WalletService walletService;

//    @PostMapping("/top-up")
//    public ResponseEntity<APIResponse<Wallet>> topUpWallet(@RequestBody TopUpRequest request) {
//        Wallet updatedWallet = walletService.topUpWallet(request);
//        APIResponse<Wallet> response = new APIResponse<>("Wallet topped up successfully", updatedWallet);
//        return ResponseEntity.ok(response);
//    }
}
