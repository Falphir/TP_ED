package lei.estg.models.enums;

public enum EAlvoTipo {
    RESGATE("Resgatar Reféns"),
    RECUPERAR("Recuperar Bens Valiosos"),
    NEUTRALIZAR("Neutralizar Armas de Destruição em Massa");

    private final String description;

    EAlvoTipo(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

