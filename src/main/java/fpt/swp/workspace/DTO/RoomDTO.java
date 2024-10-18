package fpt.swp.WorkSpace.DTO;

import com.fasterxml.jackson.annotation.*;

import lombok.Data;



@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class RoomDTO {
    private String roomId;
    private String roomName;
    private float price;
    private String[] roomImg;
    private String description;
    private String building;
    private String roomType;
    private String roomStatus;

    @JsonGetter("price")
    public Float getPrice() {
        return price == 0 ? null : price; // Return null if price is 0
    }
}
