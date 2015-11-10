package uk.kihira.playerrugs.common.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import uk.kihira.playerrugs.common.tileentities.PlayerRugTE;

public class PlayerRugBlock extends BlockContainer {

    public PlayerRugBlock() {
        super(Material.rock);
        setBlockName("playerRug");
        setCreativeTab(CreativeTabs.tabBlock);
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
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, int xPos, int yPos, int zPos, EntityLivingBase entity, ItemStack itemStack) {
        ((PlayerRugTE) world.getTileEntity(xPos, yPos, zPos)).playerProfile = NBTUtil.func_152459_a(itemStack.getTagCompound().getCompoundTag("PlayerProfile"));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new PlayerRugTE();
    }
}
