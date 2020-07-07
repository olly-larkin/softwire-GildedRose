import { expect } from 'chai';
import { Item, GildedRose } from '../app/gilded-rose';

const aged_brie_name = 'Aged Brie'
const sulfuras_name = 'Sulfuras, Hand of Ragnaros'
const backstage_name = 'Backstage passes to a TAFKAL80ETC concert'

describe('Gilded Rose', function () {

    it('Quality never negative', function() {
        const gildedRose = new GildedRose([ new Item('foo', 0, 0) ]);
        const items = gildedRose.updateQuality();
        expect(items[0].quality).to.equal(0);
    });

    it('Quality decreases over time', function() {
        const startVal = 10
        const gildedRose = new GildedRose([ new Item('foo', 10, startVal) ]);
        const items = gildedRose.updateQuality();
        expect(items[0].quality).lessThan(startVal);
    });

    it('sellIn expiry double quality loss', function() {
        const startVal = 10
        const gildedRose = new GildedRose([ new Item('foo', 1, startVal) ]);
        const quality1 = gildedRose.updateQuality()[0].quality;
        const quality2 = gildedRose.updateQuality()[0].quality;
        expect(2*(startVal - quality1)).to.equal(quality1 - quality2)
    });

});
