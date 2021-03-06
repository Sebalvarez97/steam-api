package nl.pvanassen.steam.store.listing;

import nl.pvanassen.steam.error.SteamException;

/**
 * Exception while trying to sell the item
 * 
 * @author Paul van Assen
 */
public class SellException extends SteamException {
    private final boolean itemNotInInventory;

    SellException(String error) {
        super(error);
        itemNotInInventory = ((error != null) && error.contains("is no longer in your inventory"));
    }

    SellException(String error, Throwable cause) {
        super(error, cause);
        itemNotInInventory = ((error != null) && error.contains("is no longer in your inventory"));
    }

    /**
     * @return True if the item is missing from the inventory
     */
    public boolean isItemNotInInventory() {
        return itemNotInInventory;
    }
}
