package fr.agregio.kata.controller

import org.example.AgregioApplication
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = DEFINED_PORT, classes = [AgregioApplication::class])
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@AutoConfigureWebTestClient
class MarketControllerIT(@Autowired private val webTestClient: WebTestClient) {

    @Test
    fun `post offer should succeed`() {
        givenFranceMarket()

        webTestClient
            .post()
            .uri("/api/market/France/offer")
            .contentType(APPLICATION_JSON)
            .bodyValue(
                """
                {
                  "hoursBlocs": [
                    {
                        "hours": "MORNING",
                        "producedEnergyQuantityMW": 150,
                        "floorPrice":5.23
                    },
                    {
                        "hours": "NIGHT",
                        "producedEnergyQuantityMW": 67,
                        "floorPrice": 1.99
                    }
                  ]                  
                }
            """.trimIndent()
            )
            .exchange()
            .expectStatus()
            .isAccepted
    }

    @Test
    fun `post offer with unknown market should bad request`() {
        webTestClient
            .post()
            .uri("/api/market/Unknown/offer")
            .contentType(APPLICATION_JSON)
            .bodyValue(
                """
                {
                  "hoursBlocs": [
                    {
                        "hours": "MORNING",
                        "producedEnergyQuantityMW": 150,
                        "floorPrice":5.23
                    },
                    {
                        "hours": "NIGHT",
                        "producedEnergyQuantityMW": 67,
                        "floorPrice": 1.99
                    }
                  ]                  
                }
            """.trimIndent()
            )
            .exchange()
            .expectStatus()
            .isBadRequest
            .expectBody()
            .json(
                """
                {
                  "message": "Unkown market 'Unknown'"
                }
            """.trimIndent()
            )
    }

    @Test
    fun `post park should succeed`() {
        givenFranceMarket()

        webTestClient
            .post()
            .uri("/api/market/France/park")
            .contentType(APPLICATION_JSON)
            .bodyValue(
                """
                {
                  "type": "WIND_TURBINE",
                  "productionMWByHours": {
                    "_8TO11":145,
                    "AFTERNOON":56
                  }
                }
            """.trimIndent()
            )
            .exchange()
            .expectStatus()
            .isAccepted
    }

    @Test
    fun `post park with unknown market should bad request`() {
        webTestClient
            .post()
            .uri("/api/market/Unknown/park")
            .contentType(APPLICATION_JSON)
            .bodyValue(
                """
               {
                  "type": "WIND_TURBINE",
                  "productionMWByHours": {
                    "_8TO11":145,
                    "AFTERNOON":56
                  }
                }
            """.trimIndent()
            )
            .exchange()
            .expectStatus()
            .isBadRequest
            .expectBody()
            .json(
                """
                {
                  "message": "Unkown market 'Unknown'"
                }
            """.trimIndent()
            )
    }

    @Test
    fun `get all markets offers should succeed`() {
        givenSettedUpFranceMarket()
        givenSettedUpChinaMarket()

        webTestClient
            .get()
            .uri("/api/market/offer")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .json(
                """
              {
                  "markets": [
                    {
                      "name": "China",
                      "offers": [
                        {
                          "hoursBlocs": [
                            {
                              "hours": "_8TO11",
                              "producedEnergyQuantityMW": 20,
                              "floorPrice": 11.65
                            },
                            {
                              "hours": "_11TO14",
                              "producedEnergyQuantityMW": 350,
                              "floorPrice": 0.21
                            }
                          ]
                        }
                      ]
                    },
                    {
                      "name": "France",
                      "offers": [
                        {
                          "hoursBlocs": [
                            {
                              "hours": "MORNING",
                              "producedEnergyQuantityMW": 150,
                              "floorPrice": 5.23
                            },
                            {
                              "hours": "NIGHT",
                              "producedEnergyQuantityMW": 67,
                              "floorPrice": 1.99
                            }
                          ]
                        }
                      ]
                    }
                  ]
                }
            """.trimIndent()
            )
    }


    @Test
    fun `get all markets parks should succeed`() {
        givenSettedUpFranceMarket()
        givenSettedUpChinaMarket()

        webTestClient
            .get()
            .uri("/api/market/park")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .json(
                """
              {
                  "markets": [
                    {
                      "name": "China",
                      "parks": [
                        {
                          "type": "HYDROPOWER",
                          "productionMWByHours": {
                            "_8TO11": 25,
                            "_11TO14": 800
                          }
                        }
                      ]
                    },
                    {
                      "name": "France",
                      "parks": [
                        {
                          "type": "WIND_TURBINE",
                          "productionMWByHours": {
                            "MORNING": 200,
                            "NIGHT": 90
                          }
                        }
                      ]
                    }
                  ]
                }
            """.trimIndent()
            )
    }

