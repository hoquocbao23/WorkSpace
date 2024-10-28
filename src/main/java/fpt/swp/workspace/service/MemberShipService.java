package fpt.swp.workspace.service;

import fpt.swp.workspace.models.UserNumberShip;
import fpt.swp.workspace.repository.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberShipService implements IMemberShipService {
    @Autowired
    private MembershipRepository membershipRepository;

    @Override
    public List<UserNumberShip> getAllMemberShip() {
        return membershipRepository.findAll();
    }

    @Override
    public UserNumberShip getMemberShipById(String id) {
        return membershipRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy gói ưu đãi"));
    }
}
