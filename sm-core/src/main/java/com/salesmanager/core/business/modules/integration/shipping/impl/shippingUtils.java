package com.salesmanager.core.business.modules.integration.shipping.impl;

import java.util.Arrays;
import java.util.Collections;

import com.amazonaws.endpointdiscovery.Constants;
import com.salesmanager.core.model.shipping.PackageDetails;
import com.salesmanager.core.model.shipping.Quote;

import antlr.collections.List;

public class shippingUtils {

    public static class ShippingStats {
        public final Double distance;
        public final Double volume;
        public final Double weight;
        public final Double size;

        public ShippingStats(Double distance, Double volume, Double weight, Double size) {
            this.distance = distance;
            this.volume = volume;
            this.weight = weight;
            this.size = size;
        }
    }

    public static ShippingStats calculateStats(Quote quote, List<PackageDetails> packages) {
        Double distance = null;

        if (quote != null && quote.getQuoteInformations() != null &&
            quote.getQuoteInformations().containsKey(Constants.DISTANCE_KEY)) {
            distance = (Double) quote.getQuoteInformations().get(Constants.DISTANCE_KEY);
        }

        Double volume = null;
        Double weight = 0D;
        Double size = null;

        for (PackageDetails pack : packages) {
            weight += pack.getShippingWeight();

            Double tmpVolume = pack.getShippingHeight() * pack.getShippingLength() * pack.getShippingWidth();
            if (volume == null || tmpVolume > volume) {
                volume = tmpVolume;
            }

            List<Double> sizeList = Arrays.asList(
                pack.getShippingHeight(),
                pack.getShippingWidth(),
                pack.getShippingLength()
            );
            Double maxSize = Collections.max(sizeList);
            if (size == null || maxSize > size) {
                size = maxSize;
            }
        }

        return new ShippingStats(distance, volume, weight, size);
    }
}

