package com.cargo.elevator.model.state;

import com.cargo.elevator.model.entity.CargoElevator;

/**
 * Interface representing the state of the high-security cargo elevator.
 * Defines the contract for all concrete state implementations.
 */
public interface ElevatorState {
    void requestElevator(CargoElevator elevator, int floor);
    void loadCargo(CargoElevator elevator, double weight);
    void closeDoors(CargoElevator elevator);
    void move(CargoElevator elevator);
    void reachFloor(CargoElevator elevator);
    void unloadCargo(CargoElevator elevator);
    
    /**
     * Helper method to get the state name for logging purposes.
     */
    String getStateName();
}
