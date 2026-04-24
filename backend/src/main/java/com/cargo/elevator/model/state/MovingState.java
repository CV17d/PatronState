package com.cargo.elevator.model.state;

import com.cargo.elevator.model.entity.CargoElevator;

/**
 * State when the elevator is in motion.
 */
public class MovingState implements ElevatorState {

    @Override
    public void requestElevator(CargoElevator elevator, int floor) {
        System.out.println("[ERROR] El ascensor está en movimiento.");
    }

    @Override
    public void loadCargo(CargoElevator elevator, double weight) {
        System.out.println("[ERROR] No se puede cargar mercancía mientras el ascensor se mueve.");
    }

    @Override
    public void closeDoors(CargoElevator elevator) {
        System.out.println("[ERROR] Las puertas ya se encuentran cerradas y aseguradas.");
    }

    @Override
    public void move(CargoElevator elevator) {
        System.out.println("[INFO] El ascensor se desplaza hacia el piso " + elevator.getTargetFloor());
        // Simulate arrival
        reachFloor(elevator);
    }

    @Override
    public void reachFloor(CargoElevator elevator) {
        elevator.setCurrentFloor(elevator.getTargetFloor());
        System.out.println("[INFO] Destino alcanzado: Piso " + elevator.getCurrentFloor());
        elevator.setState(new FloorReachedState());
    }

    @Override
    public void unloadCargo(CargoElevator elevator) {
        System.out.println("[ERROR] No se puede descargar en movimiento.");
    }

    @Override
    public String getStateName() {
        return "En movimiento";
    }
}
