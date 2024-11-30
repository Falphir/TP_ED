package lei.estg.utils;

import lei.estg.models.enums.EItemTipo;

public class EItemTipoConverter {
    public EItemTipo convertStringToEItemTipo(String tipo) {
        switch (tipo) {
            case "colete":
                return EItemTipo.COLETE;
            case "kit de vida":
                return EItemTipo.KIT;
            default:
                return null;
        }
    }

    public String convertEItemTipoToString(EItemTipo tipo) {
        switch (tipo) {
            case COLETE:
                return "colete";
            case KIT:
                return "kit de vida";
            default:
                return null;
        }
    }
    
}
