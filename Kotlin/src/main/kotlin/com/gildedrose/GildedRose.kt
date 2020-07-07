package com.gildedrose

import kotlin.math.max
import kotlin.math.min

class GildedRose(var items: Array<Item>) {

    val MIN_QUALITY = 0
    val MAX_QUALITY = 50
    val AGED_BRIE_NAME = "Aged Brie"
    val SULFURAS_NAME = "Sulfuras, Hand of Ragnaros"
    val BACKSTAGE_PASS_NAME = "Backstage passes to a TAFKAL80ETC concert"

    fun addQuality(item : Item, quality: Int) {
        // Adds quality to item, then ensures MIN_QUALITY <= item.quality <= MAX_QUALITY
        item.quality = max(min(item.quality + quality, MAX_QUALITY), MIN_QUALITY)
    }

    fun updateQuality() {
        for (item in items) {
            when (item.name) {
                AGED_BRIE_NAME -> addQuality(item, 1)
                BACKSTAGE_PASS_NAME ->
                    when (item.sellIn) {
                        in Int.MIN_VALUE..-1 -> item.quality = 0
                        in 0..5 -> addQuality(item, 3)
                        in 6..10 -> addQuality(item, 2)
                        else -> addQuality(item, 1)
                    }
                SULFURAS_NAME -> {}
                else -> addQuality(item, -1)
            }

            if (item.name != SULFURAS_NAME) {
                item.sellIn = item.sellIn - 1
            }

            if (item.sellIn < 0) {
                when (item.name) {
                    // According to the legacy code, aged brie increases by 2 after sellIn has passed.
                    AGED_BRIE_NAME -> addQuality(item, 1)
                    SULFURAS_NAME -> {}
                    else -> addQuality(item, -1)
                }
            }
        }
    }

}

