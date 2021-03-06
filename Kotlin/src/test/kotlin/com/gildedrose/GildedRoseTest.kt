package com.gildedrose

import com.sun.corba.se.impl.resolver.BootstrapResolverImpl
import org.junit.Assert.*
import org.junit.Test

class GildedRoseTest {

    val aged_brie_name = "Aged Brie"
    val sulfuras_name = "Sulfuras, Hand of Ragnaros"
    val backstage_pass_name = "Backstage passes to a TAFKAL80ETC concert"

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

    @Test
    fun quality_never_above_50() {
        val startQuality = 49
        val iters = 5
        val app = GetApp(Item(aged_brie_name, 50, startQuality))

        for (i in 1..iters) {
            app.updateQuality()
            assertTrue("Quality should be <= 50", app.items.all {it.quality <= 50})
        }
    }

    @Test
    fun sulfuras_never_changes() {
        val startQuality = 25
        val startSellin = 3

        val iters = 3
        val app = GetApp(Item(sulfuras_name, startSellin, startQuality))

        for (i in 1..iters) {
            app.updateQuality()
            assertTrue("Sulfuras never has to be sold and never decreases in quality",
                app.items[0].quality == startQuality &&
                        app.items[0].sellIn == startSellin)
        }
    }

    @Test
    fun backstage_passes_improve() {
        val startQuality = 10
        val iters = 2
        val app = GetApp(Item(backstage_pass_name, 50, startQuality))

        for (i in 1..iters) {
            val lastQuality = app.items[0].quality
            assertTrue("Quality shouldn't reach 50 in this test", lastQuality < 50)
            app.updateQuality()
            assertTrue("Quality of ${app.items[0].name} should increase",
                    app.items[0].quality > lastQuality)
        }
    }

    @Test
    fun backstage_passes_improve_more() {
        val startQuality = 10
        val app = GetApp(Item(backstage_pass_name, 10, startQuality),
                Item(backstage_pass_name, 5, startQuality),
                Item(backstage_pass_name, 0, startQuality))
        app.updateQuality()
        assertTrue("Quality of $backstage_pass_name should increase by 2 with <=10 days left",
                app.items[0].quality == startQuality + 2)
        assertTrue("Quality of $backstage_pass_name should increase by 3 with <=10 days left",
                app.items[1].quality == startQuality + 3)
        assertTrue("Quality of $backstage_pass_name should drop to 0 after concert",
                app.items[2].quality == 0)
    }

    @Test
    fun conjured_items_degrade_faster() {
        val startQuality = 10
        // Only want degrading itemNames
        for (itemName in arrayOf("foo", sulfuras_name)) {
            val app = GetApp(
                    Item("Conjured $itemName", 10, startQuality),
                    Item(itemName, 10, startQuality)
            )

            app.updateQuality()
            val diffConjured = startQuality - app.items[0].quality
            val diffNormal = startQuality - app.items[1].quality
            assertTrue("Conjured items should degrade twice as fast as normal items",
                    diffConjured == 2 * diffNormal)
        }
    }

    @Test
    fun test_same_as_legacy() {
        // Want to test that backstage, brie, and other items `foo` all behave the same
        // NOT TESTING "conjured" -- Doesn't exist in legacy code!!

        val numItems = 10
        val numItersEach = 10
        val numItersTotal = 100
        val itemNames = arrayOf(backstage_pass_name, aged_brie_name, sulfuras_name, "foo")

        for (iter in 1..numItersTotal) {
            val items = mutableListOf<Item>()
            for (i in 1..numItems) {
                items.add(
                        Item(itemNames.random(), (1..numItersEach).random(), (1..49).random())
                )
            }
            val thisApp = GildedRose(items.toTypedArray())
            val referenceApp = GildedRoseLegacy(items.toTypedArray())

            for (i in 1..numItersEach) {
                thisApp.updateQuality()
                referenceApp.updateQuality()
                thisApp.items.zip(referenceApp.items) {
                    thisItem, refItem -> assertTrue("$thisItem, $refItem should be equal",
                        thisItem.quality == refItem.quality &&
                        thisItem.sellIn == refItem.sellIn)
                }
            }
        }
    }

}


