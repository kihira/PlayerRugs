package uk.kihira.playerrugs;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import uk.kihira.playerrugs.common.PlayerRugCommand;
import uk.kihira.playerrugs.common.blocks.PlayerRugBlock;
import uk.kihira.playerrugs.common.items.PlayerRugItem;
import uk.kihira.playerrugs.common.tileentities.PlayerRugTE;
import uk.kihira.playerrugs.proxy.CommonProxy;

@Mod(name = "PlayerRugs", modid = "playerrugs", guiFactory = "uk.kihira.playerrugs.client.PlayerRugsGuiFactory")
public class PlayerRugs {

    public PlayerRugBlock playerRugBlock;

    /*** Config stuff ***/
    public Configuration config;
    private boolean easyCrafting;

    @SidedProxy(clientSide = "uk.kihira.playerrugs.proxy.ClientProxy", serverSide = "uk.kihira.playerrugs.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static PlayerRugs INSTANCE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        GameRegistry.registerBlock(playerRugBlock = new PlayerRugBlock(), PlayerRugItem.class, "playerRug");
        GameRegistry.registerTileEntity(PlayerRugTE.class, "playerRug");

        proxy.registerRenderers();

        MinecraftForge.EVENT_BUS.register(this);

        //GameRegistry.addShapedRecipe(getPlayerRugStack(null), " H ", "LLL", " L ", 'H', new ItemStack(Blocks.skull, 1, 3), 'L', Items.leather);
        GameRegistry.addRecipe(getPlayerRugStack(null), " H ", "LLL", " L ", 'H', new ItemStack(Items.skull, 1, 3), 'L', Items.leather);

        // Load config
        config = new Configuration(e.getSuggestedConfigurationFile());
        config.load();
        loadConfig();
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent e) {
        e.registerServerCommand(new PlayerRugCommand());
    }

    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent e) {
        if (e.getModID().equals("playerrugs")) {
            loadConfig();
        }
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent e) {
        if (e.getEntityLiving() instanceof EntityPlayer && e.getSource() == DamageSource.anvil) {
            EntityPlayer player = (EntityPlayer) e.getEntityLiving();
            ItemStack itemStack = getPlayerRugStack(player.getGameProfile());
            player.worldObj.spawnEntityInWorld(new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, itemStack));
        }
    }

    @SubscribeEvent
    public void onAnvilRepair(AnvilRepairEvent e) {
        ItemStack stack = e.getOutput();
        if (Block.getBlockFromItem(stack.getItem()) instanceof PlayerRugBlock && stack.hasDisplayName()) {
            NBTTagCompound tagCompound = stack.getTagCompound() != null ? stack.getTagCompound() : new NBTTagCompound();
            NBTTagCompound playerTag = stack.getSubCompound("PlayerProfile", true);
            NBTUtil.writeGameProfile(playerTag, e.getEntityPlayer().getServer().getPlayerProfileCache().getGameProfileForUsername(stack.getDisplayName()));
            tagCompound.setTag("PlayerProfile", playerTag);
            e.getOutput().setTagCompound(tagCompound);
        }
    }

    @SubscribeEvent
    public void onCraft(PlayerEvent.ItemCraftedEvent e) {
        if (e.crafting.getItem() == Item.getItemFromBlock(playerRugBlock)) {
            NBTTagCompound tagCompound = e.crafting.hasTagCompound() ? e.crafting.getTagCompound() : new NBTTagCompound();

            tagCompound.setTag("PlayerProfile", e.craftMatrix.getStackInSlot(1).hasTagCompound() ?
                    e.craftMatrix.getStackInSlot(1).getTagCompound().getCompoundTag("SkullOwner") : new NBTTagCompound());
            e.crafting.setTagCompound(tagCompound);
        }
    }

    public ItemStack getPlayerRugStack(GameProfile profile) {
        ItemStack itemStack = new ItemStack(playerRugBlock);
        NBTTagCompound tagCompound = new NBTTagCompound();
        NBTTagCompound playerProfileTag = new NBTTagCompound();

        if (profile != null) {
            NBTUtil.writeGameProfile(playerProfileTag, profile);
        }
        tagCompound.setTag("PlayerProfile", playerProfileTag);
        itemStack.setTagCompound(tagCompound);

        return itemStack;
    }

    private void loadConfig() {
        easyCrafting = config.getBoolean("Easy Crafting", Configuration.CATEGORY_GENERAL, false, "If true, allows rugs to be renamed in anvils to get that players rug");

        config.save();
    }
}
