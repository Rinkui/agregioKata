package org.example.fr.agregio.kata.controller

import org.example.fr.agregio.kata.controller.dto.*
import org.example.fr.agregio.kata.service.MarketService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/market")
class MarketController(private val marketService: MarketService) {
    @PostMapping
    fun createMarket(@RequestBody name: Name): ResponseEntity<String> {
        marketService.createMarket(name.name)
        return ResponseEntity.accepted().build()
    }

    @PostMapping("{marketName}/offer")
    fun createOffer(@PathVariable marketName: String, @RequestBody offer: Offer): ResponseEntity<String> {
        marketService.createOffer(marketName, offer)
        return ResponseEntity.accepted().build()
    }

    @PostMapping("{marketName}/park")
    fun createPark(@PathVariable marketName: String, @RequestBody park: Park): ResponseEntity<String> {
        marketService.createPark(marketName, park)
        return ResponseEntity.accepted().build()
    }

    @GetMapping("/offer")
    fun getAllOffers(): ResponseEntity<Markets> {
        return ResponseEntity.ok(Markets(marketService.getMarkets().toOffersMarketDto()))
    }

    @GetMapping("{marketName}/offer")
    fun getMarketOffers(@PathVariable marketName: String): ResponseEntity<Market> {
        val offers = marketService.getMarketOffers(marketName)
        return ResponseEntity.ok(Market(name = marketName, offers = offers))
    }

    @GetMapping("/park")
    fun getAllParks(): ResponseEntity<Markets> {
        return ResponseEntity.ok(Markets(marketService.getMarkets().toParksMarketDto()))
    }

    @GetMapping("{marketName}/park")
    fun getMarketParks(@PathVariable marketName: String): ResponseEntity<Market> {
        val parks = marketService.getMarketParks(marketName)
        return ResponseEntity.ok(Market(name = marketName, parks = parks))
    }


    private fun Map<String, org.example.fr.agregio.kata.controller.model.Market>.toOffersMarketDto() =
        this.map { (marketName, market) -> Market(name = marketName, offers = market.offers) }

    private fun Map<String, org.example.fr.agregio.kata.controller.model.Market>.toParksMarketDto() =
        this.map { (marketName, market) -> Market(name = marketName, parks = market.parks) }

}

