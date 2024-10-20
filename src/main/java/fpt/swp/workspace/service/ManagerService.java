package fpt.swp.workspace.service;

import fpt.swp.workspace.models.Manager;
import fpt.swp.workspace.models.UserStatus;
import fpt.swp.workspace.repository.BuildingRepository;
import fpt.swp.workspace.repository.ManagerRepository;
import fpt.swp.workspace.repository.UserRepo;
import fpt.swp.workspace.response.ManagerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ManagerService {
    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private BuildingRepository buildingRepository;

//    public Manager createManager(ManagerRequest request) {
//        boolean buildingExists = buildingRepository.existsById(request.getBuildingId());
//        if (!buildingExists) {
//            throw new RuntimeException("Building not found");
//        }
//
//        boolean userExists = userRepository.existsById(request.getUserId());
//        if (!userExists) {
//            throw new RuntimeException("User not found");
//        }
//        Manager manager = new Manager();
//        //manager.setManagerId(request.getUserId());
//        manager.setEmail(request.getEmail());
//        manager.setFullName(request.getFullName());
//        manager.setPhoneNumber(request.getPhoneNumber());
//        manager.setDateOfBirth(request.getDateOfBirth());
//        manager.setRoleName("MANAGER");
//        manager.setCreateAt(LocalDateTime.now());
//        manager.setUserId(request.getUserId());
//        manager.setBuildingId(request.getBuildingId());
//        managerRepository.save(manager);
//        return manager;
//    }

    public Page<Manager> getAllManagers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return managerRepository.findAll(pageable);
    }

    public Manager getManagerById(String managerId) {
        return managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));
    }

    public Manager updateManager(String managerId, ManagerRequest request) {
        Manager existedManager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        if(request.getEmail() != null){
            existedManager.setEmail(request.getEmail());
        }
        if(request.getFullName() != null){
            existedManager.setFullName(request.getFullName());
        }
        if(request.getPhoneNumber() != null){
            existedManager.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getDateOfBirth() != null) {
            existedManager.setDateOfBirth(request.getDateOfBirth());
        }
        return managerRepository.save(existedManager);
    }

    public void updateManageStatus(String managerId) {
        Manager manager = managerRepository.findById(managerId).orElseThrow(() -> new RuntimeException("Manager không tồn tại"));
        manager.setStatus(UserStatus.DISABLED);
        managerRepository.save(manager);
    }
}
