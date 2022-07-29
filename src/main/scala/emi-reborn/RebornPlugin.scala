package net.bulbyvr.emireborn

import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry 
import dev.emi.emi.api.widget.Widget
import dev.emi.emi.api.widget.AnimatedTextureWidget
import dev.emi.emi.api.widget.DrawableWidget
import dev.emi.emi.api.widget.Bounds
import net.minecraft.client.util.math.MatrixStack
import reborncore.common.powerSystem.PowerSystem
import com.mojang.blaze3d.systems.RenderSystem
import reborncore.client.gui.guibuilder.GuiBuilder
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.util.math.MathHelper
import dev.emi.emi.api.stack.FluidEmiStack
import net.minecraft.fluid.Fluid
import net.minecraft.screen.PlayerScreenHandler
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.tooltip.TooltipComponent
import java.{util => ju}
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import dev.emi.emi.api.stack.FluidEmiStack.FluidEntry
import dev.emi.emi.api.widget.SlotWidget
import net.minecraft.util.math.BlockPos
import net.bulbyvr.emireborn.fluidgenerator.FluidGeneratorCategories
import dev.emi.emi.api.recipe.EmiRecipeCategory
import techreborn.init.ModRecipes
import dev.emi.emi.api.stack.EmiStack
import techreborn.init.TRContent
import net.minecraft.util.Identifier
import net.bulbyvr.emireborn.machines.*
import reborncore.common.crafting.RebornRecipe
import dev.emi.emi.api.recipe.EmiRecipe
import net.minecraft.recipe.RecipeType
import net.bulbyvr.emireborn.fluidreplicator.EmiFluidReplicatorCategories
object RebornPlugin extends EmiPlugin {
  
  override def register(registry: EmiRegistry) = 
    FluidGeneratorCategories.registerFluidGenerator(registry)
    EmiFluidReplicatorCategories.register(registry)
    EmiAssemblingMachineRecipe.register(registry)
    EmiBlastFurnaceRecipe.register(registry)
    EmiDistillationTowerRecipe.register(registry)
    EmiElectrolyzerRecipe.register(registry)
    EmiGrinderRecipe.register(registry)
    EmiImplosionCompressorRecipe.register(registry)
    EmiCentrifugeRecipe.register(registry)
    OneInputOneOutput.register(registry)
    EmiSawmillRecipe.register(registry)
    EmiTwoInputsCenterOutputRecipe.register(registry)
  def createEnergyDisplay(x: Int, y: Int, w: Int, h: Int, energy: Double, animation: EntryAnimation, toolTipBuilder: (Int, Int) => ju.List[TooltipComponent]): EnergyWidget =
    EnergyWidget(x, y, w, h, animation, toolTipBuilder)
  def createFluidDisplay(x: Int, y: Int, w: Int, h: Int, fluid: FluidEmiStack, animation: EntryAnimation): FluidWidget = 
    val widget = FluidWidget(x, y, animation, fluid)
    widget.customBackground(null, 0, 0, w, h)
    widget
  def registerHelper[R <: RebornRecipe](workbenches: EmiStack*)(registry: EmiRegistry, kind: RecipeType[R], category: EmiRecipeCategory, builder: R => EmiRecipe) = 
    registry.addCategory(category) 
    for (workbench <- workbenches) {
      registry.addWorkstation(category, workbench)
    }
    registry.getRecipeManager().listAllOfType(kind).forEach(it => 
      registry.addRecipe(builder(it))
    )
}
object RebornCategories {
  val SCRAPBOX = EmiRecipeCategory(ModRecipes.SCRAPBOX.name, EmiStack.of(TRContent.SCRAP_BOX))
}
class EnergyWidget (val x: Int, val y:Int, val w:Int, val h: Int, val animation: EntryAnimation, val tooltipBuilder: (Int, Int) => ju.List[TooltipComponent]) extends Widget {
    val bounds : Bounds = Bounds(x, y, w, h) 
    override def getBounds() : Bounds = bounds  
    override def render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float): Unit = 
      val innerHeight = h - 2 
      val displayPower = PowerSystem.getDisplayPower()
      RenderSystem.setShaderTexture(0, GuiBuilder.defaultTextureSheet)
      DrawableHelper.drawTexture(matrices, x, y, displayPower.xBar - 15, displayPower.yBar - 1, w, h, 256, 256)
      lazy val fancyHeight = MathHelper.ceil((System.currentTimeMillis() / (animation.duration / innerHeight) % innerHeight))
      val innerDisplayHeight = animation match 
        case EntryAnimation.None =>
          innerHeight 
        case EntryAnimation.Upwards(_)  => 
          fancyHeight 
        case EntryAnimation.Downwards(_) => 
          innerHeight - fancyHeight
      DrawableHelper.drawTexture(matrices, x + 1, y + 1 + innerHeight - innerDisplayHeight, displayPower.xBar, innerHeight + displayPower.yBar - innerDisplayHeight, w - 2, innerDisplayHeight, 256, 256)
    override def getTooltip(mouseX: Int, mouseY: Int): ju.List[TooltipComponent] = tooltipBuilder(mouseX, mouseY)
}

