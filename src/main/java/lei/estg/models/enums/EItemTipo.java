package lei.estg.models.enums;


public enum EItemTipo {
    COLETE("Colete"),
    KIT("Kit de vida");

    private final String description;

    EItemTipo(String description) {
        this.description = description;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }
}
