package com.gildedrose

import org.junit.Assert.*
import org.junit.Test

class GildedRoseTest {

    val aged_brie_name = "Aged Brie"

    fun GetApp(vararg items : Item): GildedRose {
        return GildedRose(arrayOf(*items))
    }

    @Test
    fun quality_never_negative() {
        val app = GetApp(Item("foo", 0, 0))
        app.updateQuality()
        assertTrue("Quality can't be negative", app.items.all { it.quality >= 0 })
    }

    @Test
    fun quality_decreases_over_time() {
        val startQuality = 10
        val app = GetApp(Item("foo", 10, startQuality))
        app.updateQuality()
        assertTrue("Quality must decrease", startQuality > app.items[0].quality)

    }

    @Test
    fun sellby_doubles_quality_loss() {
        val startQuality = 10
        val app = GetApp(Item("foo", 1, startQuality))
        app.updateQuality()
        val secondQuality = app.items[0].quality
        app.updateQuality()
        val finalQuality = app.items[0].quality

        val diff1 = startQuality - secondQuality
        val diff2 = secondQuality - finalQuality

        assertTrue("Quality should double after sell-by date", diff1 * 2 == diff2)
    }

    @Test
    fun sellby_lowers_daily() {
        val startSellin = 1
        val app = GetApp(Item("foo", startSellin, 10))
        app.updateQuality()
        val secondSellin = app.items[0].sellIn
        app.updateQuality()
        val finalSellin = app.items[0].sellIn

        assertTrue("SellIn should always decrease after updateQuality()",
            startSellin == secondSellin + 1 &&
            secondSellin == finalSellin + 1)
    }

    @Test
    fun aged_brie_improves() {
        val startQuality = 10
        val iters = 2
        val app = GetApp(Item(aged_brie_name, 50, startQuality))

        for (i in 1..iters) {
            val lastQuality = app.items[0].quality
            assertTrue("Quality shouldn't reach 50 in this test", lastQuality < 50)
            app.updateQuality()
            assertTrue("Quality of $aged_brie_name should increase",
                    app.items[0].quality > lastQuality)
        }
    }

}


