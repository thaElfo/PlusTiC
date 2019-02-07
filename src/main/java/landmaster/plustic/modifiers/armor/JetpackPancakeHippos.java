package landmaster.plustic.modifiers.armor;

import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.*;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Throwables;

import c4.conarm.lib.modifiers.*;
import landmaster.plustic.net.*;
import landmaster.plustic.util.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.settings.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraft.world.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.*;
import net.minecraftforge.energy.*;
import net.minecraftforge.fml.client.*;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraftforge.items.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.modifiers.*;
import slimeknights.tconstruct.library.utils.*;
import tonius.simplyjetpacks.*;
import tonius.simplyjetpacks.client.handler.*;
import tonius.simplyjetpacks.client.util.*;
import tonius.simplyjetpacks.handler.*;
import tonius.simplyjetpacks.item.*;
import tonius.simplyjetpacks.util.*;

/**
 * Adapted from SimplyJetpack2's code.
 * @author Landmaster
 *
 */
public class JetpackPancakeHippos extends ArmorModifierTrait {
	static {
		MinecraftForge.EVENT_BUS.register(JetpackPancakeHippos.class);
	}
	
	public static enum JetpackSettings {
		ENGINE, HOVER, EHOVER;
		
		public String getNBTKey() {
			return "PlusTiC_SJ2_"+this.name();
		}
		
		public boolean isOff(ItemStack stack) {
			return TagUtil.getTagSafe(stack).hasKey(getNBTKey());
		}
		
		public void setOff(ItemStack stack, boolean off) {
			if (off) {
				TagUtil.getTagSafe(stack).setBoolean(getNBTKey(), false);
			} else {
				TagUtil.getTagSafe(stack).removeTag(getNBTKey());
			}
		}
		
		public String getStateMsgUnloc() {
			switch (this) {
			case EHOVER:
				return "chat.itemJetpack.emergencyHoverMode";
			case ENGINE:
				return "chat.itemJetpack.on";
			case HOVER:
				return "chat.itemJetpack.hoverMode";
			default:
				return "";
			}
		}
	}
	
