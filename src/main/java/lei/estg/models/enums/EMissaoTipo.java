package lei.estg.models.enums;

public enum EMissaoTipo {
    RESGATE("Resgatar Reféns"),
    RECUPERAR("Recuperar Bens Valiosos"),
    NEUTRALIZAR("Neutralizar Armas de Destruição em Massa");

    private final String description;

    EMissaoTipo(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

