package com.derf.btawc.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

public class MobDropHandler {
	
	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent e) {
	}
	
	@SubscribeEvent
	public void onEvent(LivingDropsEvent e) {
	}
}