	private static final ArrayList<KeyBinding> keys;
	static {
		ArrayList<KeyBinding> temp = null;
		try {
			Field f = KeyTracker.class.getDeclaredField("keys");
			f.setAccessible(true);
			temp = (ArrayList<KeyBinding>) MethodHandles.lookup().unreflectGetter(f).invokeExact();
		} catch (Throwable e) {
			if (FMLCommonHandler.instance().getSide() == Side.CLIENT) { 
				Throwables.throwIfUnchecked(e);
				throw new RuntimeException(e);
			}
		}
		keys = temp;
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onKeyInput(InputEvent.KeyInputEvent event) {
		EntityPlayer player = FMLClientHandler.instance().getClient().player;
		ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

		Utils.getModifiers(chestStack).stream()
		.filter(trait -> trait instanceof JetpackPancakeHippos)
		.findAny().ifPresent(trait -> {
			for (KeyBinding keyBindings : keys) {
				int button = keyBindings.getKeyCode();
				if (button > 0 && keyBindings.isPressed()) {
					JetpackSettings setting = null;
					if (keyBindings.getKeyDescription().equals(SimplyJetpacks.PREFIX + "keybind.engine")) {
						setting = JetpackSettings.ENGINE;
					} else if (keyBindings.getKeyDescription().equals(SimplyJetpacks.PREFIX + "keybind.hover")) {
						setting = JetpackSettings.HOVER;
					} else if (keyBindings.getKeyDescription().equals(SimplyJetpacks.PREFIX + "keybind.emergencyhover")) {
						setting = JetpackSettings.EHOVER;
					}
					if (setting != null) {
						boolean oldState = setting.isOff(chestStack);
						if (tonius.simplyjetpacks.config.Config.enableStateMessages) {
							String unlocTemp = setting.getStateMsgUnloc();
							ITextComponent state = SJStringHelper.localizeNew(oldState ? "chat.enabled" : "chat.disabled");
							state.setStyle(new Style().setColor(oldState ? TextFormatting.GREEN : TextFormatting.RED));
							player.sendStatusMessage(SJStringHelper.localizeNew(unlocTemp, state), true);
						}
						PacketHandler.INSTANCE.sendToServer(new PacketSJKey(setting));
					}
				}
			}
		});
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(receiveCanceled = true)
	public static void onOverlayRender(RenderGameOverlayEvent.Post event) {
		if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
			return;
		}
		if (Minecraft.getMinecraft().player != null) {
			if ((Minecraft.getMinecraft().currentScreen == null || tonius.simplyjetpacks.config.Config.showHUDWhileChatting && Minecraft.getMinecraft().currentScreen instanceof GuiChat) && !Minecraft.getMinecraft().gameSettings.hideGUI && !Minecraft.getMinecraft().gameSettings.showDebugInfo) {
				ItemStack chestplate = Minecraft.getMinecraft().player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
				Utils.getModifiers(chestplate).stream()
				//.peek(modifier -> System.out.println(modifier.getIdentifier()))
				.filter(trait -> trait instanceof JetpackPancakeHippos)
				.findAny().ifPresent(trait -> {
					List<String> info = new ArrayList<String>();
					((JetpackPancakeHippos)trait).addHUDInfo(info, chestplate, tonius.simplyjetpacks.config.Config.enableStateHUD);
					if (info.isEmpty()) {
						return;
					}

					GL11.glPushMatrix();
					Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
					GL11.glScaled(tonius.simplyjetpacks.config.Config.HUDScale, tonius.simplyjetpacks.config.Config.HUDScale, 1.0D);

					int i = 0;
					for (String s : info) {
						RenderUtils.drawStringAtHUDPosition(s, RenderUtils.HUDPositions.values()[tonius.simplyjetpacks.config.Config.HUDPosition], Minecraft.getMinecraft().fontRenderer, tonius.simplyjetpacks.config.Config.HUDOffsetX, tonius.simplyjetpacks.config.Config.HUDOffsetY, tonius.simplyjetpacks.config.Config.HUDScale, 0xeeeeee, true, i);
						i++;
					}

					GL11.glPopMatrix();
				});
			}
		}
	}
	
	/*
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent evt) {
		if (evt.phase == TickEvent.Phase.START) {
			
		}
	}*/
	
	public void addHUDInfo(List<String> list, ItemStack stack, boolean showState) {
		if (showState) {
			list.add(this.getHUDStatesInfo(stack));
		}
	}
	public String getHUDStatesInfo(ItemStack stack) {
		return SJStringHelper.getHUDStateText(!JetpackSettings.ENGINE.isOff(stack), !JetpackSettings.HOVER.isOff(stack), null);
	}
	
	public static final Map<Jetpack, JetpackPancakeHippos> jetpackpancakehippos
		= Arrays.stream(Jetpack.values())
		.filter(jetpack -> !jetpack.isArmored)
		.collect(() -> new EnumMap<>(Jetpack.class), (m,j) -> {
			JetpackPancakeHippos trait = new JetpackPancakeHippos(j);
			//trait.addItem(j.getStackJetpack(), 1, 1);
			m.put(j, trait);
		}, Map::putAll);
	
	public final Jetpack jetpack;
	
	public JetpackPancakeHippos(Jetpack jetpack) {
		super("jetpackpancakehippos_"+jetpack.name().toLowerCase(Locale.US), 0x60b2ff);
		this.jetpack = jetpack;
		//addAspects(new ModifierAspect.DataAspect(this), new ModifierAspect.SingleAspect(this), ModifierAspect.freeModifier);
	}
	
	@Override
	public boolean canApplyTogether(IToolMod otherModifier) {
		return super.canApplyTogether(otherModifier) && !(otherModifier instanceof JetpackPancakeHippos);
	}
	
	@Override
    public boolean canApplyCustom(ItemStack stack) {
		//System.out.println("STACK: "+stack);
		return EntityLiving.getSlotForItemStack(stack) == EntityEquipmentSlot.CHEST && super.canApplyCustom(stack);
	}
	
	
	/*@Override
	public Optional<RecipeMatch.Match> matches(NonNullList<ItemStack> stacks) {
		System.out.println("Candidates: "+stacks);
		System.out.println("Matchers: "+items);
		Optional<RecipeMatch.Match> res = super.matches(stacks);
		System.out.println("Verdict: "+res);
		return res;
	}*/
	
	@Override
	public void onArmorTick(ItemStack armor, World world, EntityPlayer player) {
		this.flyUser(player, armor, false);
	}
	
	protected int getFuelUsage(ItemStack stack) {
		if (jetpack.getBaseName().contains("enderium")) {
			return (int)Math.round(jetpack.getFuelUsage() * 0.8);
		}
		return jetpack.getFuelUsage();
	}
	
	protected void flyUser(EntityPlayer user, ItemStack stack, boolean force) {
		int fuelUsage = (int) (user.isSprinting() ? Math.round(this.getFuelUsage(stack) * jetpack.sprintFuelModifier) : this.getFuelUsage(stack));
		
		Optional<IEnergyStorage> storage = Optional.empty();
		IItemHandler cap = user.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		for (int i=0; i<cap.getSlots(); ++i) {
			ItemStack energyStack = cap.getStackInSlot(i);
			storage = Optional.ofNullable(energyStack.getCapability(CapabilityEnergy.ENERGY, null))
					.filter(raw -> raw.extractEnergy(fuelUsage, true) >= fuelUsage);
			if (storage.isPresent()) break;
		}
		
		if (!JetpackSettings.ENGINE.isOff(stack)) {
			boolean hoverMode = !JetpackSettings.HOVER.isOff(stack);
			double hoverSpeed = tonius.simplyjetpacks.config.Config.invertHoverSneakingBehavior == SyncHandler.isDescendKeyDown(user) ? jetpack.speedVerticalHoverSlow : jetpack.speedVerticalHover;
			boolean flyKeyDown = force || SyncHandler.isFlyKeyDown(user);
			boolean descendKeyDown = SyncHandler.isDescendKeyDown(user);
			double currentAccel = jetpack.accelVertical * (user.motionY < 0.3D ? 2.5D : 1.0D);
			double currentSpeedVertical = jetpack.speedVertical * (user.isInWater() ? 0.4D : 1.0D);
			
			//System.out.println(flyKeyDown + " " + descendKeyDown);
			
			if (flyKeyDown || hoverMode && !user.onGround) {
				if (jetpack.usesFuel && storage.isPresent()) {
					storage.get().extractEnergy(fuelUsage, false);
				}

				if (!jetpack.usesFuel || storage.map(IEnergyStorage::getEnergyStored).filter(e -> e>0).isPresent()) {
					if (flyKeyDown) {
						if (!hoverMode) {
							user.motionY = Math.min(user.motionY + currentAccel, currentSpeedVertical);
						} else {
							if (descendKeyDown) {
								user.motionY = Math.min(user.motionY + currentAccel, -jetpack.speedVerticalHoverSlow);
							} else {
								user.motionY = Math.min(user.motionY + currentAccel, jetpack.speedVerticalHover);
							}
						}
					} else {
						user.motionY = Math.min(user.motionY + currentAccel, -hoverSpeed);
					}

					float speedSideways = (float) (user.isSneaking() ? jetpack.speedSideways * 0.5F : jetpack.speedSideways);
					float speedForward = (float) (user.isSprinting() ? speedSideways * jetpack.sprintSpeedModifier : speedSideways);
					if (SyncHandler.isForwardKeyDown(user)) {
						user.moveRelative(0, 0, speedForward, speedForward);
					}
					if (SyncHandler.isBackwardKeyDown(user)) {
						user.moveRelative(0, 0, -speedSideways, speedSideways * 0.8F);
					}
					if (SyncHandler.isLeftKeyDown(user)) {
						user.moveRelative(speedSideways, 0, 0, speedSideways);
					}
					if (SyncHandler.isRightKeyDown(user)) {
						user.moveRelative(-speedSideways, 0, 0, speedSideways);
					}

					if (!user.world.isRemote) {
						user.fallDistance = 0.0F;

						if (user instanceof EntityPlayerMP) {
							((EntityPlayerMP) user).connection.floatingTickCount = 0;
						}
					}
				}
			}
		}

		//Emergency Hover
		if (!user.world.isRemote && jetpack.emergencyHoverMode && !JetpackSettings.EHOVER.isOff(stack)) {
			if ((!jetpack.usesFuel || storage.map(IEnergyStorage::getEnergyStored).filter(e -> e>0).isPresent()) && (JetpackSettings.HOVER.isOff(stack) || JetpackSettings.ENGINE.isOff(stack))) {
				if (user.posY < -5) {
					this.doEHover(stack, user);
				} else {
					if (!user.capabilities.isCreativeMode && user.fallDistance - 1.2F >= user.getHealth()) {
						for (int j = 0; j <= 16; j++) {
							int x = Math.round((float) user.posX - 0.5F);
							int y = Math.round((float) user.posY) - j;
							int z = Math.round((float) user.posZ - 0.5F);
							if (!user.world.isAirBlock(new BlockPos(x, y, z))) {
								this.doEHover(stack, user);
								break;
							}
						}
					}
				}
			}
		}
	}
	
	protected void doEHover(ItemStack armor, EntityPlayer user) {
		JetpackSettings.ENGINE.setOff(armor, false);
		JetpackSettings.HOVER.setOff(armor, false);
		ITextComponent msg = SJStringHelper.localizeNew("chat.itemJetpack.emergencyHoverMode.msg");
		msg.setStyle(new Style().setColor(TextFormatting.RED));
		user.sendStatusMessage(msg, true);
	}
	
	@Override
	public String getLocalizedName() {
		return Util.translate(LOC_Name, "jetpackpancakehippos");
	}
	
	@Override
	public String getLocalizedDesc() {
		return Util.translate(LOC_Desc, "jetpackpancakehippos");
	}
}
