package net.bulbyvr.emireborn.machines

import reborncore.common.crafting.RebornRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import techreborn.init.ModRecipes
import techreborn.init.TRContent.Machine
import dev.emi.emi.api.widget.WidgetHolder
import dev.emi.emi.api.widget.SlotWidget
import dev.emi.emi.api.stack.EmiStack
import net.bulbyvr.emireborn.RebornProgressWidget
import reborncore.client.gui.guibuilder.GuiBuilder
import net.minecraft.text.TranslatableText
import java.text.DecimalFormat
import dev.emi.emi.api.EmiRegistry
import net.bulbyvr.emireborn.addRightText

class EmiDistillationTowerRecipe[R <: RebornRecipe](recipe: R) extends AbstractEnergyConsumingMachineRecipe[R], AbstractMachineRecipe[R](recipe, EmiDistillationTowerRecipe.CATEGORY) {
  override def addWidgets(holder: WidgetHolder): Unit = 
    holder.add(SlotWidget(getInput(0), 55 - 23, 35 - 19))
    holder.add(SlotWidget(getInput(1), 55 - 23, 55 - 19))
    // holder.add(SlotWidget(EmiStack.EMPTY, 101 - 23 - 5, 45 - 19 - 5).customBackground(null, 0, 0, 66, 26))
    holder.add(SlotWidget(getOutput(0), 101 - 23, 45 - 19).recipeContext(this))
    holder.add(SlotWidget(getOutput(1), 101 + 20 - 23, 45 - 19).recipeContext(this))
    holder.add(SlotWidget(getOutput(2), 101 + 40 - 23, 45 - 19).recipeContext(this))
    holder.add(RebornProgressWidget(76 - 23, 48 - 19, time * 50, GuiBuilder.ProgressDirection.RIGHT))

    holder.addRightText(TranslatableText("techreborn.jei.recipe.processing.time.3", DecimalFormat("###.##").format(time.toFloat / 20)).asOrderedText(), getDisplayWidth() - 5, 5, 0xFF404040, false)
}
object EmiDistillationTowerRecipe {
  val CATEGORY = EmiRecipeCategory(ModRecipes.DISTILLATION_TOWER.name, EmiStack.of(Machine.DISTILLATION_TOWER))
  def register(registry: EmiRegistry): Unit = 
    registry.addCategory(CATEGORY)
    registry.addWorkstation(CATEGORY, EmiStack.of(Machine.DISTILLATION_TOWER))
    registry.getRecipeManager().listAllOfType(ModRecipes.DISTILLATION_TOWER).forEach(it => registry.addRecipe(EmiDistillationTowerRecipe(it)))
}
