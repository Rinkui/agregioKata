package org.example.fr.agregio.kata.controller.dto

data class Park(val type : ParkType, val productionMWByHours : Map<Hours, Int>)
