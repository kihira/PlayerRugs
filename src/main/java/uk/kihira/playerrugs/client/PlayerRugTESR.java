package uk.kihira.playerrugs.client;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelHumanoidHead;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import uk.kihira.playerrugs.common.blocks.PlayerRugBlock;
import uk.kihira.playerrugs.common.tileentities.PlayerRugTE;

import java.util.Map;
import java.util.UUID;

public class PlayerRugTESR extends TileEntitySpecialRenderer<PlayerRugTE> {

    public ModelHumanoidHead headModel = new ModelHumanoidHead();
    public static final float pWidth = 14/16f; // Width of a pixel in game units

    @Override
    public void renderTileEntityAt(PlayerRugTE tileEntity, double xPos, double yPos, double zPos, float partialTicks, int destroyStage) {
        GameProfile profile;
        ResourceLocation playerSkin = DefaultPlayerSkin.getDefaultSkinLegacy();
        IBlockState state;
        float angle;
        boolean standing;
        boolean slimModel = false;

        // Fix for inventory rendering
        if (tileEntity == null) {
            profile = null;
            angle = 0;
            standing = false;
        }
        else {
            profile = tileEntity.getPlayerProfile();
            state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
            angle = (state.getValue(PlayerRugBlock.FACING).getHorizontalIndex()+2)*-90f;
            standing = state.getValue(PlayerRugBlock.STANDING);

            // Darken light map
            int i = tileEntity.getWorld().getCombinedLight(tileEntity.getPos(), 0);
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F * 0.95f, (float)k / 1.0F * 0.95f);
        }

