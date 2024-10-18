package fpt.swp.WorkSpace.controller;

import fpt.swp.WorkSpace.models.Wallet;
import fpt.swp.WorkSpace.response.APIResponse;
import fpt.swp.WorkSpace.response.TopUpRequest;
import fpt.swp.WorkSpace.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
