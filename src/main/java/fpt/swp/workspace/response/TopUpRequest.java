package fpt.swp.WorkSpace.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopUpRequest {
    private String walletId;
    private int amount;
}