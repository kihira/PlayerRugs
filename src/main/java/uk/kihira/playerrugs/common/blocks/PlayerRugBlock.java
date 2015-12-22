package uk.kihira.playerrugs.common.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import uk.kihira.playerrugs.PlayerRugs;
import uk.kihira.playerrugs.common.tileentities.PlayerRugTE;

import java.util.ArrayList;
import java.util.List;

public class PlayerRugBlock extends BlockContainer {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool STANDING = PropertyBool.create("standing");

    public PlayerRugBlock() {
        super(Material.cloth);
        setUnlocalizedName("playerRug");
        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(STANDING, false));
        setCreativeTab(CreativeTabs.tabBlock);
        setStepSound(soundTypeCloth);
    }

    @Override
    public int getRenderType() {
        return 2;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
        // Set rotation
        EnumFacing enumfacing = entity.getHorizontalFacing().getOpposite();
        state = state.withProperty(FACING, enumfacing);
        state = state.withProperty(STANDING, false);
        world.setBlockState(pos, state, 3);

        if (stack.hasTagCompound()) {
            ((PlayerRugTE) world.getTileEntity(pos)).setPlayerProfile(NBTUtil.readGameProfileFromNBT(stack.getSubCompound("PlayerProfile", false)));
        }
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(PlayerRugs.INSTANCE.getPlayerRugStack(((PlayerRugTE) world.getTileEntity(pos)).getPlayerProfile()));
        return drops;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state) {
        setBlockBoundsBasedOnState(world, pos);
        return super.getCollisionBoundingBox(world, pos, state);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock().isAir(world, pos)) {
            return;
        }
        if (state.getPropertyNames().contains(STANDING) && state.getValue(STANDING)) {
            minY = 0;
            maxY = 1f;
            if (state.getValue(FACING).getIndex() % 2 != 0) {
                if (state.getValue(FACING).getIndex() == 5) {
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
                if (state.getValue(FACING).getIndex() == 6) {
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
            if (state.getValue(FACING).getIndex() % 2 != 0) {
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
    public AxisAlignedBB getSelectedBoundingBox(World world, BlockPos pos) {
        float xLength = 0f;
        float zLength = 0f;
        float xOffset = 0f;
        float zOffset = 0f;
        float yOffset = -7.5f/16f;
        float yLength = 1f;
        //int meta = world.getBlockMetadata(xPos, yPos, zPos);
        IBlockState state = world.getBlockState(pos);
        if (state.getValue(STANDING)) {
            yLength = 12f;
            yOffset = -6f/16f;
        }
        switch (state.getValue(FACING).getIndex()){
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
        zOffset += pos.getZ()+0.5f;
        xOffset += pos.getX()+0.5f;
        yOffset += pos.getY()+0.5f;
        return AxisAlignedBB.fromBounds(xOffset-(xLength/2f)/16f, yOffset-(yLength/2f)/16f, zOffset-(zLength/2f)/16f, xOffset+(xLength/2f)/16f, yOffset+(yLength/2f)/16f, zOffset+(zLength/2f)/16f);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player) {
        return PlayerRugs.INSTANCE.getPlayerRugStack(((PlayerRugTE) world.getTileEntity(pos)).getPlayerProfile());
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(STANDING, facing.getAxis() == EnumFacing.Axis.Y);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }

        return getDefaultState().withProperty(FACING, enumfacing).withProperty(STANDING, meta >= 4);
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex() + (state.getValue(STANDING) ? 4 : 0);
    }

    protected BlockState createBlockState() {
        return new BlockState(this, FACING, STANDING);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new PlayerRugTE();
    }
}
