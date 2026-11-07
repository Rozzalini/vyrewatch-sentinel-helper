package com.vyrewatchhelper;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ExamplePluginTest
{
	public static void main(String[] args) throws Exception
	{
		// This tells the test client to force-load your custom plugin on startup
		ExternalPluginManager.loadBuiltin(VyrewatchHelperPlugin.class);
		RuneLite.main(args);
	}
}
