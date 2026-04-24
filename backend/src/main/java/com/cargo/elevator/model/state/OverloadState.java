package com.cargo.elevator.model.state;

import com.cargo.elevator.model.entity.CargoElevator;

/**
 * Critical state for safety. Triggered when weight exceeds limit.
 */
public class OverloadState implements ElevatorState {

    @Override
    public void requestElevator(CargoElevator elevator, int floor) {
        System.out.println("[ALERTA] SISTEMA BLOQUEADO POR SOBRECARGA. Retire peso para continuar.");
    }

    @Override
    public void loadCargo(CargoElevator elevator, double weight) {
        System.out.println("[ADVERTENCIA] El sistema ya está sobrecargado. Peso actual: " + elevator.getCurrentWeight() + " kg.");
    }

    @Override
    public void closeDoors(CargoElevator elevator) {
        System.err.println("[CRÍTICO] Error de seguridad: No se pueden cerrar las puertas con sobrecarga.");
    }

    @Override
    public void move(CargoElevator elevator) {
        System.err.println("[BLOQUEO] MOVIMIENTO DENEGADO. El motor ha sido deshabilitado preventivamente por exceso de peso (" + elevator.getCurrentWeight() + " kg).");
    }

    @Override
    public void reachFloor(CargoElevator elevator) {
        System.out.println("[ERROR] Operación no permitida en estado de sobrecarga.");
    }

    @Override
    public void unloadCargo(CargoElevator elevator) {
        System.out.println("[LOG] Liberando carga... Peso retirado.");
        elevator.setCurrentWeight(0);
        System.out.println("[INFO] Peso normalizado. Reiniciando validación de seguridad.");
        elevator.setState(new WeightValidationState());
    }

    @Override
    public String getStateName() {
        return "Sobrecarga - Sistema Bloqueado";
    }
}
