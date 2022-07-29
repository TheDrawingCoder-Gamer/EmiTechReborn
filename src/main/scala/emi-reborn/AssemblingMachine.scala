package net.bulbyvr.emireborn.machines

import reborncore.common.crafting.RebornRecipe
import dev.emi.emi.api.widget.WidgetHolder
import dev.emi.emi.api.recipe.EmiRecipeCategory
import techreborn.init.ModRecipes
import dev.emi.emi.api.stack.EmiStack
import techreborn.init.TRContent.Machine
import dev.emi.emi.api.widget.SlotWidget
import net.bulbyvr.emireborn.RebornProgressWidget
import reborncore.client.gui.guibuilder.GuiBuilder
import net.minecraft.text.TranslatableText
import java.text.DecimalFormat
import net.bulbyvr.emireborn.RebornProgressWidget
import dev.emi.emi.api.EmiRegistry
import net.bulbyvr.emireborn.RightAlignedText

class EmiAssemblingMachineRecipe[R <: RebornRecipe](recipe: R) extends AbstractEnergyConsumingMachineRecipe[R], AbstractMachineRecipe[R](recipe, EmiAssemblingMachineRecipe.CATEGORY) {
  override def addWidgets(holder: WidgetHolder): Unit = 
    super.addWidgets(holder)
    holder.add(SlotWidget(getInput(0), 55 - 9, 35 - 19))
    holder.add(SlotWidget(getInput(1), 55 - 9, 55 - 19))
    holder.add(SlotWidget(getOutput(0), 101 - 9, 45 - 19).recipeContext(this))
    holder.add(RebornProgressWidget(76 - 9, 48 - 19, time * 50, GuiBuilder.ProgressDirection.RIGHT))

    holder.add(RightAlignedText(TranslatableText("techreborn.jei.recipe.processing.time.3", DecimalFormat("###.##").format(time.toFloat / 20f)).asOrderedText(), getDisplayWidth() - 5, 5, 0xFF404040, false))

}

object EmiAssemblingMachineRecipe {
  val CATEGORY =  EmiRecipeCategory(ModRecipes.ASSEMBLING_MACHINE.name, EmiStack.of(Machine.ASSEMBLY_MACHINE))
  def register(registry: EmiRegistry): Unit = 
    registry.addCategory(CATEGORY)
    registry.addWorkstation(CATEGORY, EmiStack.of(Machine.ASSEMBLY_MACHINE))
    registry.getRecipeManager().listAllOfType(ModRecipes.ASSEMBLING_MACHINE).forEach(it => 
        registry.addRecipe(EmiAssemblingMachineRecipe(it))
    )
}
