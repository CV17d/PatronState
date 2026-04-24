package com.cargo.elevator.model.state;

import com.cargo.elevator.model.entity.CargoElevator;

/**
 * State when the elevator has reached its destination.
 */
public class FloorReachedState implements ElevatorState {

    @Override
    public void requestElevator(CargoElevator elevator, int floor) {
        System.out.println("[INFO] Destino actualizado al piso " + floor);
        elevator.setTargetFloor(floor);
        // Mantenemos el estado actual (puertas abiertas)
    }

    @Override
    public void loadCargo(CargoElevator elevator, double weight) {
        System.out.println("[INFO] Apertura de puertas. Cargando material...");
        elevator.setCurrentWeight(weight);
    }

    @Override
    public void closeDoors(CargoElevator elevator) {
        System.out.println("[LOG] Validando peso para cierre de puertas...");
        if (elevator.getCurrentWeight() > CargoElevator.MAX_WEIGHT_KG) {
            System.err.println("[ALERTA] SOBRECARGA DETECTADA: " + elevator.getCurrentWeight() + " kg excede el límite de " + CargoElevator.MAX_WEIGHT_KG + " kg.");
            elevator.setState(new OverloadState());
        } else {
            System.out.println("[INFO] Peso aceptado. Puertas cerradas. Iniciando tránsito.");
            elevator.setState(new MovingState());
        }
    }

    @Override
    public void move(CargoElevator elevator) {
        System.out.println("[ERROR] Debe seleccionar un destino antes de mover el ascensor.");
    }

    @Override
    public void reachFloor(CargoElevator elevator) {
        System.out.println("[INFO] El ascensor ya se encuentra en el piso " + elevator.getCurrentFloor());
    }

    @Override
    public void unloadCargo(CargoElevator elevator) {
        System.out.println("[INFO] Puertas abiertas. Descarga de " + elevator.getCurrentWeight() + " kg completada.");
        elevator.setCurrentWeight(0);
        elevator.setState(new IdleState());
    }

    @Override
    public String getStateName() {
        return "Piso alcanzado";
    }
}
