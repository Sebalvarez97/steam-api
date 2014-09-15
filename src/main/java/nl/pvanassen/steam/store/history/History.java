package nl.pvanassen.steam.store.history;

import java.util.Date;

import nl.pvanassen.steam.store.common.Item;

/**
 * Items in the market
 * 
 * @author Paul van Assen
 */
public class History extends Item {
    private final String steamId;
    private final int contextId;
    private final Date listed;
    private final Date acted;
    private final int price;
    private final String buyer;
    private final HistoryStatus status;

    History(String steamId, int appId, int contextId, String urlName, Date listed, Date acted, int price,
            String buyer, HistoryStatus status) {
        super(appId, urlName);
        this.steamId = steamId;
        this.contextId = contextId;
        this.listed = listed;
        this.acted = acted;
        this.price = price;
        this.buyer = buyer;
        this.status = status;
    }

    /**
     * @return the steamId
     */
    public String getSteamId() {
        return steamId;
    }

    /**
     * @return the contextId
     */
    public int getContextId() {
        return contextId;
    }

    /**
     * @return the listed
     */
    public Date getListed() {
        return listed;
    }

    /**
     * @return the acted
     */
    public Date getActed() {
        return acted;
    }

    /**
     * @return the price
     */
    public int getPrice() {
        return price;
    }

    /**
     * @return the buyer
     */
    public String getBuyer() {
        return buyer;
    }

    /**
     * @return Status of the item
     */
    public HistoryStatus getStatus() {
        return status;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((steamId == null) ? 0 : steamId.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof History)) {
            return false;
        }
        History other = (History) obj;
        if (steamId == null) {
            if (other.steamId != null) {
                return false;
            }
        }
        else if (!steamId.equals(other.steamId)) {
            return false;
        }
        return true;
    }

}