package net.bulbyvr.emireborn.machines

import reborncore.common.crafting.RebornRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import techreborn.init.ModRecipes
import dev.emi.emi.api.stack.EmiStack
import techreborn.init.TRContent.Machine
import dev.emi.emi.api.widget.WidgetHolder
import dev.emi.emi.api.widget.SlotWidget
import net.bulbyvr.emireborn.RebornProgressWidget
import reborncore.client.gui.guibuilder.GuiBuilder
import java.text.DecimalFormat
import net.minecraft.text.TranslatableText
import dev.emi.emi.api.EmiRegistry
import net.bulbyvr.emireborn.RebornPlugin

object EmiCentrifugeRecipe {
  val CATEGORY = EmiRecipeCategory(ModRecipes.CENTRIFUGE.name, EmiStack.of(Machine.INDUSTRIAL_CENTRIFUGE))
  def register(registry: EmiRegistry) = 
    RebornPlugin.registerHelper(EmiStack.of(Machine.INDUSTRIAL_CENTRIFUGE))(registry, ModRecipes.CENTRIFUGE, CATEGORY, EmiCentrifugeRecipe(_))
}
class EmiCentrifugeRecipe[R <: RebornRecipe](recipe:R) extends AbstractEnergyConsumingMachineRecipe[R], AbstractMachineRecipe[R](recipe, EmiCentrifugeRecipe.CATEGORY) {
  override def addWidgets(widgets: WidgetHolder): Unit = 
    widgets.add(SlotWidget(getInput(0), 55 - 17, 35 - 19).output(false))
    widgets.add(SlotWidget(getInput(1), 55 - 17, 55 - 19).output(false))
    widgets.add(SlotWidget(getOutput(0), 97 - 17, 45 - 19).output(true).recipeContext(this))
    widgets.add(SlotWidget(getOutput(1), 116 - 17, 26 - 19).output(true).recipeContext(this))
    widgets.add(SlotWidget(getOutput(2), 135 - 17, 45 - 19).output(true).recipeContext(this))
    widgets.add(SlotWidget(getOutput(3), 116 - 17, 64 - 19).output(true).recipeContext(this))
    widgets.add(RebornProgressWidget(76 - 17, 48 - 19, time * 50, GuiBuilder.ProgressDirection.RIGHT))

    widgets.addText(TranslatableText("techreborn.jei.recipe.processing.time.3", DecimalFormat("###.##").format(time.toFloat / 20f)).asOrderedText(), 24, 5, 0xFF404040, false)

}
