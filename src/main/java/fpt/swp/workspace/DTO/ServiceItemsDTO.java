package fpt.swp.WorkSpace.DTO;


import lombok.Data;


@Data
public class ServiceItemsDTO {
    private int serviceId;
    private String serviceName;
    private String[] serviceImg;
    private float price;
    private int quantity;
    private String serviceType;
}
