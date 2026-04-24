package com.cargo.elevator.service;

import com.cargo.elevator.model.entity.CargoElevator;
import org.springframework.stereotype.Service;

@Service
public class ElevatorService {
    
    private final CargoElevator elevator;

    public ElevatorService() {
        this.elevator = new CargoElevator();
    }

    public void callToFloor(int floor) {
        elevator.requestElevator(floor);
    }

    public void loadGoods(double weight) {
        elevator.loadCargo(weight);
    }

    public void processDeparture() {
        elevator.closeDoors();
    }

    public void executeMovement() {
        elevator.move();
    }

    public void handleArrival() {
        elevator.reachFloor();
    }

    public void unloadGoods() {
        elevator.unloadCargo();
    }

    public String getStateName() {
        return elevator.getCurrentState().getStateName();
    }

    public int getCurrentFloor() {
        return elevator.getCurrentFloor();
    }

    public double getCurrentWeight() {
        return elevator.getCurrentWeight();
    }
    
    public int getTargetFloor() {
        return elevator.getTargetFloor();
    }
}
