package uk.kihira.playerrugs.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import uk.kihira.playerrugs.PlayerRugs;

import java.util.Set;

public class PlayerRugsGuiFactory implements IModGuiFactory {

    @Override
    public void initialize(Minecraft minecraftInstance) {}

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return PlayerRugsConfigGui.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }

    public static class PlayerRugsConfigGui extends GuiConfig {

        @SuppressWarnings("unchecked")
        public PlayerRugsConfigGui(GuiScreen parentScreen) {
            super(parentScreen, new ConfigElement(PlayerRugs.INSTANCE.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
                    "playerrugs", false, false, GuiConfig.getAbridgedConfigPath(PlayerRugs.INSTANCE.config.toString()));
        }
    }
}
