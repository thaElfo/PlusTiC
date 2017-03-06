package landmaster.plustic.toggle;

import java.io.*;
import java.util.*;

import landmaster.plustic.*;
import landmaster.plustic.net.*;
import landmaster.plustic.proxy.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.fml.relauncher.*;
import slimeknights.tconstruct.library.utils.*;

public class Toggle {
	public static final int GUI_ID = 1;
	
	public static Set<String> toggleable = new HashSet<>();
	
	public static class Gui extends GuiScreen {
		public static final int OPTIONS_PER_PAGE = 6;
		
		private final ResourceLocation background = new ResourceLocation(PlusTiC.MODID, "textures/gui/toggle.png");
		
		private int page;
		private EntityPlayer player;
		private List<String> identifiers;
		private List<Boolean> enableds;
		
		private int xSize = 176;
		private int ySize = 128;
		private int guiLeft, guiTop;
		
		public Gui(EntityPlayer player) {
			this.player = player;
			guiLeft = (width - xSize) / 2;
	        guiTop = (height - ySize) / 2;
			page = 0;
			identifiers = new ArrayList<>();
			enableds = new ArrayList<>();
			NBTTagCompound nbt = TagUtil.getTagSafe(this.player.getHeldItemMainhand());
			NBTTagList modifiers = TagUtil.getBaseModifiersTagList(nbt);
			NBTTagList traits = TagUtil.getTraitsTagList(nbt);
			String identifier;
			for (int i=0; i<modifiers.tagCount(); ++i) {
				identifier = modifiers.getStringTagAt(i);
				if (toggleable.contains(identifier)) {
					identifiers.add(identifier);
					enableds.add(getToggleState(nbt, identifier));
				}
			}
			for (int i=0; i<traits.tagCount(); ++i) {
				identifier = traits.getStringTagAt(i);
				if (toggleable.contains(identifier)){
					identifiers.add(identifier);
					enableds.add(getToggleState(nbt, identifier));
				}
			}
		}
		
		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks) {
			super.drawScreen(mouseX, mouseY, partialTicks);
			guiLeft = (width - xSize) / 2;
	        guiTop = (height - ySize) / 2;
			
			mc.renderEngine.bindTexture(background);
			drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
			fontRendererObj.drawString(I18n.format("gui.header.toggle"), guiLeft+5, guiTop+5, 0xFFFFFF);
			
			mc.renderEngine.bindTexture(background);
			for (int i=0; i<OPTIONS_PER_PAGE; ++i) {
				drawTexturedModalRect(guiLeft+7, guiTop+18*(i+1), 0, 128, 114, 16);
			}
			for (int i=page*OPTIONS_PER_PAGE; i<Math.min((page+1)*OPTIONS_PER_PAGE, identifiers.size()); ++i) {
				String identifier = identifiers.get(i);
				boolean enabled = enableds.get(i);
				fontRendererObj.drawString(I18n.format("modifier."+identifier+".name"), guiLeft+10, guiTop+18*(i+1)+3, 0xFFFFFF);
				mc.renderEngine.bindTexture(background);
				drawTexturedModalRect(guiLeft+96, guiTop+18*(i+1)+1,176+(enabled ? 0 : 12), 0, 12, 12);
				if (isPointInRegion(7, 18*(i+1), 114, 16, mouseX, mouseY)) {
					drawHoveringText(Arrays.asList("Click to toggle"), mouseX, mouseY);
				}
			}
		}
		
		@Override
		public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
			try {
				super.mouseClicked(mouseX, mouseY, mouseButton);
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (int i=0; i<Math.min(OPTIONS_PER_PAGE, identifiers.size() - page*OPTIONS_PER_PAGE); ++i) {
				if (isPointInRegion(7, 18*(i+1), 114, 16, mouseX, mouseY)) {
					String identifier = identifiers.get(page*OPTIONS_PER_PAGE+i);
					PacketHandler.INSTANCE.sendToServer(new PacketHandleToggleGui(identifier));
					return;
				}
			}
		}
		
		protected boolean isPointInRegion(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY) {
	        int i = this.guiLeft;
	        int j = this.guiTop;
	        pointX = pointX - i;
	        pointY = pointY - j;
	        return pointX >= rectX - 1 && pointX < rectX + rectWidth + 1 && pointY >= rectY - 1 && pointY < rectY + rectHeight + 1;
		}
		
		public void update(String identifier, boolean value) {
			System.out.println("UPDATE: "+identifier+" "+value);
			int ind = identifiers.indexOf(identifier);
			if (ind>=0) enableds.set(ind, value);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void testAndToggle(InputEvent.KeyInputEvent event) {
		if (CommonProxy.keyBindings.get(1).isPressed()
				&& canToggle(Minecraft.getMinecraft().thePlayer.getHeldItemMainhand())) {
			Minecraft.getMinecraft().displayGuiScreen(new Gui(Minecraft.getMinecraft().thePlayer));
		}
	}
	
	public static boolean canToggle(ItemStack is) {
		return canToggle(TagUtil.getTagSafe(is));
	}
	
	public static boolean canToggle(NBTTagCompound nbt) {
		NBTTagList modifiers = TagUtil.getBaseModifiersTagList(nbt);
		NBTTagList traits = TagUtil.getTraitsTagList(nbt);
		for (int i=0; i<modifiers.tagCount(); ++i) {
			if (toggleable.contains(modifiers.getStringTagAt(i))) return true;
		}
		for (int i=0; i<traits.tagCount(); ++i) {
			if (toggleable.contains(traits.getStringTagAt(i))) return true;
		}
		return false;
	}
	
	public static boolean getToggleState(ItemStack is, String identifier) {
		NBTTagCompound nbt = TagUtil.getTagSafe(is);
		return getToggleState(nbt, identifier);
	}
	
	public static boolean getToggleState(NBTTagCompound nbt, String identifier) {
		NBTTagCompound toggle = TagUtil.getTagSafe(nbt, "PlusTiC_toggle");
		return (TinkerUtil.hasTrait(nbt, identifier) || TinkerUtil.hasModifier(nbt, identifier))
				&& (!toggleable.contains(identifier) || !toggle.hasKey(identifier));
	}
	
	public static void setToggleState(NBTTagCompound nbt, String identifier, boolean value) {
		if (!toggleable.contains(identifier)
				|| !(TinkerUtil.hasTrait(nbt, identifier) || TinkerUtil.hasModifier(nbt, identifier))) return;
		NBTTagCompound toggle = TagUtil.getTagSafe(nbt, "PlusTiC_toggle");
		if (value) {
			toggle.removeTag(identifier);
		} else {
			toggle.setBoolean(identifier, false);
		}
		nbt.setTag("PlusTiC_toggle", toggle);
	}
}
