package fpt.swp.workspace.service;

import fpt.swp.workspace.models.UserNumberShip;

import java.util.List;

public interface IMemberShipService {
    List<UserNumberShip> getAllMemberShip();


    UserNumberShip getMemberShipById(String id);
}
