package net.bulbyvr.emireborn.machines

import reborncore.common.crafting.RebornRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import techreborn.init.ModRecipes
import dev.emi.emi.api.stack.EmiStack
import techreborn.init.TRContent.Machine
import dev.emi.emi.api.widget.WidgetHolder
import dev.emi.emi.api.widget.SlotWidget
import net.minecraft.text.TranslatableText
import java.text.DecimalFormat
import net.bulbyvr.emireborn.RebornProgressWidget
import reborncore.client.gui.guibuilder.GuiBuilder
import net.bulbyvr.emireborn.RebornPlugin
import dev.emi.emi.api.stack.FluidEmiStack
import net.bulbyvr.emireborn.EntryAnimation
import dev.emi.emi.api.EmiRegistry

object EmiSawmillRecipe {
  val CATEGORY = EmiRecipeCategory(ModRecipes.INDUSTRIAL_SAWMILL.name, EmiStack.of(Machine.INDUSTRIAL_SAWMILL))
  def register(registry: EmiRegistry) = RebornPlugin.registerHelper(EmiStack.of(Machine.INDUSTRIAL_SAWMILL))(registry, ModRecipes.INDUSTRIAL_SAWMILL, CATEGORY, EmiSawmillRecipe(_))
}
class EmiSawmillRecipe[R <: RebornRecipe](recipe: R) extends AbstractEnergyConsumingMachineRecipe[R], AbstractMachineRecipe[R](recipe,EmiSawmillRecipe.CATEGORY) {
  override def addWidgets(widgets: WidgetHolder): Unit = 
    super.addWidgets(widgets)
    widgets.add(SlotWidget(getInput(0), 55, 26).output(false))
    widgets.add(SlotWidget(getOutput(0), 55 + 46, 26 - 18).output(true).recipeContext(this))
    widgets.add(SlotWidget(getOutput(1), 55 + 46, 26).output(true).recipeContext(this))
    widgets.add(SlotWidget(getOutput(2), 55 + 46, 26 + 18).output(true).recipeContext(this))
    widgets.add(RebornProgressWidget(55 + 21, 30, time * 50, GuiBuilder.ProgressDirection.RIGHT))
    widgets.add(RebornPlugin.createFluidDisplay(55 - 26, 8, 16, 50, getInput(1).asInstanceOf[FluidEmiStack], EntryAnimation.Downwards(5000)))
    widgets.addText(TranslatableText("techreborn.jei.recipe.processing.time.3", DecimalFormat("###.##").format(time.toFloat / 20f)).asOrderedText(), 24, 5, 0xFF404040, false)
}
