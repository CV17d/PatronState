package com.cargo.elevator.controller;

import com.cargo.elevator.service.ElevatorService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/elevator")
@CrossOrigin(origins = "*")
public class ElevatorController {
    
    private final ElevatorService elevatorService;

    public ElevatorController(ElevatorService elevatorService) {
        this.elevatorService = elevatorService;
    }

    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        return Map.of(
            "state", elevatorService.getStateName(),
            "floor", elevatorService.getCurrentFloor(),
            "weight", elevatorService.getCurrentWeight(),
            "targetFloor", elevatorService.getTargetFloor(),
            "maxWeight", 1000.0
        );
    }

    @PostMapping("/request")
    public void requestElevator(@RequestParam int floor) {
        elevatorService.callToFloor(floor);
    }

    @PostMapping("/load")
    public void loadCargo(@RequestParam double weight) {
        elevatorService.loadGoods(weight);
    }

    @PostMapping("/close-doors")
    public void closeDoors() {
        elevatorService.processDeparture();
    }

    @PostMapping("/move")
    public void move() {
        elevatorService.executeMovement();
    }

    @PostMapping("/arrive")
    public void arrive() {
        elevatorService.handleArrival();
    }

    @PostMapping("/unload")
    public void unload() {
        elevatorService.unloadGoods();
    }
}
