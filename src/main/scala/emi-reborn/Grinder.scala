package net.bulbyvr.emireborn.machines 

import dev.emi.emi.api.recipe.EmiRecipeCategory
import techreborn.init.ModRecipes
import techreborn.init.TRContent.Machine
import dev.emi.emi.api.stack.EmiStack
import reborncore.common.crafting.RebornRecipe
import dev.emi.emi.api.widget.WidgetHolder
import net.bulbyvr.emireborn.RebornProgressWidget
import net.bulbyvr.emireborn.RebornPlugin
import net.bulbyvr.emireborn.EntryAnimation
import net.minecraft.text.OrderedText
import scala.collection.mutable.ListBuffer
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import net.minecraft.client.gui.tooltip.TooltipComponent

import scala.jdk.CollectionConverters._
import dev.emi.emi.api.widget.SlotWidget
import reborncore.client.gui.guibuilder.GuiBuilder
import dev.emi.emi.api.stack.FluidEmiStack
import java.text.DecimalFormat
import dev.emi.emi.api.EmiRegistry
object EmiGrinderRecipe {
  val CATEGORY = EmiRecipeCategory(ModRecipes.INDUSTRIAL_GRINDER.name, EmiStack.of(Machine.INDUSTRIAL_GRINDER)) 
  def register(registry: EmiRegistry): Unit =
    RebornPlugin.registerHelper(EmiStack.of(Machine.INDUSTRIAL_GRINDER))(registry, ModRecipes.INDUSTRIAL_GRINDER, CATEGORY, EmiGrinderRecipe(_))

}
class EmiGrinderRecipe[R <: RebornRecipe](recipe: R) extends AbstractMachineRecipe[R](recipe, EmiGrinderRecipe.CATEGORY) {
  override def getDisplayHeight(): Int = 88
  override def addWidgets(widgets: WidgetHolder): Unit = 
    widgets.add(RebornPlugin.createEnergyDisplay(8, 18, 14, 50, energy, EntryAnimation.Downwards(5000), (x, y) => 
        val list: ListBuffer[OrderedText] = ListBuffer()
        list.addOne(Text.of("Energy").asOrderedText())
        list.addOne(TranslatableText("techreborn.jei.recipe.running.cost", "E", energy).formatted(Formatting.GRAY).asOrderedText())
        list.addOne(Text.of("").asOrderedText())
        (for (txt <- list) yield TooltipComponent.of(txt)).asJava
    ))
    widgets.add(SlotWidget(getInput(0), 55, 36).output(false))
    widgets.add(SlotWidget(getOutput(0), 55 + 46, 36 - 9 - 18).output(true).recipeContext(this))
    widgets.add(SlotWidget(getOutput(1), 55 + 46, 36 - 9).output(true).recipeContext(this))
    widgets.add(SlotWidget(getOutput(2), 55 + 46, 36 - 9 + 18).output(true).recipeContext(this))
    widgets.add(SlotWidget(getOutput(3), 55 + 46, 36 - 9 + 36).output(true).recipeContext(this))
    widgets.add(RebornProgressWidget(55 + 21, 36 + 4, time * 50, GuiBuilder.ProgressDirection.RIGHT))
    if getInput(1).getEmiStacks().size() > 0 then 
      widgets.add(RebornPlugin.createFluidDisplay(55 - 26, 18, 16, 50, getInput(1).getEmiStacks().get(0).asInstanceOf[FluidEmiStack], EntryAnimation.Downwards(5000)))


    widgets.addText(TranslatableText("techreborn.jei.recipe.processing.time.3", DecimalFormat("###.##").format(time.toFloat / 20f)).asOrderedText(), 51, 15, 0xFF404040, false)
}
