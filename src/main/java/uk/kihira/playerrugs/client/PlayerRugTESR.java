package uk.kihira.playerrugs.client;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import uk.kihira.playerrugs.common.tileentities.PlayerRugTE;

public class PlayerRugTESR extends TileEntitySpecialRenderer {

    public EnhancedSkullModel headModel = new EnhancedSkullModel(0, 0, 64, 32);

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double xPos, double yPos, double zPos, float partialTicks) {
        GameProfile profile = ((PlayerRugTE) tileEntity).playerProfile;
        ResourceLocation playerSkin = AbstractClientPlayer.locationStevePng;
        float angle = tileEntity.getBlockMetadata()*-90f;
        boolean standing = tileEntity.getBlockMetadata() >= 4;

        if (profile != null) {
/*            SkinManager skinManager = Minecraft.getMinecraft().func_152342_ad();
            Map skinMap = skinManager.func_152788_a(profile);

            // Attempt to load players skin if it is loaded or exists
            if (skinMap.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                playerSkin = skinManager.func_152792_a((MinecraftProfileTexture) skinMap.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
            }*/
            playerSkin = AbstractClientPlayer.getLocationSkin(profile.getName());
            AbstractClientPlayer.getDownloadImageSkin(playerSkin, profile.getName());
        }
        bindTexture(playerSkin);

        // Render head
        GL11.glPushMatrix();
        GL11.glTranslated(xPos + 0.5f, yPos + (standing ? 0.4999f: 0f), zPos + 0.5f);
        GL11.glRotatef(angle, 0f, 1f, 0f);
        GL11.glTranslated(0, 0, standing ? 8f/16f: -9f/16f);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        headModel.render(null, 0.0F, 0.0F, 0.0F, 0f, 0.0F, 0.0625f);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslated(xPos+0.5f, yPos+0.001d, zPos+0.5f);
        GL11.glRotatef(angle, 0, 1, 0);
        float texHeight = 32;
        float texWidth = 64;
        Tessellator tess = Tessellator.instance;
        RenderHelper.disableStandardItemLighting();

        if (standing) {
            GL11.glRotatef(90f, 1f, 0f, 0f);
            GL11.glTranslatef(0f, 7f/16f, -1f/16f);
        }

        // Left Arm
        float xOffset = 4f/16f-0.5f;
        float zOffset = 5f/16f-0.5f;
        float thickness = 1f/16f;
        float yOffset = 1f/16f;
        tess.startDrawingQuads();
        if (standing) {
            xOffset = -0.5f;
            zOffset = 1f/16f-0.5f;
            buildBodyPart(xOffset, yOffset, zOffset, 4f/16f, thickness, 12f/16f, 44f/texWidth, 20f/texHeight, 48f/ texWidth, 32f/texHeight, texWidth, texHeight);
        }
        else {
            buildBodyPart(xOffset, yOffset, zOffset, -12f/16f, thickness, -4f/16f, 52f/ texWidth, 20f/texHeight, 56f/texWidth, 32f/texHeight, texWidth, texHeight);
        }

        // Right Arm
        xOffset = 12f/16f-0.5f;
        zOffset = 1f/16f-0.5f;
        if (standing) {
            buildBodyPart(xOffset, yOffset, zOffset, 4f/16f, thickness, 12f/16f, 48f/texWidth, 20f/texHeight, 52f/texWidth, 32f/texHeight, texWidth, texHeight);
        }
        else {
            buildBodyPart(xOffset, yOffset, zOffset, 12f/16f, thickness, 4f/16f, 56f/texWidth, 20f/texHeight, 52f/texWidth, 32f/texHeight, texWidth, texHeight);
        }

        // Body
        xOffset = 0.25f-0.5f;
        zOffset = 1f/16f-0.5f;
        buildBodyPart(xOffset, yOffset, zOffset, 8f/16f, thickness, 12f/16f, (standing ? 20f : 32f)/texWidth, 20f/texHeight, (standing ? 28f : 40f)/texWidth, 32f/texHeight, texWidth, texHeight);

        // Left Leg
        xOffset = 0.25f-0.5f;
        zOffset = 13f/16f-0.5f;
        buildBodyPart(xOffset, yOffset, zOffset, 4f/16f, thickness, 12f/16f, (standing ? 8f : 16f)/texWidth, 20f/texHeight, (standing ? 4f : 12f)/texWidth, 32f/texHeight, texWidth, texHeight);

        // Right Leg
        xOffset = 0.5f-0.5f;
        zOffset = 13f/16f-0.5f;
        buildBodyPart(xOffset, yOffset, zOffset, 4f/16f, thickness, 12f/16f, (standing ? 4f : 12f)/texWidth, 20f/texHeight, (standing ? 8f : 16f)/texWidth, 32f/texHeight, texWidth, texHeight);

        tess.draw();
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
    }

    private void buildBodyPart(float xPos, float yPos, float zPos, float width, float depth, float length, float minU, float minV, float maxU, float maxV, float texWidth, float texHeight) {
        Tessellator tess = Tessellator.instance;
        float texDepth = depth*16f;
        // This if is used if texture should be rotated as width would be longer then length (used for arms)
        if (Math.abs(width) > Math.abs(length)) {
            // Draws base texture
            tess.addVertexWithUV(xPos, yPos, zPos, minU, minV);
            tess.addVertexWithUV(xPos, yPos, zPos+length, maxU, minV);
            tess.addVertexWithUV(xPos+width, yPos, zPos+length, maxU, maxV);
            tess.addVertexWithUV(xPos+width, yPos, zPos, minU, maxV);

            tess.addVertexWithUV(xPos+width, yPos-depth, zPos, minU, maxV);
            tess.addVertexWithUV(xPos+width, yPos-depth, zPos+length, maxU, maxV);
            tess.addVertexWithUV(xPos, yPos-depth, zPos+length, maxU, minV);
            tess.addVertexWithUV(xPos, yPos-depth, zPos, minU, minV);

            // Draws sides
            tess.addVertexWithUV(xPos, yPos-depth, zPos, minU, minV+(texDepth/texHeight));
            tess.addVertexWithUV(xPos, yPos-depth, zPos+length, maxU, minV+(texDepth/texHeight));
            tess.addVertexWithUV(xPos, yPos, zPos+length, maxU, minV);
            tess.addVertexWithUV(xPos, yPos, zPos, minU, minV);

            float uUpper = maxU + ((minU > maxU) ? (texDepth/texWidth) : -(texDepth/texWidth));
            tess.addVertexWithUV(xPos, yPos-depth, zPos+length, uUpper, minV);
            tess.addVertexWithUV(xPos+width, yPos-depth, zPos+length, uUpper, maxV);
            tess.addVertexWithUV(xPos+width, yPos, zPos+length, maxU, maxV);
            tess.addVertexWithUV(xPos, yPos, zPos+length, maxU, minV);

            tess.addVertexWithUV(xPos+width, yPos-depth, zPos+length, maxU, maxV-(texDepth/texHeight));
            tess.addVertexWithUV(xPos+width, yPos-depth, zPos, minU, maxV-(texDepth/texHeight));
            tess.addVertexWithUV(xPos+width, yPos, zPos, minU, maxV);
            tess.addVertexWithUV(xPos+width, yPos, zPos+length, maxU, maxV);

            uUpper = minU + ((minU < maxU) ? (texDepth/texWidth) : -(texDepth/texWidth));
            tess.addVertexWithUV(xPos+width, yPos-depth, zPos, minU, maxV);
            tess.addVertexWithUV(xPos, yPos-depth, zPos, minU, minV);
            tess.addVertexWithUV(xPos, yPos, zPos, uUpper, minV);
            tess.addVertexWithUV(xPos+width, yPos, zPos, uUpper, maxV);
        }
        else {
            // Draws base texture
            tess.addVertexWithUV(xPos, yPos, zPos, minU, minV);
            tess.addVertexWithUV(xPos, yPos, zPos+length, minU, maxV);
            tess.addVertexWithUV(xPos+width, yPos, zPos+length, maxU, maxV);
            tess.addVertexWithUV(xPos+width, yPos, zPos, maxU, minV);

            tess.addVertexWithUV(xPos+width, yPos-depth, zPos, maxU, minV);
            tess.addVertexWithUV(xPos+width, yPos-depth, zPos+length, maxU, maxV);
            tess.addVertexWithUV(xPos, yPos-depth, zPos+length, minU, maxV);
            tess.addVertexWithUV(xPos, yPos-depth, zPos, minU, minV);

            // Draws sides
            float uUpper = minU + ((minU < maxU) ? (texDepth/texWidth) : -(texDepth/texWidth));
            tess.addVertexWithUV(xPos, yPos-depth, zPos, minU, minV);
            tess.addVertexWithUV(xPos, yPos-depth, zPos+length, minU, maxV);
            tess.addVertexWithUV(xPos, yPos, zPos+length, uUpper, maxV);
            tess.addVertexWithUV(xPos, yPos, zPos, uUpper, minV);

            tess.addVertexWithUV(xPos, yPos-depth, zPos+length, minU, maxV);
            tess.addVertexWithUV(xPos+width, yPos-depth, zPos+length, maxU, maxV);
            tess.addVertexWithUV(xPos+width, yPos, zPos+length, maxU, maxV-(texDepth/texHeight));
            tess.addVertexWithUV(xPos, yPos, zPos+length, minU, maxV-(texDepth/texHeight));

            tess.addVertexWithUV(xPos+width, yPos-depth, zPos+length, maxU, maxV);
            tess.addVertexWithUV(xPos+width, yPos-depth, zPos, maxU, minV);
            tess.addVertexWithUV(xPos+width, yPos, zPos, maxU-(texDepth/texWidth), minV);
            tess.addVertexWithUV(xPos+width, yPos, zPos+length, maxU-(texDepth/texWidth), maxV);

            tess.addVertexWithUV(xPos+width, yPos-depth, zPos, minU, minV+(texDepth/texHeight));
            tess.addVertexWithUV(xPos, yPos-depth, zPos, maxU, minV+(texDepth/texHeight));
            tess.addVertexWithUV(xPos, yPos, zPos, maxU, minV);
            tess.addVertexWithUV(xPos+width, yPos, zPos, minU, minV);
        }
    }
}
