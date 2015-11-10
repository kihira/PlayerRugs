package uk.kihira.playerrugs;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
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
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent e) {
        e.registerServerCommand(new PlayerRugCommand());
    }
}
