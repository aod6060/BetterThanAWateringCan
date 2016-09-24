package com.derf.btawc.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;

public class Holder {
	private BlockPos pos;
	private ForgeDirection direction;
	
	public Holder(BlockPos pos, ForgeDirection direction) {
		this.pos = pos;
		this.direction = direction;
	}

	public BlockPos getPos() {
		return pos;
	}

	public void setPos(BlockPos pos) {
		this.pos = pos;
	}

	public ForgeDirection getDirection() {
		return direction;
	}

	public void setDirection(ForgeDirection direction) {
		this.direction = direction;
	}
	
	public static List<Holder> getHolders(BlockPos pos) {
		List<Holder> temp = new ArrayList<Holder>();
		
		temp.add(new Holder(BlockPos.add(pos, new BlockPos(-1, 0, 0)), ForgeDirection.WEST));
		temp.add(new Holder(BlockPos.add(pos, new BlockPos(1, 0, 0)), ForgeDirection.EAST));
		temp.add(new Holder(BlockPos.add(pos, new BlockPos(0, 1, 0)), ForgeDirection.UP));
		temp.add(new Holder(BlockPos.add(pos, new BlockPos(0, -1, 0)), ForgeDirection.DOWN));
		temp.add(new Holder(BlockPos.add(pos, new BlockPos(0, 0, -1)), ForgeDirection.NORTH));
		temp.add(new Holder(BlockPos.add(pos, new BlockPos(0, 0, 1)), ForgeDirection.SOUTH));
		
		return temp;
	}
}