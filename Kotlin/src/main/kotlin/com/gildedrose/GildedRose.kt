package com.gildedrose

class GildedRose(var items: Array<Item>) {

    fun updateQuality() {
        for (item in items) {
            when (item.name) {
                "Aged Brie" -> if (item.quality < 50) item.quality += 1
                "Backstage passes to a TAFKAL80ETC concert" ->
                    when (item.sellIn) {
                        in Int.MIN_VALUE..-1 -> item.quality = 0
                        in 0..5 -> if (item.quality <= 47) item.quality += 3 else item.quality = 50
                        in 6..10 -> if (item.quality <= 48) item.quality += 2 else item.quality = 50
                        else -> if (item.quality < 50) item.quality += 1
                    }
                "Sulfuras, Hand of Ragnaros" -> {}
                else -> if (item.quality > 0) item.quality -= 1
            }

            if (item.name != "Sulfuras, Hand of Ragnaros") {
                item.sellIn = item.sellIn - 1
            }

            if (item.sellIn < 0) {
                when (item.name) {
                    "Aged Brie" -> if (item.quality < 50) item.quality += 1
                    "Backstage passes to a TAFKAL80ETC concert", "Sulfuras, Hand of Ragnaros" -> {}
                    else -> if (item.quality > 0) item.quality -= 1
                }
            }
        }
    }

}