class FluidWidget (x: Int, y:Int, val animation: EntryAnimation, protected val fluid: FluidEmiStack) extends SlotWidget(fluid, x, y) {
  override def render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float): Unit = 
    val h = getBounds.height 
    val w = getBounds.width
    val displayPower = PowerSystem.getDisplayPower()
    RenderSystem.setShaderTexture(0, GuiBuilder.defaultTextureSheet)
    // these values are to prevent jank
    DrawableHelper.drawTexture(matrices, x - 4, y - 4, 194, 26, w + 6, h + 6, 256, 256)
    DrawableHelper.drawTexture(matrices, x - 1, y - 1, 194, 82,  w + 2, h + 2, 256, 256)
    lazy val fancyHeight = MathHelper.ceil((System.currentTimeMillis() / (animation.duration / h) % h))
    val innerDisplayHeight = animation match 
      case EntryAnimation.None => h 
      case EntryAnimation.Upwards(_) => fancyHeight 
      case EntryAnimation.Downwards(_) => h - fancyHeight
    drawFluid(matrices, fluid.getEntry().getValue().asInstanceOf[FluidVariant].getFluid(), innerDisplayHeight, x, y, w, h)
  def drawFluid(matrices: MatrixStack, fluid: Fluid, drawHeight : Int, dX: Int, bY: Int, dW: Int, dH: Int): Unit = 
    RenderSystem.setShaderTexture(0, PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)
    val dY = bY + dH 

    val handler = FluidRenderHandlerRegistry.INSTANCE.get(fluid)
    if (handler == null) return 
    val sprite = handler.getFluidSprites(MinecraftClient.getInstance().world, BlockPos.ORIGIN, fluid.getDefaultState())(0)
    val color = handler.getFluidColor(MinecraftClient.getInstance().world, BlockPos.ORIGIN, fluid.getDefaultState())

    val iconHeight = sprite.getHeight 
    var offsetHeight = drawHeight 

    RenderSystem.setShaderColor((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, 1F)

    (0 to 50).takeWhile(_ => offsetHeight != 0).foreach(_ => { 
      val curHeight = Math.min(offsetHeight, iconHeight)

      DrawableHelper.drawSprite(matrices, dX, dY - offsetHeight - 2, 0, dW - 2, curHeight, sprite)
      offsetHeight = offsetHeight - curHeight 
    })

}
  
class RebornProgressWidget(val x: Int, val y: Int, val duration: Double, val direction: GuiBuilder.ProgressDirection) extends Widget {
  override def getBounds() = Bounds(x, y, direction.width, direction.height) 
  override def render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float): Unit = 
    RenderSystem.setShaderTexture(0, GuiBuilder.defaultTextureSheet)
    DrawableHelper.drawTexture(matrices, x, y, direction.x, direction.y, direction.width, direction.height, 256, 256)
    val j : Int = Math.max(0, ((System.currentTimeMillis() / duration) % 1.0 * 16.0).toInt)
    direction match 
      case GuiBuilder.ProgressDirection.RIGHT => DrawableHelper.drawTexture(matrices, x, y, direction.xActive, direction.yActive, j, 10, 256, 256) 
      case GuiBuilder.ProgressDirection.LEFT => DrawableHelper.drawTexture(matrices, x + 16 - j, y, direction.xActive + 16 - j, direction.yActive, j, 10,256, 256) 
      case GuiBuilder.ProgressDirection.UP => DrawableHelper.drawTexture(matrices, x, y + 16 - j, direction.xActive, direction.yActive + 16 - j, 10, j,256, 256) 
      case GuiBuilder.ProgressDirection.DOWN => DrawableHelper.drawTexture(matrices, x, y, direction.xActive, direction.yActive, 10, j, 256, 256) 
}

enum EntryAnimation(val duration: Double) {
  case Upwards(dur: Double) extends EntryAnimation(dur) 
  case Downwards(dur: Double) extends EntryAnimation(dur)
  case None extends EntryAnimation(0.0d)
}

