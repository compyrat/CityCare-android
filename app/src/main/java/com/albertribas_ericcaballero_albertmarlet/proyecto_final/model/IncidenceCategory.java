package com.albertribas_ericcaballero_albertmarlet.proyecto_final.model;

/**
 * Created by albertribgar on 19/05/2016.
 */
public enum IncidenceCategory {
    DETERIORO, MOBILIARIO, ALUMBRADO, ALCANTARILLADO, LIMPIEZA, SEMAFOROS;

    @Override
    public String toString() {
        String s="";
        switch (this) {
            case DETERIORO: s="Deterioro de la Vía pública"; break;
            case MOBILIARIO: s="Mobiliario urbano"; break;
            case ALUMBRADO: s="Farolas y alumbrado"; break;
            case ALCANTARILLADO: s="Alcantarillado"; break;
            case LIMPIEZA: s="Limpieza"; break;
            case SEMAFOROS: s="Semáforos y señales de tráfico"; break;
        }
        return s;
    }
}
