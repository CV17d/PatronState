package com.cargo.elevator.model.state;

import com.cargo.elevator.model.entity.CargoElevator;

/**
 * Initial state of the elevator. Waiting for a request.
 */
public class IdleState implements ElevatorState {

    @Override
    public void requestElevator(CargoElevator elevator, int floor) {
        System.out.println("[INFO] Ascensor solicitado en el piso: " + floor);
        elevator.setTargetFloor(floor);
        // If it's already on the floor, wait for cargo, otherwise move (simplified for this exercise)
        if (elevator.getCurrentFloor() == floor) {
            System.out.println("[INFO] El ascensor ya se encuentra en el piso " + floor + ". Esperando carga.");
            elevator.setState(new WeightValidationState());
        } else {
            System.out.println("[INFO] Moviendo ascensor vacío al piso " + floor);
            elevator.setState(new MovingState());
        }
    }

    @Override
    public void loadCargo(CargoElevator elevator, double weight) {
        System.out.println("[ERROR] No se puede cargar mercancía si el ascensor no ha sido solicitado.");
    }

    @Override
    public void closeDoors(CargoElevator elevator) {
        System.out.println("[ERROR] Las puertas ya están cerradas en estado de espera.");
    }

    @Override
    public void move(CargoElevator elevator) {
        System.out.println("[ERROR] El ascensor está en reposo. Solicite un piso primero.");
    }

    @Override
    public void reachFloor(CargoElevator elevator) {
        System.out.println("[ERROR] El ascensor ya está detenido.");
    }

    @Override
    public void unloadCargo(CargoElevator elevator) {
        System.out.println("[ERROR] No hay carga para descargar.");
    }

    @Override
    public String getStateName() {
        return "En reposo";
    }
}
