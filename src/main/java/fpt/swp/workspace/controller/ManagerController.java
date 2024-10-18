package fpt.swp.WorkSpace.controller;


import fpt.swp.WorkSpace.models.Manager;
import fpt.swp.WorkSpace.response.APIResponse;
import fpt.swp.WorkSpace.response.ManagerRequest;
import fpt.swp.WorkSpace.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/managers")
public class ManagerController {
    @Autowired
    private ManagerService managerService;

    @PostMapping()
    public ResponseEntity<APIResponse<Manager>> createManager(@RequestBody ManagerRequest request) {
        try {
            Manager createManager = managerService.createManager(request);
            APIResponse<Manager> response = new APIResponse<>("Manager create successfully", createManager);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            APIResponse<Manager> response = new APIResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping
    public Page<Manager> getAllManagers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return managerService.getAllManagers(page, size);
    }

    @GetMapping("/{id}")
    public Manager getManagerById(@PathVariable String id) {
        return managerService.getManagerById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<Manager>> updateManager(@PathVariable String id,@RequestBody ManagerRequest request) {
        try {
            Manager updateManager = managerService.updateManager(id, request);
            APIResponse<Manager> response = new APIResponse<>("Manager updated successfully", updateManager);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            APIResponse<Manager> response = new APIResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteManager(@PathVariable String id) {
        try {
            managerService.deleteManager(id);
            APIResponse<Void> response = new APIResponse<>("Manager deleted successfully", null);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            APIResponse<Void> response = new APIResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
