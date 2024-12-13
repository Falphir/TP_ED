package lei.estg.utils;

import lei.estg.models.enums.EItemTipo;

public class EItemTipoConverter {
    /**
     * Converts a string representation of an item type to its corresponding EItemTipo enum value.
     * This method takes a string input and returns the corresponding EItemTipo enum value.
     * If the input string does not match any known item type, it returns null.
     *
     * @param tipo the string representation of the item type
     * @return the corresponding EItemTipo enum value, or null if the input does not match any known item type
     */
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

    /**
     * Converts an EItemTipo enum value to its corresponding string representation.
     * This method takes an EItemTipo enum value and returns the corresponding string representation.
     * If the input enum value does not match any known item type, it returns null.
     *
     * @param tipo the EItemTipo enum value
     * @return the corresponding string representation of the item type, or null if the input does not match any known item type
     */
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
