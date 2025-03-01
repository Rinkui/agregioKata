package org.example.fr.agregio.kata.controller.dto

data class Market(val name: String, var offers: List<Offer>? = null, var parks: List<Park>? = null)
