package landmaster.plustic.tools;

import java.awt.Color;
import java.lang.reflect.*;
import java.util.*;

import javax.annotation.*;

import com.google.common.base.*;

import net.minecraft.client.*;
import net.minecraft.client.resources.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.relauncher.*;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.tinkering.*;
import slimeknights.tconstruct.library.tools.*;
import slimeknights.tconstruct.library.utils.*;
import slimeknights.tconstruct.tools.*;
import slimeknights.tconstruct.tools.ranged.item.*;

public class ToolKatana extends SwordCore {
	public static final float DURABILITY_MODIFIER = 0.88f;
	public static final float MAX_COUNTER = 16;
	
	public static final String COUNTER_TAG = "PlusTiC_Counter";
	
	static {
		MinecraftForge.EVENT_BUS.register(ToolKatana.class);
	}
	
	public ToolKatana() {
		super(PartMaterialType.handle(TinkerTools.toughToolRod),
				PartMaterialType.head(TinkerTools.largeSwordBlade),
				PartMaterialType.head(TinkerTools.largeSwordBlade),
				PartMaterialType.extra(TinkerTools.toughBinding));
		setUnlocalizedName("katana").setRegistryName("katana");
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void render(RenderGameOverlayEvent event) {
		final Minecraft mc = Minecraft.getMinecraft();
		final ItemStack is = mc.thePlayer.getHeldItemMainhand();
		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT
				&& is != null && is.getItem() instanceof ToolKatana) {
			float counter = TagUtil.getTagSafe(is).getFloat(COUNTER_TAG);
			if (counter > 0) {
				mc.fontRendererObj.drawString(I18n.format("meter.plustic.katana", counter),
						5, 5, Color.HSBtoRGB(Math.min(counter/(MAX_COUNTER*3), 1.0f/3), 1, 1) & 0xFFFFFF, true);
			}
		}
	}
	
	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (hand == EnumHand.MAIN_HAND && playerIn.getHeldItemOffhand() != null && !(playerIn.getHeldItemOffhand().getItem() instanceof Shuriken)) {
			return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
		}
		return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
	}
	
	@Override
	public int[] getRepairParts() {
		return new int[] {1,2};
	}
	
	@Override
	public double attackSpeed() {
		return 2.55;
	}

	@Override
	public float damagePotential() {
		return 0.77f;
	}
	
	@Override
	public float damageCutoff() {
		return 48.0f;
	}
	
	@Override
	public float knockback() {
		return 0.83f;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		NBTTagCompound tag = TagUtil.getTagSafe(stack);
		float counter = tag.getFloat(COUNTER_TAG);
		counter = Math.max(counter-0.1f, 0);
		tag.setFloat(COUNTER_TAG, counter);
		stack.setTagCompound(tag);
	}
	
	@Override
	public boolean dealDamage(ItemStack stack, EntityLivingBase player, Entity entity, float damage) {
		boolean scaleDown = false;
		if (entity instanceof EntityLivingBase) {
			EntityLivingBase targetLiving = (EntityLivingBase)entity;
			if (entity.hurtResistantTime > 0 && lastDamage(targetLiving) > 0) {
				scaleDown = true;
			}
			entity.hurtResistantTime = 0;
			lastDamage(targetLiving, 0);
			if (targetLiving.getTotalArmorValue() <= 0) {
				damage += 2.6f; // increase damage against unarmored
			}
		}
		NBTTagCompound tag = TagUtil.getTagSafe(stack);
		float counter = tag.getFloat(COUNTER_TAG);
		damage += counter * 0.84f;
		if (scaleDown) damage *= 0.64f;
		
		boolean success = super.dealDamage(stack, player, entity, damage);
		if (success) {
			if (entity.isDead) counter += 0.7f;
			if (scaleDown) counter += 0.6f;
			counter += 0.8f;
			if (++counter > MAX_COUNTER) counter = MAX_COUNTER;
			if (entity instanceof EntityLivingBase) {
				EntityLivingBase targetLiving = (EntityLivingBase)entity;
				if (counter >= 12) {
					targetLiving.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 200, 3));
				}
				if (counter >= 9) {
					targetLiving.addPotionEffect(new PotionEffect(MobEffects.WITHER, 200, 1));
				}
				if (counter >= 6) {
					targetLiving.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 200, 1));
				}
			}
			tag.setFloat(COUNTER_TAG, counter);
			stack.setTagCompound(tag);
		}
		return success;
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
	
	private static final Field lastDamageF;
	static {
		try {
			lastDamageF = EntityLivingBase.class.getDeclaredField(
					"field_110153_bc"/* lastDamage */);
			lastDamageF.setAccessible(true);
		} catch (Throwable e) {
			throw Throwables.propagate(e);
		}
	}
	
	private static float lastDamage(EntityLivingBase elb) {
		try {
			return lastDamageF.getFloat(elb);
		} catch (Throwable e) {
			throw Throwables.propagate(e);
		}
	}
	
	private static void lastDamage(EntityLivingBase elb, float val) {
		try {
			lastDamageF.setFloat(elb, val);
		} catch (Throwable e) {
			throw Throwables.propagate(e);
		}
	}
}
