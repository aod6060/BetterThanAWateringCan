package com.derf.btawc.network;

import com.derf.btawc.Loader;
import com.derf.btawc.network.data.FactoryPacketData;
import com.derf.btawc.network.data.IPacketData;
import com.derf.btawc.network.handlers.PacketToClientHandler;
import com.derf.btawc.network.handlers.PacketToServerHandler;
import com.derf.btawc.network.packets.PacketCreativeGeneratorInfo;
import com.derf.btawc.network.packets.PacketSixSidedConfiguration;
import com.derf.btawc.network.packets.PacketTankFluidUpdate;
import com.derf.btawc.network.packets.PacketToClient;
import com.derf.btawc.network.packets.PacketToServer;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public final class PacketHandler {
	private static int packetId = 0;
	public static SimpleNetworkWrapper INSTANCE = null;
	
	public static int nextID() {
		return ++packetId;
	}
	
	public static void registerMessages() {
		INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Loader.MODID);
		// Register Custom Messages
		// Creative Generator
		INSTANCE.registerMessage(PacketCreativeGeneratorInfo.Handler.class, PacketCreativeGeneratorInfo.class, nextID(), Side.SERVER);
		// Six Sided Configuration
		INSTANCE.registerMessage(PacketSixSidedConfiguration.Handler.class, PacketSixSidedConfiguration.class, nextID(), Side.SERVER);
		// Tank Fluid Update
		INSTANCE.registerMessage(PacketTankFluidUpdate.Handler.class, PacketTankFluidUpdate.class, nextID(), Side.CLIENT);
		// Packet to Client
		INSTANCE.registerMessage(PacketToClientHandler.class, PacketToClient.class, nextID(), Side.CLIENT);
		// Packet to Server
		INSTANCE.registerMessage(PacketToServerHandler.class, PacketToServer.class, nextID(), Side.SERVER);
		
		// Create Factory
		FactoryPacketData.create();
	}
	
	public static void sendPacketToClient(IPacketData packetData) {
		INSTANCE.sendToAll(new PacketToClient(packetData));
	}
	
	public static void sendPacketToServer(IPacketData packetData, World world) {
		INSTANCE.sendToServer(new PacketToServer(packetData, world.provider.getDimension()));
	}
	
}
