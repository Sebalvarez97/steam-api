/**
 *
 */
package nl.pvanassen.steam.store.tradeoffer;

import com.google.common.base.Optional;
import nl.pvanassen.steam.store.common.InventoryItem;

import java.util.List;

/**
 * @author Paul van Assen
 */
public interface TradeOfferService {
    /**
     * Call to accept a trade offer
     * 
     * @param tradeOffer Trade offer to accept
     */
    void acceptTradeOffer(TradeOffer tradeOffer);

    /**
     * @return A list of trade offers
     */
    List<TradeOffer> getTradeOffers();

    /**
     * Make a trade offer of items to a user
     * 
     * @param partner Trading partner ID
     * @param me What do I offer
     * @param them What do they offer
     * @param message A message for the trade
     * @return The trade offer id
     */
    int makeTradeOffer(long partner, List<InventoryItem> me, List<InventoryItem> them, Optional<String> message);
}
