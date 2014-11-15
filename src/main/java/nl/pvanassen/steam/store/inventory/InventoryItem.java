package nl.pvanassen.steam.store.inventory;

import nl.pvanassen.steam.store.common.Item;

/**
 * Representation of an item in the inventory
 *
 * @author Paul van Assen
 */
public class InventoryItem extends Item {

    private final String assetId;
    private final int contextId;
    private final String instanceId;
    private final boolean marketable;

    InventoryItem(String assetId, int contextId, String instanceId, int appId, String urlName, boolean marketable) {
        super(appId, urlName);
        this.assetId = assetId;
        this.contextId = contextId;
        this.instanceId = instanceId;
        this.marketable = marketable;
    }

    /**
     * @return the assetId
     */
    public String getAssetId() {
        return assetId;
    }

    /**
     * @return Context id to use
     */
    public int getContextId() {
        return contextId;
    }

    /**
     * Steam instance id of an item
     *
     * @return Instance id
     */
    public String getInstanceId() {
        return instanceId;
    }

    /**
     * @return Is marketable
     */
    public boolean isMarketable() {
        return marketable;
    }

    @Override
    public String toString() {
        return "InventoryItem [assetId=" + assetId + ", contextId=" + contextId + ", instanceId=" + instanceId + ", appId=" + getAppId() + ", urlName=" + getUrlName() + "]";
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((assetId == null) ? 0 : assetId.hashCode());
        result = prime * result + contextId;
        result = prime * result + ((instanceId == null) ? 0 : instanceId.hashCode());
        result = prime * result + (marketable ? 1231 : 1237);
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
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof InventoryItem)) {
            return false;
        }
        InventoryItem other = (InventoryItem) obj;
        if (assetId == null) {
            if (other.assetId != null) {
                return false;
            }
        }
        else if (!assetId.equals(other.assetId)) {
            return false;
        }
        if (contextId != other.contextId) {
            return false;
        }
        if (instanceId == null) {
            if (other.instanceId != null) {
                return false;
            }
        }
        else if (!instanceId.equals(other.instanceId)) {
            return false;
        }
        if (marketable != other.marketable) {
            return false;
        }
        return true;
    }
}
