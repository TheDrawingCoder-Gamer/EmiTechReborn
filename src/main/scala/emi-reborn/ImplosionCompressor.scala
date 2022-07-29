package net.bulbyvr.emireborn.machines

import reborncore.common.crafting.RebornRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import techreborn.init.ModRecipes
import techreborn.init.TRContent.Machine
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import dev.emi.emi.api.widget.SlotWidget
import net.bulbyvr.emireborn.RebornProgressWidget
import reborncore.client.gui.guibuilder.GuiBuilder
import net.minecraft.text.TranslatableText
import java.text.DecimalFormat
import dev.emi.emi.api.EmiRegistry
import net.bulbyvr.emireborn.RebornPlugin
import net.bulbyvr.emireborn.addRightText
object EmiImplosionCompressorRecipe {
  val CATEGORY = EmiRecipeCategory(ModRecipes.IMPLOSION_COMPRESSOR.name, EmiStack.of(Machine.IMPLOSION_COMPRESSOR))
  def register(registry: EmiRegistry): Unit = 
    RebornPlugin.registerHelper(EmiStack.of(Machine.IMPLOSION_COMPRESSOR))(registry, ModRecipes.IMPLOSION_COMPRESSOR, CATEGORY, EmiImplosionCompressorRecipe(_)) 
}
class EmiImplosionCompressorRecipe[R <: RebornRecipe](recipe: R) extends AbstractEnergyConsumingMachineRecipe[R], AbstractMachineRecipe[R](recipe, EmiImplosionCompressorRecipe.CATEGORY) {
  override def addWidgets(widgets: WidgetHolder): Unit = 
    widgets.add(SlotWidget(getInput(0), 55 - 15, 35 - 19))
    widgets.add(SlotWidget(getInput(1), 55 - 15, 55 - 19))
    widgets.add(SlotWidget(getOutput(0), 97 - 15, 45 - 19).recipeContext(this))
    widgets.add(SlotWidget(getOutput(1), 97 + 18 - 15, 45 - 19).recipeContext(this))
    widgets.add(RebornProgressWidget(76 - 15, 48 - 19, time * 50, GuiBuilder.ProgressDirection.RIGHT))
    widgets.addRightText(TranslatableText("techreborn.jei.recipe.processing.time.3", DecimalFormat("###.##").format(time.toFloat / 20f)).asOrderedText(), getDisplayWidth() - 5,5, 0xFF404040, false)

}