    @Test
    fun `get China market offers should succeed`() {
        givenSettedUpFranceMarket()
        givenSettedUpChinaMarket()

        webTestClient
            .get()
            .uri("/api/market/China/offer")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .json(
                """ 
                    {
                      "name": "China",
                      "offers": [
                        {
                          "hoursBlocs": [
                            {
                              "hours": "_8TO11",
                              "producedEnergyQuantityMW": 20,
                              "floorPrice": 11.65
                            },
                            {
                              "hours": "_11TO14",
                              "producedEnergyQuantityMW": 350,
                              "floorPrice": 0.21
                            }
                          ]
                        }
                      ]
                   }
            """.trimIndent()
            )
    }

    @Test
    fun `get offers unknown market name should bad request`() {
        webTestClient
            .get()
            .uri("/api/market/Unknown/offer")
            .exchange()
            .expectStatus()
            .isBadRequest
            .expectBody()
            .json(
                """
                {
                  "message": "Unkown market 'Unknown'"
                }
            """.trimIndent()
            )
    }

    @Test
    fun `get China market parks should succeed`() {
        givenSettedUpFranceMarket()
        givenSettedUpChinaMarket()

        webTestClient
            .get()
            .uri("/api/market/China/park")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .json(
                """
                    {
                      "name": "China",
                      "parks": [
                        {
                          "type": "HYDROPOWER",
                          "productionMWByHours": {
                            "_8TO11": 25,
                            "_11TO14": 800
                          }
                        }
                      ]
                    }
                   }
            """.trimIndent()
            )
    }

    @Test
    fun `get parks unknown market name should bad request`() {
        webTestClient
            .get()
            .uri("/api/market/Unknown/park")
            .exchange()
            .expectStatus()
            .isBadRequest
            .expectBody()
            .json(
                """
                {
                  "message": "Unkown market 'Unknown'"
                }
            """.trimIndent()
            )
    }


    private fun givenFranceMarket() {
        webTestClient
            .post()
            .uri("/api/market")
            .contentType(APPLICATION_JSON)
            .bodyValue(
                """
                {
                  "name": "France"
                }
            """.trimIndent()
            )
            .exchange()
            .expectStatus()
            .isAccepted
    }

    private fun givenChinaMarket() {
        webTestClient
            .post()
            .uri("/api/market")
            .contentType(APPLICATION_JSON)
            .bodyValue(
                """
                {
                  "name": "China"
                }
            """.trimIndent()
            )
            .exchange()
            .expectStatus()
            .isAccepted
    }

    private fun givenSettedUpFranceMarket() {
        givenFranceMarket()
        givenFranceMarketOffer()
        givenFranceMarketPark()
    }

    private fun givenSettedUpChinaMarket() {
        givenChinaMarket()
        givenChinaMarketOffer()
        givenChinaMarketPark()
    }

    private fun givenFranceMarketOffer() {
        webTestClient
            .post()
            .uri("/api/market/France/offer")
            .contentType(APPLICATION_JSON)
            .bodyValue(
                """
                {
                  "hoursBlocs": [
                    {
                      "hours": "MORNING",
                      "producedEnergyQuantityMW": 150,
                      "floorPrice": 5.23
                    },
                    {
                      "hours": "NIGHT",
                      "producedEnergyQuantityMW": 67,
                      "floorPrice": 1.99
                    }
                  ]                  
                }
            """.trimIndent()
            )
            .exchange()
            .expectStatus()
            .isAccepted
    }

    private fun givenChinaMarketOffer() {
        webTestClient
            .post()
            .uri("/api/market/China/offer")
            .contentType(APPLICATION_JSON)
            .bodyValue(
                """
                {
                  "hoursBlocs": [
                    {
                      "hours": "_8TO11",
                      "producedEnergyQuantityMW": 20,
                      "floorPrice": 11.65
                    },
                    {
                      "hours": "_11TO14",
                      "producedEnergyQuantityMW": 350,
                      "floorPrice": 0.21
                    }
                  ]
                }
            """.trimIndent()
            )
            .exchange()
            .expectStatus()
            .isAccepted
    }

    private fun givenFranceMarketPark() {
        webTestClient
            .post()
            .uri("/api/market/France/park")
            .contentType(APPLICATION_JSON)
            .bodyValue(
                """
                 {
                  "type": "WIND_TURBINE",
                  "productionMWByHours": {
                    "MORNING": 200,
                    "NIGHT": 90
                  }
                }
            """.trimIndent()
            )
            .exchange()
            .expectStatus()
            .isAccepted
    }

    private fun givenChinaMarketPark() {
        webTestClient
            .post()
            .uri("/api/market/China/park")
            .contentType(APPLICATION_JSON)
            .bodyValue(
                """
                {
                  "type": "HYDROPOWER",
                  "productionMWByHours": {
                    "_8TO11": 25,
                    "_11TO14": 800
                  }
                }
            """.trimIndent()
            )
            .exchange()
            .expectStatus()
            .isAccepted
    }
}