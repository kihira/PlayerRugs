package uk.kihira.playerrugs;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import uk.kihira.playerrugs.common.PlayerRugCommand;
import uk.kihira.playerrugs.common.PlayerRugRecipe;
import uk.kihira.playerrugs.common.blocks.PlayerRugBlock;
import uk.kihira.playerrugs.common.items.PlayerRugItem;
import uk.kihira.playerrugs.common.tileentities.PlayerRugTE;
import uk.kihira.playerrugs.proxy.CommonProxy;

@Mod(name = "PlayerRugs", modid = "playerrugs", version = "0.0.1")
public class PlayerRugs {

    public PlayerRugBlock playerRugBlock;

    @SidedProxy(clientSide = "uk.kihira.playerrugs.proxy.ClientProxy", serverSide = "uk.kihira.playerrugs.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static PlayerRugs INSTANCE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        GameRegistry.registerBlock(playerRugBlock = new PlayerRugBlock(), PlayerRugItem.class, "playerRug");
        GameRegistry.registerTileEntity(PlayerRugTE.class, "playerRug");

        MinecraftForge.EVENT_BUS.register(this);

        GameRegistry.addRecipe(new PlayerRugRecipe());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.registerRenderers();
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent e) {
        e.registerServerCommand(new PlayerRugCommand());
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent e) {
        if (e.entityLiving instanceof EntityPlayer && e.source == DamageSource.anvil) {
            EntityPlayer player = (EntityPlayer) e.entityLiving;
            ItemStack itemStack = getPlayerRugStack(player.getGameProfile());
            player.worldObj.spawnEntityInWorld(new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, itemStack));
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
}
