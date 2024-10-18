package fpt.swp.workspace.service;

import fpt.swp.workspace.models.Transaction;
import fpt.swp.workspace.repository.TransactionRepository;
import fpt.swp.workspace.response.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public List<TransactionResponse> getAllTransactionsByUserId(String userId) {
        List<Transaction> transactions = transactionRepository.findAllByUserId(userId);
        return transactions.stream()
                .map(tx -> new TransactionResponse(tx.getTransactionId(), tx.getAmount(), tx.getStatus(), tx.getType(), tx.getPayment().getPaymentId(), tx.getTransaction_time()))
                .collect(Collectors.toList());
    }
}
