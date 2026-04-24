package com.cargo.elevator.model.state;

import com.cargo.elevator.model.entity.CargoElevator;

/**
 * State where weight validation occurs before closing doors.
 */
public class WeightValidationState implements ElevatorState {

    @Override
    public void requestElevator(CargoElevator elevator, int floor) {
        System.out.println("[INFO] Destino actualizado al piso " + floor);
        elevator.setTargetFloor(floor);
    }

    @Override
    public void loadCargo(CargoElevator elevator, double weight) {
        elevator.setCurrentWeight(weight);
        System.out.println("[LOG] Carga registrada: " + weight + " kg.");
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
        System.out.println("[ERROR] Debe cerrar las puertas antes de mover el ascensor.");
    }

    @Override
    public void reachFloor(CargoElevator elevator) {
        System.out.println("[ERROR] El ascensor aún no ha iniciado el movimiento.");
    }

    @Override
    public void unloadCargo(CargoElevator elevator) {
        System.out.println("[INFO] Descargando mercancía en el piso actual.");
        elevator.setCurrentWeight(0);
    }

    @Override
    public String getStateName() {
        return "Validando peso";
    }
}
