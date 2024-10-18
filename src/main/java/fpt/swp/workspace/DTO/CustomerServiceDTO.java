package fpt.swp.workspace.DTO;

import lombok.Data;

import java.util.Map;

@Data
public class CustomerServiceDTO {
    private Map<String, Integer> serviceItems;
}
