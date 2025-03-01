package org.example.fr.agregio.kata.service

import org.example.fr.agregio.kata.controller.dto.Offer
import org.example.fr.agregio.kata.controller.dto.Park
import org.example.fr.agregio.kata.controller.model.Market
import org.springframework.stereotype.Service

@Service
class MarketService {
    private val markets = mutableMapOf<String, Market>()


    fun createMarket(name: String) {
        markets[name] = Market(emptyList(), emptyList())
    }

    fun createOffer(marketName: String, offer: Offer) {
        val market = markets[marketName] ?: throw IllegalArgumentException("Unkown market '$marketName'")
        market.offers += offer
    }

    fun createPark(marketName: String, park: Park) {
        val market = markets[marketName] ?: throw IllegalArgumentException("Unkown market '$marketName'")
        market.parks += park
    }

    fun getMarkets() = markets
    
    fun getMarketOffers(marketName: String): List<Offer> {
        val market = markets[marketName] ?: throw IllegalArgumentException("Unkown market '$marketName'")
        return market.offers
    }

    fun getMarketParks(marketName: String): List<Park> {
        val market = markets[marketName] ?: throw IllegalArgumentException("Unkown market '$marketName'")
        return market.parks
    }
}