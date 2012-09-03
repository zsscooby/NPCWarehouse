package me.jeremytrains.npcwh.type.trader;

import org.bukkit.Material;

public class Stockable {
	private double price;
	private Material item;
	
	public Stockable(Material m, double p) {
		item = m;
		price = p;
	}
	
	public Material getItem() {
		return item;
	}
	
	public double getPrice() {
		return price;
	}
}
