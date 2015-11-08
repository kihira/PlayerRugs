package uk.kihira.playerrugs;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.common.MinecraftForge;
import uk.kihira.playerrugs.common.blocks.PlayerRugBlock;
import uk.kihira.playerrugs.common.tileentities.PlayerRugTE;
import uk.kihira.playerrugs.proxy.CommonProxy;

@Mod(name = "PlayerRugs", modid = "playerrugs", version = "0.0.1")
public class PlayerRugs {

    @SidedProxy(clientSide = "uk.kihira.playerrugs.proxy.ClientProxy", serverSide = "uk.kihira.playerrugs.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        GameRegistry.registerBlock(new PlayerRugBlock(), "playerRug");
        GameRegistry.registerTileEntity(PlayerRugTE.class, "playerRug");

        proxy.registerRenderers();
    }
}
