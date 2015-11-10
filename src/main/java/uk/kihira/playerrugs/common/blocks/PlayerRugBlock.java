package uk.kihira.playerrugs.common.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import uk.kihira.playerrugs.common.tileentities.PlayerRugTE;

public class PlayerRugBlock extends BlockContainer {

    public PlayerRugBlock() {
        super(Material.cloth);
        setBlockName("playerRug");
        setCreativeTab(CreativeTabs.tabBlock);
        setStepSound(soundTypeCloth);
        setBlockTextureName("stone");
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, int xPos, int yPos, int zPos, EntityLivingBase entity, ItemStack itemStack) {
        if (itemStack.hasTagCompound()) {
            ((PlayerRugTE) world.getTileEntity(xPos, yPos, zPos)).playerProfile = NBTUtil.func_152459_a(itemStack.getTagCompound().getCompoundTag("PlayerProfile"));
        }

        // Set rotation
        int rot = MathHelper.floor_float(entity.rotationYaw*4f/360f+0.5f) & 3;
        world.setBlockMetadataWithNotify(xPos, yPos, zPos, rot, 3);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int xPos, int yPos, int zPos) {
        if (world.getBlockMetadata(xPos, yPos, zPos) % 2 != 0) {
            minX = 0f;
            minZ = 0.2f;
            maxX = 1f;
            maxZ = 0.8f;
        }
        else {
            minX = 0.2f;
            minZ = 0f;
            maxX = 0.8f;
            maxZ = 1f;
        }
        minY = 0;
        maxY = 1f/16f;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int xPos, int yPos, int zPos) {
        float xLength = 0f;
        float zLength = 0f;
        float xOffset = 0f;
        float zOffset = 0f;
        switch (world.getBlockMetadata(xPos, yPos, zPos)){
            case 0:
                xLength = 8f;
                zLength = 24f;
                zOffset += 5f/16f;
                break;
            case 1:
                xLength = 24f;
                zLength = 8f;
                xOffset -= 5f/16f;
                break;
            case 2:
                xLength = 8f;
                zLength = 24f;
                zOffset -= 5f/16f;
                break;
            case 3:
                xLength = 24f;
                zLength = 8f;
                xOffset += 5f/16f;
                break;
        }
        xLength *= 1.2f;
        zLength *= 1.2f;
        zOffset *= 1.2f;
        xOffset *= 1.2f;
        zOffset += zPos+0.5f;
        xOffset += xPos+0.5f;
        return AxisAlignedBB.getBoundingBox(xOffset-(xLength/2f)/16f, yPos, zOffset-(zLength/2f)/16f, xOffset+(xLength/2f)/16f, yPos+1f/16f, zOffset+(zLength/2f)/16f);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new PlayerRugTE();
    }
}
