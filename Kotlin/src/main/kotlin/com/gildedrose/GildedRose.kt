package com.gildedrose

import kotlin.math.max
import kotlin.math.min

class GildedRose(var items: Array<Item>) {

    val MIN_QUALITY = 0
    val MAX_QUALITY = 50
    val AGED_BRIE_NAME = "Aged Brie"
    val SULFURAS_NAME = "Sulfuras, Hand of Ragnaros"
    val BACKSTAGE_PASS_NAME = "Backstage passes to a TAFKAL80ETC concert"
    val CONJURED_NAME = "Conjured "

    private fun addQuality(item : Item, con: Boolean, quality: Int) {
        // Adds quality to item, then ensures MIN_QUALITY <= item.quality <= MAX_QUALITY
        val updatedQuality = if (con && quality < 0) quality * 2 else quality
        item.quality = max(min(item.quality + updatedQuality, MAX_QUALITY), MIN_QUALITY)
    }

    fun updateQuality() {
        for (item in items) {

            val con: Boolean
            val name: String
            if (item.name.contains(CONJURED_NAME)) {
                con = true
                name = item.name.substring(CONJURED_NAME.length)
            } else {
                con = false
                name = item.name
            }

            when (name) {
                AGED_BRIE_NAME -> addQuality(item, con, 1)
                BACKSTAGE_PASS_NAME ->
                    when (item.sellIn) {
                        in Int.MIN_VALUE..0 -> item.quality = 0
                        in 1..5 -> addQuality(item, con, 3)
                        in 6..10 -> addQuality(item, con, 2)
                        else -> addQuality(item, con, 1)
                    }
                SULFURAS_NAME -> {}
                else -> addQuality(item, con, -1)
            }

            if (name != SULFURAS_NAME) {
                item.sellIn = item.sellIn - 1
            }

            if (item.sellIn < 0) {
                when (name) {
                    // According to the legacy code, aged brie increases by 2 after sellIn has passed.
                    AGED_BRIE_NAME -> addQuality(item, con, 1)
                    SULFURAS_NAME -> {}
                    else -> addQuality(item, con, -1)
                }
            }
        }
    }

}

