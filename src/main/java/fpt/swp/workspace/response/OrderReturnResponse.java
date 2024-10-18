package fpt.swp.WorkSpace.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderReturnResponse {
    private String message;
    private float amountTopUp;
    private float currentWalletBalance;
}
