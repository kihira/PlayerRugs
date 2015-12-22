package uk.kihira.playerrugs.common.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import uk.kihira.playerrugs.PlayerRugs;
import uk.kihira.playerrugs.common.tileentities.PlayerRugTE;

import java.util.ArrayList;

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
        world.setBlockMetadataWithNotify(xPos, yPos, zPos, world.getBlockMetadata(xPos, yPos, zPos) + rot, 3);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(PlayerRugs.INSTANCE.getPlayerRugStack(((PlayerRugTE) world.getTileEntity(x, y, z)).playerProfile));
        return drops;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int xPos, int yPos, int zPos) {
        int meta = world.getBlockMetadata(xPos, yPos, zPos);
        if (meta >= 4) {
            minY = 0;
            maxY = 1f;
            if (meta % 2 != 0) {
                if (meta == 5) {
                    minX = 0f;
                    maxX = 1f/16f;
                }
                else {
                    minX = 15f/16f;
                    maxX = 1f;
                }
                minZ = 4f/16f;
                maxZ = 12f/16f;
            }
            else {
                if (meta == 6) {
                    minZ = 0f;
                    maxZ = 1f/16f;
                }
                else {
                    minZ = 15f/16f;
                    maxZ = 1f;
                }
                minX = 4f/16f;
                maxX = 12f/16f;
            }
        }
        else {
            if (meta % 2 != 0) {
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
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int xPos, int yPos, int zPos) {
        float xLength = 0f;
        float zLength = 0f;
        float xOffset = 0f;
        float zOffset = 0f;
        float yOffset = -7.5f/16f;
        float yLength = 1f;
        int meta = world.getBlockMetadata(xPos, yPos, zPos);
        if (meta >= 4) {
            yLength = 12f;
            yOffset = -6f/16f;
        }
        switch (meta){
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
            case 4:
                xLength = 8f;
                zLength = 1f;
                zOffset += 7.5f/16f;
                break;
            case 5:
                xLength = 1f;
                zLength = 8f;
                xOffset -= 7.5f/16f;
                break;
            case 6:
                xLength = 8f;
                zLength = 1f;
                zOffset -= 7.5f/16f;
                break;
            case 7:
                xLength = 1f;
                zLength = 8f;
                xOffset += 7.5f/16f;
                break;
        }
        zOffset += zPos+0.5f;
        xOffset += xPos+0.5f;
        yOffset += yPos+0.5f;
        return AxisAlignedBB.getBoundingBox(xOffset-(xLength/2f)/16f, yOffset-(yLength/2f)/16f, zOffset-(zLength/2f)/16f, xOffset+(xLength/2f)/16f, yOffset+(yLength/2f)/16f, zOffset+(zLength/2f)/16f);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        return PlayerRugs.INSTANCE.getPlayerRugStack(((PlayerRugTE) world.getTileEntity(x, y, z)).playerProfile);
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
        return side >= 2 && side <= 5 ? 4 : 0;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new PlayerRugTE();
    }
}
