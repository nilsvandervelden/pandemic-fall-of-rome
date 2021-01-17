package com.groep6.pfor.models.cards;

public enum CardType {

    CITY("Stadskaart"),
    EVENT("Eventkaart"),
    ROLE("Karakterkaart"),
    REVOLT("Opstandskaart"),
    INVASION("Invasiekaart");

    private final String cartTypeName;

    CardType(String cartTypeName) {
        this.cartTypeName = cartTypeName;
    }

    @Override
    public String toString() {
        return cartTypeName;
    }
}
