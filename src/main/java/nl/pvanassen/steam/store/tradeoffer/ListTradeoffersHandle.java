package nl.pvanassen.steam.store.tradeoffer;

import com.google.common.collect.ImmutableList;
import nl.pvanassen.steam.http.DefaultHandle;
import nl.pvanassen.steam.store.common.Item;
import nl.pvanassen.steam.store.xpath.XPathHelper;
import org.cyberneko.html.parsers.DOMParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

class ListTradeoffersHandle extends DefaultHandle {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Set<TradeOffer> tradeoffers = new LinkedHashSet<>();
    private final Map<String, Item> imageToItemMapping = new HashMap<>();
    private static final XPathExpression TRADE_OFFERS_XPATH = XPathHelper.getXpathExpression("//DIV[@class='tradeoffer']");
    private static final XPathExpression PARTNERID_XPATH = XPathHelper.getXpathExpression("//DIV[@class='tradeoffer_partner']/DIV");
    private static final XPathExpression QUOTE_XPATH = XPathHelper.getXpathExpression("//DIV[@class='quote']");

    ListTradeoffersHandle() {
        super();
    }

    Map<String, Item> getImageToItemMapping() {
        return imageToItemMapping;
    }

    List<TradeOffer> getTradeoffers() {
        return ImmutableList.copyOf(tradeoffers);
    }

    /**
     * {@inheritDoc}
     *
     * @see nl.pvanassen.steam.http.DefaultHandle#handle(java.io.InputStream)
     */
    @Override
    public void handle(InputStream stream) throws IOException {
        DOMParser parser = new DOMParser();
        try {
            parser.parse(new InputSource(stream));
            Document document = parser.getDocument();
            NodeList tradeoffersNode = (NodeList) TRADE_OFFERS_XPATH.evaluate(document, XPathConstants.NODESET);
            for (int i = 0; i != tradeoffersNode.getLength(); i++) {
                Node tradeofferNode = tradeoffersNode.item(i);
                Node partnerNode = (Node) PARTNERID_XPATH.evaluate(tradeofferNode, XPathConstants.NODE);
                String partnerId = partnerNode.getAttributes().getNamedItem("data-miniprofile").getNodeValue();
                String offerId = tradeofferNode.getAttributes().getNamedItem("id").getTextContent().replace("tradeofferid_", "");
                String quote = ((Node) QUOTE_XPATH.evaluate(tradeofferNode, XPathConstants.NODE)).getFirstChild().getTextContent().replace('\u00A0', ' ').trim();
                tradeoffers.add(new TradeOffer(partnerId, offerId, quote));
            }
        }
        catch (SAXException | XPathExpressionException e) {
            logger.error("Error getting items", e);
        }
    }
}
