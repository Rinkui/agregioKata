package org.example.fr.agregio.kata.controller.model

import org.example.fr.agregio.kata.controller.dto.Offer
import org.example.fr.agregio.kata.controller.dto.Park

data class Market(var offers: List<Offer>, var parks: List<Park>)
