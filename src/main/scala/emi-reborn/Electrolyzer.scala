package net.bulbyvr.emireborn.machines

import dev.emi.emi.api.recipe.EmiRecipeCategory
import techreborn.init.ModRecipes
import dev.emi.emi.api.stack.EmiStack
import techreborn.init.TRContent.Machine
import reborncore.common.crafting.RebornRecipe
import dev.emi.emi.api.widget.WidgetHolder
import dev.emi.emi.api.widget.SlotWidget
import net.bulbyvr.emireborn.RebornProgressWidget
import reborncore.client.gui.guibuilder.GuiBuilder
import net.minecraft.text.TranslatableText
import java.text.DecimalFormat
import dev.emi.emi.api.EmiRegistry

object EmiElectrolyzerRecipe {
  val CATEGORY = EmiRecipeCategory(ModRecipes.INDUSTRIAL_ELECTROLYZER.name, EmiStack.of(Machine.INDUSTRIAL_ELECTROLYZER))
  def register(registry: EmiRegistry): Unit = 
    registry.addCategory(CATEGORY)
    registry.addWorkstation(CATEGORY, EmiStack.of(Machine.INDUSTRIAL_ELECTROLYZER))
    registry.getRecipeManager().listAllOfType(ModRecipes.INDUSTRIAL_ELECTROLYZER).forEach(it => registry.addRecipe(EmiElectrolyzerRecipe(it)))
}

class EmiElectrolyzerRecipe[R <: RebornRecipe](recipe: R) extends AbstractEnergyConsumingMachineRecipe[R], AbstractMachineRecipe[R](recipe, EmiElectrolyzerRecipe.CATEGORY) {
  override def getDisplayHeight(): Int = 66 
  override def addWidgets(holder: WidgetHolder): Unit = 
    holder.add(SlotWidget(getInput(0), 55 - 20, 41).output(false))
    holder.add(SlotWidget(getInput(1), 55, 41).output(false))
    holder.add(SlotWidget(EmiStack.EMPTY, 55 + 17 - 9 - 20 - 5, 36 - 22 - 5).customBackground(null, 0, 0,86,26).output(true))
    holder.add(SlotWidget(getOutput(0), 55 + 17 - 9 - 20, 36 - 22).drawBack(false).recipeContext(this).output(true))
    holder.add(SlotWidget(getOutput(1), 55 + 17 - 9, 36 - 22).drawBack(false).recipeContext(this).output(true))
    holder.add(SlotWidget(getOutput(2), 55 + 17 - 9 + 20, 36 - 22).drawBack(false).recipeContext(this).output(true))
    holder.add(SlotWidget(getOutput(3), 55 + 17 - 9 + 40, 36 - 22).drawBack(false).recipeContext(this).output(true))
    holder.add(RebornProgressWidget(55 + 21, 36 + 4, time * 50, GuiBuilder.ProgressDirection.UP))
    
    holder.addText(TranslatableText("techreborn.jei.recipe.processing.time.3", DecimalFormat("###.##").format(time.toFloat / 20f)).asOrderedText(), getDisplayWidth() - 17, getDisplayHeight() - 13, 0xFF404040, false)


}
