package uk.kihira.playerrugs;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import uk.kihira.playerrugs.common.PlayerRugCommand;
import uk.kihira.playerrugs.common.blocks.PlayerRugBlock;
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
        GameRegistry.registerBlock(playerRugBlock = new PlayerRugBlock(), "playerRug");
        GameRegistry.registerTileEntity(PlayerRugTE.class, "playerRug");

        proxy.registerRenderers();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent e) {
        e.registerServerCommand(new PlayerRugCommand());
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent e) {
        if (e.entityLiving instanceof EntityPlayer && e.source == DamageSource.anvil) {
            EntityPlayer player = (EntityPlayer) e.entityLiving;
            ItemStack itemStack = new ItemStack(playerRugBlock);
            NBTTagCompound tagCompound = new NBTTagCompound();
            NBTTagCompound playerProfileTag = new NBTTagCompound();
            NBTUtil.func_152460_a(playerProfileTag, player.getGameProfile());
            tagCompound.setTag("PlayerProfile", playerProfileTag);
            itemStack.setTagCompound(tagCompound);
            player.worldObj.spawnEntityInWorld(new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, itemStack));
        }
    }
}
