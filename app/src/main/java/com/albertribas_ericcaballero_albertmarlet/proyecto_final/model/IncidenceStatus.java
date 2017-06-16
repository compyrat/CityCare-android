package com.albertribas_ericcaballero_albertmarlet.proyecto_final.model;

/**
 * Created by albertribgar on 19/05/2016.
 */
public enum IncidenceStatus {
    TRAMITE, LEIDA, PROCESO, SOLUCIONADA, DENEGADA, INDETERMINADA;

    @Override
    public String toString() {
        String s="";
        switch (this) {
            case TRAMITE: s="En trámite"; break;
            case LEIDA: s="Leída"; break;
            case PROCESO: s="En proceso"; break;
            case SOLUCIONADA: s="Solucionada"; break;
            case DENEGADA: s="Rechazada"; break;
            case INDETERMINADA: s="Indeterminada"; break;
        }
        return s;
    }
}
