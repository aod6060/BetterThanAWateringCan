package com.derf.btawc;

import com.derf.btawc.proxy.IProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid=Loader.MODID, name=Loader.NAME, version=Loader.VERSION)
public class Loader {
	// Some meta data for the mod
	public final static String MODID = "btawc";
	public final static String NAME = "BTAWC";
	public final static String VERSION = "1.0.1 alpha";
	// Instance for the mod
	public static Loader INSTANCE = new Loader();
	// Proxy
	@SidedProxy(clientSide="com.derf.btawc.proxy.ProxyClient", serverSide="com.derf.btawc.proxy.ProxyServer")
	public static IProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		proxy.preInit(e);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
	}
}
