package org.bk.dungeon;

public class BuyableItem extends GameEntity<BuyableItem> {
    final String name;
    final int cost;
    final String description;
    final int mr;
    final int marketing;

    public BuyableItem(String name, int cost, String description, String regionName, int mr, int marketing) {
        super(regionName);
        this.cost = cost;
        this.description = description;
        this.name = name;
        this.mr = mr;
        this.marketing = marketing;
    }
}
