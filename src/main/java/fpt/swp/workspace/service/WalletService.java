package fpt.swp.workspace.service;

import fpt.swp.workspace.models.Wallet;
import fpt.swp.workspace.repository.UserRepo;
import fpt.swp.workspace.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepo userRepository;

    public float getWalletBalance(String userId) {
        Optional<Wallet> walletOptional = walletRepository.findByUserId(userId);
        if (walletOptional.isPresent()) {
            return walletOptional.get().getAmount();
        } else {
            return 0;
        }
    }

    public void updateWalletBalance(String userId, float amount) {
        Optional<Wallet> walletOpt = walletRepository.findByUserId(userId);
        if (walletOpt.isPresent()) {
            Wallet wallet = walletOpt.get();
            float currentBalance = wallet.getAmount();
            wallet.setAmount(currentBalance + amount);
            walletRepository.save(wallet);
        } else {
            throw new RuntimeException("Wallet not found for user: " + userId);
        }
    }
}
