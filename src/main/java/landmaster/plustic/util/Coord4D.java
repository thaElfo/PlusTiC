package landmaster.plustic.util;

import net.minecraft.block.state.*;
import net.minecraft.entity.*;
import net.minecraft.nbt.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.common.*;

public class Coord4D {
	public int xCoord;
	public int yCoord;
	public int zCoord;
	public int dimensionId;
	
	public Coord4D(Entity ent) {
		xCoord = (int)ent.posX;
		yCoord = (int)ent.posY;
		zCoord = (int)ent.posZ;
		
		dimensionId = ent.dimension;
	}
	
	public Coord4D(double x, double y, double z, int dimension) {
		xCoord = MathHelper.floor_double(x);
		yCoord = MathHelper.floor_double(y);
		zCoord = MathHelper.floor_double(z);
		dimensionId = dimension;
	}
	
	public static Coord4D fromNBT(NBTTagCompound nbt) {
		if (nbt.getSize() == 0) return null;
		return new Coord4D(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"), nbt.getInteger("dim"));
	}
	
	public Coord4D add(int x, int y, int z) {
		return new Coord4D(xCoord+x, yCoord+y, zCoord+z, dimensionId);
	}
	
	public IBlockState blockState() {
		WorldServer world = DimensionManager.getWorld(dimensionId);
		if (world == null) return null;
		return world.getBlockState(new BlockPos(xCoord, yCoord, zCoord));
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Coord4D &&
				((Coord4D)obj).xCoord == xCoord &&
				((Coord4D)obj).yCoord == yCoord &&
				((Coord4D)obj).zCoord == zCoord &&
				((Coord4D)obj).dimensionId == dimensionId;
	}
}
