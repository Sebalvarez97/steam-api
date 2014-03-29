package nl.pvanassen.steam.store;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import nl.pvanassen.steam.http.DefaultHandle;

import org.codehaus.jackson.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ListingHandle extends DefaultHandle {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ObjectMapper objectMapper;
    private final Queue<Listing> listingQueue;

    ListingHandle( ObjectMapper objectMapper, Queue<Listing> listingQueue ) {
        this.objectMapper = objectMapper;
        this.listingQueue = listingQueue;
    }

    @Override
    public void handle( InputStream stream ) throws IOException {
        try {
        Map<String, IncompleteListing> incompleteListingsMap = null;
        Map<String, Asset> assetsMap = null;
        JsonParser jParser = objectMapper.getJsonFactory().createJsonParser( stream );
        String fieldname = jParser.getCurrentName();
        jParser.nextToken();
        while ( jParser.nextToken() != JsonToken.END_OBJECT ) {
            fieldname = jParser.getCurrentName();
            if ( fieldname == null ) {
                break;
            }
            jParser.nextToken();
            if ( "listinginfo".equals( fieldname ) ) {
                incompleteListingsMap = getListings( jParser );
            }
            else if ( "assets".equals( fieldname ) ) {
                assetsMap = getAssets( jParser );
            }
            else if ( incompleteListingsMap != null && assetsMap != null ) {
                break;
            }
            else if ( jParser.getCurrentToken() == JsonToken.START_ARRAY || jParser.getCurrentToken() == JsonToken.START_OBJECT ) {
                jParser.skipChildren();
            }
        }
        for ( Map.Entry<String, IncompleteListing> entry : incompleteListingsMap.entrySet() ) {
            IncompleteListing incompleteListing = entry.getValue();
            Asset asset = assetsMap.get( entry.getKey() );
            if ( asset == null ) {
                continue;
            }
            Listing listing = incompleteListing.getListing( asset );
            if ( listing.getFee() == 0 ) {
                continue;
            }
            listingQueue.offer( listing );
        }
        }
        catch (RuntimeException e) {
            logger.error("Error getting listing", e);
        }
    }

    private Map<String, Asset> getAssets( JsonParser jParser ) throws JsonParseException, IOException {
        Map<String, Asset> map = new HashMap<>();
        String fieldname;
        while ( jParser.nextToken() != JsonToken.END_OBJECT ) {
            jParser.nextToken();
            while ( jParser.nextToken() != JsonToken.END_OBJECT ) {
                jParser.nextToken();
                while ( jParser.nextToken() != JsonToken.END_OBJECT ) {
                    jParser.nextToken();
                    Asset asset = new Asset();
                    while ( jParser.nextToken() != JsonToken.END_OBJECT ) {
                        fieldname = jParser.getCurrentName();
                        jParser.nextToken();
                        if ( "id".equals( fieldname ) ) {
                            asset.id = jParser.getText();
                        }
                        else if ( "tradable".equals( fieldname ) ) {
                            asset.tradable = jParser.getIntValue() == 1;
                        }
                        else if ( "market_hash_name".equals( fieldname ) ) {
                            asset.urlName = jParser.getText().replace( " ", "%20" );
                        }
                        else if ( jParser.getCurrentToken() == JsonToken.START_ARRAY || jParser.getCurrentToken() == JsonToken.START_OBJECT ) {
                            jParser.skipChildren();
                        }
                    }
                    map.put( asset.id, asset );
                }
            }
        }
        return map;
    }

    private Map<String, IncompleteListing> getListings( JsonParser jParser ) throws JsonParseException, IOException {
        Map<String, IncompleteListing> map = new LinkedHashMap<>();
        String fieldname;
        while ( jParser.nextToken() != JsonToken.END_OBJECT ) {
            jParser.nextToken();
            IncompleteListing listing = new IncompleteListing();
            while ( jParser.nextToken() != JsonToken.END_OBJECT ) {
                fieldname = jParser.getCurrentName();
                jParser.nextToken();
                if ( "asset".equals( fieldname ) ) {
                    while ( jParser.nextToken() != JsonToken.END_OBJECT ) {
                        fieldname = jParser.getCurrentName();
                        jParser.nextToken();
                        if ( "id".equals( fieldname ) ) {
                            listing.id = jParser.getText();
                        }
                        else if ( "appid".equals( fieldname ) ) {
                            listing.appId = jParser.getIntValue();
                        }
                        else if ( "contextid".equals( fieldname ) ) {
                            listing.contextId = Integer.valueOf( jParser.getText() );
                        }
                        else if ( jParser.getCurrentToken() == JsonToken.START_ARRAY || jParser.getCurrentToken() == JsonToken.START_OBJECT ) {
                            jParser.skipChildren();
                        }
                    }
                }
                else if ( "listingid".equals( fieldname ) ) {
                    listing.listingId = jParser.getText();
                }
                else if ( "steamid_lister".equals( fieldname ) ) {
                    listing.steamIdLister = jParser.getText();
                }
                else if ( "converted_price".equals( fieldname ) ) {
                    listing.subTotal = jParser.getIntValue();
                }
                else if ( "converted_fee".equals( fieldname ) ) {
                    listing.fee = jParser.getIntValue();
                }
                else if ( "converted_steam_fee".equals( fieldname ) ) {
                    listing.steamFee = jParser.getIntValue();
                }
                else if ( "converted_publisher_fee".equals( fieldname ) ) {
                    listing.publisherFee = jParser.getIntValue();
                }
                else if ( "publisher_fee_app".equals( fieldname ) ) {
                    listing.publisherFeeApp = Integer.valueOf( jParser.getText() );
                }
                else if ( "publisher_fee_percent".equals( fieldname ) ) {
                    listing.publisherFeePercent = Double.valueOf( jParser.getText() );
                }
                else if ( jParser.getCurrentToken() == JsonToken.START_ARRAY ) {
                    while ( jParser.nextToken() != JsonToken.END_ARRAY ) {
                        jParser.nextToken();
                    }
                }
                else if ( jParser.getCurrentToken() == JsonToken.START_ARRAY || jParser.getCurrentToken() == JsonToken.START_OBJECT ) {
                    jParser.skipChildren();
                }
            }
            map.put( listing.id, listing );
        }
        return map;
    }

    private static class IncompleteListing {
        private int appId;

        private int contextId;

        private String id;

        private String listingId;

        private String steamIdLister;

        private int subTotal;

        private int fee;

        private int steamFee;

        private int publisherFee;

        private int publisherFeeApp;

        private double publisherFeePercent;

        Listing getListing( Asset asset ) {
            return new Listing( appId, asset.urlName, listingId, steamIdLister, subTotal, fee, steamFee, publisherFee, publisherFeeApp, publisherFeePercent );
        }
    }

    private static class Asset {
        private String id;

        private String urlName;

        private boolean tradable;
    }
}
