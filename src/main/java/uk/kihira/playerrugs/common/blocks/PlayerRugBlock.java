package uk.kihira.playerrugs.common.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import uk.kihira.playerrugs.PlayerRugs;
import uk.kihira.playerrugs.common.tileentities.PlayerRugTE;

public class PlayerRugBlock extends BlockContainer {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool STANDING = PropertyBool.create("standing");

    private static final AxisAlignedBB STANDING_EAST = new AxisAlignedBB(0, 0, 4f/16f, 1f/16f, 1, 12f/16f);
    private static final AxisAlignedBB STANDING_WEST = new AxisAlignedBB(15f/16f, 0, 4f/16f, 1f, 1, 12f/16f);
    private static final AxisAlignedBB STANDING_NORTH = new AxisAlignedBB(4f/16f, 0, 15f/16f, 12f/16f, 1, 1f);
    private static final AxisAlignedBB STANDING_SOUTH = new AxisAlignedBB(4f/16f, 0, 0, 12f/16f, 1, 1f/16f);
    private static final AxisAlignedBB FACING_EAST_WEST = new AxisAlignedBB(0, 0, 0.2f, 1, 1f/16f, 0.8f);
    private static final AxisAlignedBB FACING_NORTH_SOUTH = new AxisAlignedBB(0.2f, 0, 0, 0.8f, 1f/16f, 1);

    public PlayerRugBlock() {
        super(Material.CLOTH);
        setUnlocalizedName("playerRug");
        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(STANDING, false));
        setCreativeTab(CreativeTabs.DECORATIONS);
        setSoundType(SoundType.CLOTH);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
        // State is already set in onBlockPlaced so we don't need to change the state here
        if (stack.hasTagCompound()) {
            ((PlayerRugTE) world.getTileEntity(pos)).setPlayerProfile(NBTUtil.readGameProfileFromNBT(stack.getSubCompound("PlayerProfile", false)));
        }
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
        if (te instanceof PlayerRugTE) {
            spawnAsEntity(worldIn, pos, PlayerRugs.INSTANCE.getPlayerRugStack(((PlayerRugTE) te).getPlayerProfile()));
        }
        else super.harvestBlock(worldIn, player, pos, state, te, stack);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (state.getValue(STANDING)) {
            switch (state.getValue(FACING)) {
                case NORTH:
                    return STANDING_NORTH;
                case SOUTH:
                    return STANDING_SOUTH;
                case WEST:
                    return STANDING_WEST;
                case EAST:
                    return STANDING_EAST;
            }
        }
        else {
            return state.getValue(FACING).getHorizontalIndex() % 2 != 0 ? FACING_EAST_WEST : FACING_NORTH_SOUTH;
        }
        return super.getBoundingBox(state, source, pos);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
        float xLength = 0f;
        float zLength = 0f;
        float xOffset = 0f;
        float zOffset = 0f;
        float yOffset = -7.5f/16f;
        float yLength = 1f;
        if (state.getValue(STANDING)) {
            yLength = 24f;
            yOffset = -12f/16f;
        }
        switch (state.getValue(FACING).getHorizontalIndex() + (state.getValue(STANDING) ? 4 : 0)){
            case 0:
                xLength = 8f;
                zLength = 24f;
                zOffset -= 5f/16f;
                break;
            case 1:
                xLength = 24f;
                zLength = 8f;
                xOffset += 5f/16f;
                break;
            case 2:
                xLength = 8f;
                zLength = 24f;
                zOffset += 5f/16f;
                break;
            case 3:
                xLength = 24f;
                zLength = 8f;
                xOffset -= 5f/16f;
                break;
            case 4:
                xLength = 8f;
                zLength = 1f;
                zOffset -= 7.5f/16f;
                break;
            case 5:
                xLength = 1f;
                zLength = 8f;
                xOffset += 7.5f/16f;
                break;
            case 6:
                xLength = 8f;
                zLength = 1f;
                zOffset += 7.5f/16f;
                break;
            case 7:
                xLength = 1f;
                zLength = 8f;
                xOffset -= 7.5f/16f;
                break;
        }
        zOffset += pos.getZ()+0.5f;
        xOffset += pos.getX()+0.5f;
        yOffset += pos.getY()+0.5f;
        return new AxisAlignedBB(xOffset-(xLength/2f)/16f, yOffset-(yLength/2f)/16f, zOffset-(zLength/2f)/16f, xOffset+(xLength/2f)/16f, yOffset+(yLength/2f)/16f, zOffset+(zLength/2f)/16f);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return PlayerRugs.INSTANCE.getPlayerRugStack(((PlayerRugTE) world.getTileEntity(pos)).getPlayerProfile());
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(STANDING, facing.getAxis() != EnumFacing.Axis.Y);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % 4)).withProperty(STANDING, meta >= 4);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex() + (state.getValue(STANDING) ? 4 : 0);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, STANDING);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new PlayerRugTE();
    }
}
