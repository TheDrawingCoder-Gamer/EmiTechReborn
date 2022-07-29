package net.bulbyvr.emireborn.machines

import reborncore.common.crafting.RebornRecipe
import dev.emi.emi.api.widget.WidgetHolder
import dev.emi.emi.api.widget.SlotWidget
import dev.emi.emi.api.recipe.EmiRecipeCategory
import techreborn.init.ModRecipes
import techreborn.init.TRContent.Machine
import dev.emi.emi.api.stack.EmiStack
import net.bulbyvr.emireborn.RebornProgressWidget
import reborncore.client.gui.guibuilder.GuiBuilder
import net.minecraft.text.LiteralText
import net.minecraft.text.TranslatableText
import java.text.DecimalFormat
import dev.emi.emi.api.EmiRegistry
import net.bulbyvr.emireborn.addRightText

class EmiBlastFurnaceRecipe[R <: RebornRecipe](recipe: R) extends AbstractEnergyConsumingMachineRecipe[R], AbstractMachineRecipe[R](recipe, EmiBlastFurnaceRecipe.CATEGORY) {
  override def addWidgets(holder: WidgetHolder): Unit = 
    holder.add(SlotWidget(getInput(0), 55 - 17, 35 - 19))
    holder.add(SlotWidget(getInput(1), 55 - 17, 55 - 19))
    holder.add(SlotWidget(getOutput(0), 101 -17, 45 - 19).recipeContext(this))
    holder.add(SlotWidget(getOutput(1), 101 + 20 - 17, 45 - 19).recipeContext(this))
    holder.add(RebornProgressWidget(76 - 17, 48 - 19, time * 50, GuiBuilder.ProgressDirection.RIGHT))
    val neededHeat = LiteralText(String.valueOf(heat)).append(" ").append(TranslatableText("techreborn.jei.recipe.heat")).asOrderedText()
    holder.addRightText(neededHeat, getDisplayWidth() - 5, 5, 0xFF040404, false)
    holder.addText(TranslatableText("techreborn.jei.recipe.processing.time.3", DecimalFormat("###.##").format(time.toFloat / 20f)).asOrderedText(), 24, 5, 0xFF040404, false)
}
object EmiBlastFurnaceRecipe {
  val CATEGORY = EmiRecipeCategory(ModRecipes.BLAST_FURNACE.name, EmiStack.of( Machine.INDUSTRIAL_BLAST_FURNACE))
  def register(registry: EmiRegistry): Unit = 
    registry.addCategory(CATEGORY)
    registry.addWorkstation(CATEGORY, EmiStack.of(Machine.INDUSTRIAL_BLAST_FURNACE))
    registry.getRecipeManager().listAllOfType(ModRecipes.BLAST_FURNACE).forEach(it => registry.addRecipe(EmiBlastFurnaceRecipe(it)))
}
