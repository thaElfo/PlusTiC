package landmaster.plustic.util;

import java.util.*;

import io.netty.buffer.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.*;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.common.*;

public class DoubleCoord4D {
	public double xCoord;
	public double yCoord;
	public double zCoord;
	public int dimensionId;
	
	public DoubleCoord4D(Coord4D coord) {
		this.xCoord = coord.xCoord;
		this.yCoord = coord.yCoord;
		this.zCoord = coord.zCoord;
		this.dimensionId = coord.dimensionId;
	}
	
	public DoubleCoord4D(Entity ent) {
		xCoord = ent.posX;
		yCoord = ent.posY;
		zCoord = ent.posZ;
		dimensionId = ent.dimension;
	}
	
	public DoubleCoord4D(double x, double y, double z, int dimension) {
		xCoord = x;
		yCoord = y;
		zCoord = z;
		dimensionId = dimension;
	}
	
	public DoubleCoord4D(Vec3d pos, World world) {
		this(pos.xCoord, pos.yCoord, pos.zCoord, world.provider.getDimension());
	}
	
	public static Coord4D fromNBT(NBTTagCompound nbt) {
		if (nbt.getSize() == 0) return null;
		return new Coord4D(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"), nbt.getInteger("dim"));
	}
	
	public NBTTagCompound toNBT(NBTTagCompound nbt) {
		nbt.setDouble("x", xCoord);
		nbt.setDouble("y", yCoord);
		nbt.setDouble("z", zCoord);
		nbt.setInteger("dim", dimensionId);
		return nbt;
	}
	
	public static Coord4D fromByteBuf(ByteBuf bb) {
		return new Coord4D(bb.readInt(), bb.readInt(), bb.readInt(), bb.readInt());
	}
	
	public ByteBuf toByteBuf(ByteBuf bb) {
		return bb.writeDouble(xCoord).writeDouble(yCoord).writeDouble(zCoord).writeInt(dimensionId);
	}
	
	public Coord4D add(int x, int y, int z) {
		return new Coord4D(xCoord+x, yCoord+y, zCoord+z, dimensionId);
	}
	
	public IBlockState blockState() {
		WorldServer world = world();
		if (world == null) return null;
		return world.getBlockState(pos());
	}
	
	public TileEntity TE() {
		WorldServer world = world();
		if (world == null) return null;
		return world.getTileEntity(pos());
	}
	
	public BlockPos pos() {
		return new BlockPos(xCoord, yCoord, zCoord);
	}
	
	public WorldServer world() {
		return DimensionManager.getWorld(dimensionId);
	}
	
	@Override
	public String toString() {
		return String.format(Locale.US, "[x=%f, y=%f, z=%f] @ dimension %d", xCoord, yCoord, zCoord, dimensionId);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof DoubleCoord4D &&
				((DoubleCoord4D)obj).xCoord == xCoord &&
				((DoubleCoord4D)obj).yCoord == yCoord &&
				((DoubleCoord4D)obj).zCoord == zCoord &&
				((DoubleCoord4D)obj).dimensionId == dimensionId;
	}
}
