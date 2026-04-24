package com.cargo.elevator.model.entity;

import com.cargo.elevator.model.state.ElevatorState;
import com.cargo.elevator.model.state.IdleState;

/**
 * Context class in the State Pattern.
 * Maintains the current state and data of the cargo elevator.
 */
public class CargoElevator {
    public static final double MAX_WEIGHT_KG = 1000.0;
    
    private ElevatorState currentState;
    private double currentWeight;
    private int currentFloor;
    private int targetFloor;

    public CargoElevator() {
        this.currentState = new IdleState();
        this.currentWeight = 0.0;
        this.currentFloor = 0; // Ground floor
        this.targetFloor = 0;
    }

    // Delegation of behavior to the current state
    
    public void requestElevator(int floor) {
        currentState.requestElevator(this, floor);
    }

    public void loadCargo(double weight) {
        currentState.loadCargo(this, weight);
    }

    public void closeDoors() {
        currentState.closeDoors(this);
    }

    public void move() {
        currentState.move(this);
    }

    public void reachFloor() {
        currentState.reachFloor(this);
    }

    public void unloadCargo() {
        currentState.unloadCargo(this);
    }

    // Getters and Setters

    public ElevatorState getCurrentState() {
        return currentState;
    }

    public void setState(ElevatorState state) {
        this.currentState = state;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getTargetFloor() {
        return targetFloor;
    }

    public void setTargetFloor(int targetFloor) {
        this.targetFloor = targetFloor;
    }
}
