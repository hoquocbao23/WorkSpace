package fpt.swp.workspace.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomStatusResponse {
    private String roomId;
    private String roomStatus;
}

