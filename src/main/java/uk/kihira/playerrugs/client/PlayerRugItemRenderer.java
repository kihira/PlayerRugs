package uk.kihira.playerrugs.client;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import uk.kihira.playerrugs.common.tileentities.PlayerRugTE;

public class PlayerRugItemRenderer implements IItemRenderer {

    private PlayerRugTESR rugRenderer;
    private PlayerRugTE fakeTileEntity;

    public PlayerRugItemRenderer(PlayerRugTESR rugRenderer) {
        this.rugRenderer = rugRenderer;
        fakeTileEntity = new PlayerRugTE();
        fakeTileEntity.blockMetadata = 2;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if (item.hasTagCompound()) {
            fakeTileEntity.playerProfile = NBTUtil.func_152459_a(item.getTagCompound().getCompoundTag("PlayerProfile"));
        }

        switch (type) {
            case INVENTORY:
                GL11.glScalef(0.65f, 0.65f, 0.65f);
                fakeTileEntity.blockMetadata = 2;
                rugRenderer.renderTileEntityAt(fakeTileEntity, 0, 0, 0, 0);
                break;
            case EQUIPPED:
                fakeTileEntity.blockMetadata = 3;
                rugRenderer.renderTileEntityAt(fakeTileEntity, 0.25f, 0.25f, 0.25f, 0);
                break;
            case EQUIPPED_FIRST_PERSON:
                fakeTileEntity.blockMetadata = 0;
                rugRenderer.renderTileEntityAt(fakeTileEntity, 0.25f, 0.5f, 0.25f, 0);
            default:
                fakeTileEntity.blockMetadata = 0;
                rugRenderer.renderTileEntityAt(fakeTileEntity, -0.5f, 0f, -0.5f, 0);
        }
    }
}
