package net.bulbyvr.emireborn.fluidreplicator 

import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import techreborn.api.recipe.recipes.FluidReplicatorRecipe

import scala.jdk.CollectionConverters._
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.FluidEmiStack
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import dev.emi.emi.api.widget.WidgetHolder
import net.bulbyvr.emireborn.RebornPlugin
import net.bulbyvr.emireborn.EntryAnimation
import net.minecraft.text.OrderedText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import scala.collection.mutable.ListBuffer
import net.minecraft.util.Formatting
import net.minecraft.client.gui.tooltip.TooltipComponent
import dev.emi.emi.api.widget.SlotWidget
import net.bulbyvr.emireborn.RebornProgressWidget
import reborncore.client.gui.guibuilder.GuiBuilder
import java.text.DecimalFormat
import dev.emi.emi.api.EmiRegistry
import net.minecraft.util.Identifier
import techreborn.TechReborn
import techreborn.init.ModRecipes
import techreborn.init.TRContent.Machine

class EmiFluidReplicatorRecipe(val recipe: FluidReplicatorRecipe, val category: EmiRecipeCategory) extends EmiRecipe {
  override def getDisplayHeight(): Int = 54 
  override def getDisplayWidth(): Int = 134 
  
  val inputs : List[EmiIngredient] = List.from(for (ing <- recipe.getRebornIngredients().asScala) yield EmiIngredient.of((for (stack <- ing.getPreviewStacks().asScala) 
    yield EmiStack.of(stack)).asJava))
  val fluidInstance = recipe.getFluidInstance() 
  val outputs: List[EmiStack] = if fluidInstance == null then List() else List(FluidEmiStack(FluidVariant.of(fluidInstance.getFluid()), fluidInstance.getAmount().getRawValue()))
  val energy: Int = recipe.getPower()
  val time: Int = recipe.getTime() 
  override def getInputs() = inputs.asJava
  override def getOutputs() = outputs.asJava
  override def getCategory(): EmiRecipeCategory = category 
  override def getId() = recipe.getId()
  override def addWidgets(holder: WidgetHolder): Unit = 
    val startX : Int = (getDisplayWidth() / 2).toInt - 41
    val startY : Int = (getDisplayHeight() / 2).toInt - 13 

    holder.add(RebornPlugin.createEnergyDisplay(8, 8, 14, 50, energy, EntryAnimation.Downwards(5000), (x, y) => {
      val list : ListBuffer[OrderedText] = ListBuffer() 
      list.addOne(Text.of("Energy").asOrderedText())
      list.addOne(TranslatableText("techreborn.jei.recipe.running.cost", "E", energy).formatted(Formatting.GRAY).asOrderedText())
      list.addOne(Text.of("").asOrderedText())
      (for (txt <- list) yield TooltipComponent.of(txt)).asJava
    }))
    holder.add(SlotWidget(inputs(0),46, 26).output(false))
    holder.add(RebornProgressWidget(46 + 21, 30, time * 50,  GuiBuilder.ProgressDirection.RIGHT))
    holder.add(RebornPlugin.createFluidDisplay(46 + 46,  8, 16, 50, outputs(0).getEmiStacks().get(0).asInstanceOf[FluidEmiStack], EntryAnimation.Upwards(5000)))
    holder.addText(TranslatableText("techreborn.jei.recipe.processing.time.3", DecimalFormat("###.##").format(time.toFloat / 20.0)).asOrderedText(), 24, 5, 0xFF404040, false)
}

object EmiFluidReplicatorCategories {
  val FLUID_REPLICATOR : EmiRecipeCategory = EmiRecipeCategory(ModRecipes.FLUID_REPLICATOR.name, EmiStack.of(Machine.FLUID_REPLICATOR))
  def register(registry: EmiRegistry): Unit = 
    registry.addCategory(FLUID_REPLICATOR)
    registry.addWorkstation(FLUID_REPLICATOR, EmiStack.of(Machine.FLUID_REPLICATOR))
    registry.getRecipeManager().listAllOfType(ModRecipes.FLUID_REPLICATOR).forEach(it => 
        registry.addRecipe(EmiFluidReplicatorRecipe(it, FLUID_REPLICATOR))
    )

}