        if (profile != null) {
            Minecraft minecraft = Minecraft.getMinecraft();
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(profile);

            if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                playerSkin = minecraft.getSkinManager().loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
                String meta = map.get(MinecraftProfileTexture.Type.SKIN).getMetadata("model");
                slimModel = meta != null && meta.equals("slim");
            }
            else {
                UUID uuid = EntityPlayer.getUUID(profile);
                playerSkin = DefaultPlayerSkin.getDefaultSkin(uuid);
                slimModel = DefaultPlayerSkin.getSkinType(uuid).equals("slim");
            }
        }

        // Render head
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.disableCull();
        GlStateManager.disableLighting();

        GlStateManager.translate(xPos + 0.5f, yPos + (standing ? 0.4999f: 0f), zPos + 0.5f);
        GlStateManager.rotate(angle, 0f, 1f, 0f);
        GlStateManager.translate(0, 0, standing ? 8f/16f: -9f/16f);
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);

        bindTexture(playerSkin);
        headModel.render(null, 0.0F, 0.0F, 0.0F, 0f, 0.0F, 0.0625f);

        GlStateManager.disableRescaleNormal();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(xPos+0.5f, yPos+0.001d, zPos+0.5f);
        GlStateManager.rotate(angle, 0, 1, 0);

        Tessellator tess = Tessellator.getInstance();
        VertexBuffer wr = tess.getBuffer();
        RenderHelper.disableStandardItemLighting();

        if (standing) {
            GlStateManager.rotate(90f, 1f, 0f, 0f);
            GlStateManager.translate(0f, 7f/16f, -1f/16f);
        }

        float texHeight = 64;
        float texWidth = 64;
        float xOffset = 4f/16f-0.5f;
        float zOffset = 5f/16f-0.5f;
        float thickness = 1f/16f;
        float yOffset = 1f/16f;
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        // Left Arm
        if (standing) {
            xOffset = -0.5f;
            zOffset = 1f/16f-0.5f;
            buildBodyPart(
                    xOffset+(slimModel?1f/16f:0f), yOffset, zOffset,
                    (slimModel?3f:4f)/16f, thickness, 12f/16f,
                    (slimModel?39f:40f)/texWidth, 52f/texHeight, 36f/texWidth, 64f/texHeight,
                    texWidth, texHeight);
        }
        else {
            buildBodyPart(
                    xOffset, yOffset, zOffset-(slimModel?1f/16f:0f),
                    -12f/16f, thickness, -(slimModel?3f:4f)/16f,
                    (slimModel?46f:48f)/texWidth, 52f/texHeight, (slimModel?43f:44f)/texWidth, 64f/texHeight,
                    texWidth, texHeight);
        }

        // Right Arm
        xOffset = 12f/16f-0.5f;
        zOffset = 1f/16f-0.5f;
        if (standing) {
            buildBodyPart(
                    xOffset, yOffset, zOffset,
                    (slimModel?3f:4f)/16f, thickness, 12f/16f,
                    (slimModel?47f:48f)/texWidth, 20f/texHeight, 44f/texWidth, 32f/texHeight,
                    texWidth, texHeight);
        }
        else {
            buildBodyPart(
                    xOffset, yOffset, zOffset,
                    12f/16f, thickness, (slimModel?3f:4f)/16f,
                    (slimModel?54f:56f)/texWidth, 20f/texHeight, (slimModel?51f:52f)/texWidth, 32f/texHeight,
                    texWidth, texHeight);
        }

        // Body
        xOffset = 0.25f-0.5f;
        zOffset = 1f/16f-0.5f;
        buildBodyPart(
                xOffset, yOffset, zOffset,
                8f/16f, thickness, 12f/16f,
                (standing?28f:32f)/texWidth, 20f/texHeight, (standing?20f:40f)/texWidth, 32f/texHeight,
                texWidth, texHeight);

        // Left Leg
        xOffset = 0.25f-0.5f;
        zOffset = 13f/16f-0.5f;
        buildBodyPart(
                xOffset, yOffset, zOffset,
                4f/16f, thickness, 12f/16f,
                (standing?20f:28f)/texWidth, 52f/texHeight, (standing?24f:32f)/texWidth, 64f/texHeight,
                texWidth, texHeight);

        // Right Leg
        xOffset = 0.5f-0.5f;
        zOffset = 13f/16f-0.5f;
        buildBodyPart(
                xOffset, yOffset, zOffset,
                4f/16f, thickness, 12f/16f,
                (standing ? 4f : 12f)/texWidth, 20f/texHeight, (standing ? 8f : 16f)/texWidth, 32f/texHeight,
                texWidth, texHeight);

        tess.draw();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    private void buildBodyPart(float xPos, float yPos, float zPos, float width, float depth, float length, float minU, float minV, float maxU, float maxV, float texWidth, float texHeight) {
        VertexBuffer wr = Tessellator.getInstance().getBuffer();
        float texDepth = depth*16f;
        // This if is used if texture should be rotated as width would be longer then length (used for arms)
        if (Math.abs(width) > Math.abs(length)) {
            // Draws base texture
            addVertexWithUV(wr, xPos, yPos, zPos, minU, minV);
            addVertexWithUV(wr, xPos, yPos, zPos+length, maxU, minV);
            addVertexWithUV(wr, xPos+width, yPos, zPos+length, maxU, maxV);
            addVertexWithUV(wr, xPos+width, yPos, zPos, minU, maxV);

            addVertexWithUV(wr, xPos+width, yPos-depth, zPos, minU, maxV);
            addVertexWithUV(wr, xPos+width, yPos-depth, zPos+length, maxU, maxV);
            addVertexWithUV(wr, xPos, yPos-depth, zPos+length, maxU, minV);
            addVertexWithUV(wr, xPos, yPos-depth, zPos, minU, minV);

            // Draws sides
            addVertexWithUV(wr, xPos, yPos-depth, zPos, minU, minV+(texDepth/texHeight));
            addVertexWithUV(wr, xPos, yPos-depth, zPos+length, maxU, minV+(texDepth/texHeight));
            addVertexWithUV(wr, xPos, yPos, zPos+length, maxU, minV);
            addVertexWithUV(wr, xPos, yPos, zPos, minU, minV);

            float uUpper = maxU + ((minU > maxU) ? (texDepth/texWidth) : -(texDepth/texWidth));
            addVertexWithUV(wr, xPos, yPos-depth, zPos+length, uUpper, minV);
            addVertexWithUV(wr, xPos+width, yPos-depth, zPos+length, uUpper, maxV);
            addVertexWithUV(wr, xPos+width, yPos, zPos+length, maxU, maxV);
            addVertexWithUV(wr, xPos, yPos, zPos+length, maxU, minV);

            addVertexWithUV(wr, xPos+width, yPos-depth, zPos+length, maxU, maxV-(texDepth/texHeight));
            addVertexWithUV(wr, xPos+width, yPos-depth, zPos, minU, maxV-(texDepth/texHeight));
            addVertexWithUV(wr, xPos+width, yPos, zPos, minU, maxV);
            addVertexWithUV(wr, xPos+width, yPos, zPos+length, maxU, maxV);

            uUpper = minU + ((minU < maxU) ? (texDepth/texWidth) : -(texDepth/texWidth));
            addVertexWithUV(wr, xPos+width, yPos-depth, zPos, minU, maxV);
            addVertexWithUV(wr, xPos, yPos-depth, zPos, minU, minV);
            addVertexWithUV(wr, xPos, yPos, zPos, uUpper, minV);
            addVertexWithUV(wr, xPos+width, yPos, zPos, uUpper, maxV);
        }
        else {
            // Draws base texture
            addVertexWithUV(wr, xPos, yPos, zPos, minU, minV);
            addVertexWithUV(wr, xPos, yPos, zPos+length, minU, maxV);
            addVertexWithUV(wr, xPos+width, yPos, zPos+length, maxU, maxV);
            addVertexWithUV(wr, xPos+width, yPos, zPos, maxU, minV);

            addVertexWithUV(wr, xPos+width, yPos-depth, zPos, maxU, minV);
            addVertexWithUV(wr, xPos+width, yPos-depth, zPos+length, maxU, maxV);
            addVertexWithUV(wr, xPos, yPos-depth, zPos+length, minU, maxV);
            addVertexWithUV(wr, xPos, yPos-depth, zPos, minU, minV);

            // Draws sides
            float uUpper = minU + ((minU < maxU) ? (texDepth/texWidth) : -(texDepth/texWidth));
            addVertexWithUV(wr, xPos, yPos-depth, zPos, minU, minV);
            addVertexWithUV(wr, xPos, yPos-depth, zPos+length, minU, maxV);
            addVertexWithUV(wr, xPos, yPos, zPos+length, uUpper, maxV);
            addVertexWithUV(wr, xPos, yPos, zPos, uUpper, minV);

            addVertexWithUV(wr, xPos, yPos-depth, zPos+length, minU, maxV);
            addVertexWithUV(wr, xPos+width, yPos-depth, zPos+length, maxU, maxV);
            addVertexWithUV(wr, xPos+width, yPos, zPos+length, maxU, maxV-(texDepth/texHeight));
            addVertexWithUV(wr, xPos, yPos, zPos+length, minU, maxV-(texDepth/texHeight));

            addVertexWithUV(wr, xPos+width, yPos-depth, zPos+length, maxU, maxV);
            addVertexWithUV(wr, xPos+width, yPos-depth, zPos, maxU, minV);
            addVertexWithUV(wr, xPos+width, yPos, zPos, maxU-(texDepth/texWidth), minV);
            addVertexWithUV(wr, xPos+width, yPos, zPos+length, maxU-(texDepth/texWidth), maxV);

            addVertexWithUV(wr, xPos+width, yPos-depth, zPos, maxU, minV);
            addVertexWithUV(wr, xPos, yPos-depth, zPos, minU, minV);
            addVertexWithUV(wr, xPos, yPos, zPos, minU, minV+(texDepth/texHeight));
            addVertexWithUV(wr, xPos+width, yPos, zPos, maxU, minV+(texDepth/texHeight));
        }
    }
    
    private static void addVertexWithUV(VertexBuffer wr, float x, float y, float z, float u, float v) {
        wr.pos(x, y, z).tex(u, v).endVertex();
    }
}
