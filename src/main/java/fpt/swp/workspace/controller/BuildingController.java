package fpt.swp.WorkSpace.controller;

import fpt.swp.WorkSpace.models.Building;
import fpt.swp.WorkSpace.response.APIResponse;
import fpt.swp.WorkSpace.response.BuildingRequest;
import fpt.swp.WorkSpace.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/buildings")
public class BuildingController {
    @Autowired
    private BuildingService buildingService;

    @PostMapping()
    public ResponseEntity<Building> createBuilding(@RequestBody BuildingRequest request) {
        Building createdBuilding = buildingService.createBuilding(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBuilding);
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<Building>>> getAllBuildings() {
        try {
            List<Building> buildings = buildingService.findAllBuildings();
            APIResponse<List<Building>> response = new APIResponse<>("Successfully", buildings);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            APIResponse<List<Building>> response = new APIResponse<>("Not Found", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{wsId}")
    public ResponseEntity<APIResponse<Building>> getBuildingById(@PathVariable String wsId) {
        try {
            Building building = buildingService.findBuildingById(wsId);
            APIResponse<Building> response = new APIResponse<>("Successfully", building);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            APIResponse<Building> response = new APIResponse<>("Not Found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/{wsId}")
    public ResponseEntity<APIResponse<Building>> updateBuilding(@PathVariable String wsId, @RequestBody BuildingRequest request) {
        try {
            Building updatedBuilding = buildingService.updateBuilding(wsId, request);
            APIResponse<Building> response = new APIResponse<>("Building updated successfully", updatedBuilding);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            APIResponse<Building> response = new APIResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{wsId}")
    public ResponseEntity<APIResponse<Void>> deleteBuilding(@PathVariable String wsId) {
        try {
            buildingService.deleteBuilding(wsId);
            APIResponse<Void> response = new APIResponse<>("Building deleted successfully", null);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            APIResponse<Void> response = new APIResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}