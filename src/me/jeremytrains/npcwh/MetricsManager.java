package me.jeremytrains.npcwh;

import java.io.IOException;
import java.util.logging.Logger;

import org.mcstats.Metrics;
import org.mcstats.Metrics.Graph;

public class MetricsManager {
	NPCWarehouse plugin;
	
	public MetricsManager(NPCWarehouse n) {
		plugin = n;
	}
	
	public void setupMetrics() {
		try {
		    Metrics metrics = new Metrics(plugin);
		    
		    metrics.addCustomData(new Metrics.Plotter("Number of NPCs") {
				
				@Override
				public int getValue() {
					return plugin.npcData.totNpcs;
				}
			});
		    
		    Graph graph3 = metrics.createGraph("Percentage of People who use Crafting");
		    graph3.addPlotter(new Metrics.Plotter("Use Crafting") {

		            @Override
		            public int getValue() {
		                    if (ConfigFile.crafting) {
		                    	return 1;
		                    } else {
		                    	return 0;
		                    }
		            }

		    });
		    graph3.addPlotter(new Metrics.Plotter("Don't Use Crafting") {

	            @Override
	            public int getValue() {
	                    if (ConfigFile.crafting) {
	                    	return 0;
	                    } else {
	                    	return 1;
	                    }
	            }

	    });
		    
		    Graph graph2 = metrics.createGraph("Percentage of People who use Right-Click-Selecting");
		    graph2.addPlotter(new Metrics.Plotter("Use Right-Click-Selecting") {

		            @Override
		            public int getValue() {
		                    if (ConfigFile.rightClickSelect) {
		                    	return 1;
		                    } else {
		                    	return 0;
		                    }
		            }

		    });
		    graph2.addPlotter(new Metrics.Plotter("Don't Use Right-Click-Selecting") {

	            @Override
	            public int getValue() {
	                    if (ConfigFile.rightClickSelect) {
	                    	return 0;
	                    } else {
	                    	return 1;
	                    }
	            }

	    });

		    // Construct a graph, which can be immediately used and considered as valid
		    Graph graph = metrics.createGraph("Usage of Spout Features");
		    graph.addPlotter(new Metrics.Plotter("No Spout") {

		            @Override
		            public int getValue() {
		                    if (ConfigFile.useSpout == false) {
		                    	return 1;
		                    } else {
		                    	return 0;
		                    }
		            }

		    });

		    graph.addPlotter(new Metrics.Plotter("Spout Capes") {

		            @Override
		            public int getValue() {
		                    if (ConfigFile.useSpout && ConfigFile.capes) {
		                    	return 1;
		                    } else {
		                    	return 0;
		                    }
		            }

		    });

		    graph.addPlotter(new Metrics.Plotter("Spout Skins") {

	            @Override
	            public int getValue() {
	                    if (ConfigFile.useSpout && ConfigFile.skins) {
	                    	return 1;
	                    } else {
	                    	return 0;
	                    }
	            }

		    });
		    
		    metrics.start();
		} catch (IOException e) {
		    Logger.getLogger("Minecraft").warning(e.getMessage());
		}
	}
}
