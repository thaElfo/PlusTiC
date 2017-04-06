package landmaster.plustic.tools;

import java.lang.reflect.*;
import java.util.List;

import com.google.common.base.*;

import net.minecraft.entity.*;
import net.minecraft.item.*;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.tinkering.*;
import slimeknights.tconstruct.library.tools.*;
import slimeknights.tconstruct.tools.*;

/**
 * currently unfinished 
 * @author Landmaster
 */
public class ToolKatana extends SwordCore {
	public static final float DURABILITY_MODIFIER = 1.05f;
	
	public ToolKatana() {
		super(PartMaterialType.head(TinkerTools.swordBlade),
				PartMaterialType.head(TinkerTools.swordBlade),
				PartMaterialType.extra(TinkerTools.toughBinding),
				PartMaterialType.handle(TinkerTools.toughToolRod));
	}
	
	@Override
	public double attackSpeed() {
		return 2.3;
	}

	@Override
	public float damagePotential() {
		return 0.9f;
	}
	
	@Override
	public boolean dealDamage(ItemStack stack, EntityLivingBase player, Entity entity, float damage) {
		if (entity instanceof EntityLivingBase) {
			EntityLivingBase targetLiving = (EntityLivingBase)entity;
			
		}
		return super.dealDamage(stack, player, entity, damage);
	}
	
	@Override
	public float getRepairModifierForPart(int index) {
	    return DURABILITY_MODIFIER;
	}

	@Override
	protected ToolNBT buildTagData(List<Material> materials) {
		ToolNBT data = buildDefaultTag(materials);
		data.attack += 1f;
		data.durability *= DURABILITY_MODIFIER;
		return data;
	}
	
	private static Field lastDamageF;
	static {
		try {
			lastDamageF = EntityLivingBase.class.getDeclaredField("lastDamage");
			lastDamageF.setAccessible(true);
		} catch (Throwable e) {
			throw Throwables.propagate(e);
		}
	}
	private static int lastDamage(EntityLivingBase elb) {
		try {
			return lastDamageF.getInt(elb);
		} catch (Throwable e) {
			throw Throwables.propagate(e);
		}
	}
	private static void lastDamage(EntityLivingBase elb, int val) {
		try {
			lastDamageF.setInt(elb, val);
		} catch (Throwable e) {
			throw Throwables.propagate(e);
		}
	}
}
