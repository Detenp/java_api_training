package fr.lernejo.navy_battle.models;

public enum Boat {
    PORTE_AVION,CROISEUR,CONTRE_TORPILLEUR,TORPILLEUR;

    public int size() {
        return switch (this) {
            case PORTE_AVION -> 5;
            case CROISEUR -> 4;
            case CONTRE_TORPILLEUR -> 3;
            case TORPILLEUR -> 2;
        };
    }

    public int getValue() {
        return this.ordinal() + 1;
    }
}
