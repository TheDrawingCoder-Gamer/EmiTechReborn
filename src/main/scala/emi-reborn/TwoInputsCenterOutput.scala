package net.bulbyvr.emireborn.machines

import dev.emi.emi.api.recipe.EmiRecipeCategory
import techreborn.init.ModRecipes
import reborncore.common.crafting.RebornRecipe
import dev.emi.emi.api.widget.WidgetHolder
import dev.emi.emi.api.widget.SlotWidget
import dev.emi.emi.api.stack.EmiStack
import reborncore.client.gui.guibuilder.GuiBuilder
import net.minecraft.text.TranslatableText
import java.text.DecimalFormat
import net.bulbyvr.emireborn.RebornProgressWidget
import techreborn.init.TRContent.Machine
import dev.emi.emi.api.EmiRegistry
import net.bulbyvr.emireborn.RebornPlugin
import net.minecraft.recipe.RecipeType
import net.bulbyvr.emireborn.addRightText
object EmiTwoInputsCenterOutputRecipe {
  val ALLOY_SMELTER = EmiRecipeCategory(ModRecipes.ALLOY_SMELTER.name, EmiStack.of(Machine.ALLOY_SMELTER))
  val CHEMICAL_REACTOR = EmiRecipeCategory(ModRecipes.CHEMICAL_REACTOR.name, EmiStack.of(Machine.CHEMICAL_REACTOR))
  val FUSION_REACTOR = EmiRecipeCategory(ModRecipes.FUSION_REACTOR.name, EmiStack.of(Machine.FUSION_CONTROL_COMPUTER))
  val SOLID_CANNING_MACHINE = EmiRecipeCategory(ModRecipes.SOLID_CANNING_MACHINE.name, EmiStack.of(Machine.SOLID_CANNING_MACHINE))

  def register(registry: EmiRegistry): Unit = 
    registerHelper(EmiStack.of(Machine.ALLOY_SMELTER), EmiStack.of(Machine.IRON_ALLOY_FURNACE))(registry, ModRecipes.ALLOY_SMELTER, ALLOY_SMELTER)
    registerHelper(EmiStack.of(Machine.CHEMICAL_REACTOR))(registry, ModRecipes.CHEMICAL_REACTOR, CHEMICAL_REACTOR)
    registerHelper(EmiStack.of(Machine.FUSION_CONTROL_COMPUTER))(registry, ModRecipes.FUSION_REACTOR, FUSION_REACTOR)
    registerHelper(EmiStack.of(Machine.SOLID_CANNING_MACHINE))(registry, ModRecipes.SOLID_CANNING_MACHINE, SOLID_CANNING_MACHINE)
  private def registerHelper[R <: RebornRecipe](workbenches: EmiStack*)(registry:EmiRegistry, kind: RecipeType[R], cat: EmiRecipeCategory) = 
    RebornPlugin.registerHelper(workbenches:_*)(registry, kind, cat, EmiTwoInputsCenterOutputRecipe(_, cat))
}
class EmiTwoInputsCenterOutputRecipe[R <: RebornRecipe](recipe: R, category: EmiRecipeCategory) extends AbstractEnergyConsumingMachineRecipe[R], AbstractMachineRecipe[R](recipe, category) {
  override def addWidgets(widgets: WidgetHolder): Unit =
    super.addWidgets(widgets)
    widgets.add(SlotWidget(getInput(0), 29, 26))
    widgets.add(SlotWidget(getInput(1), 29 + 92, 26))
    // valid use of output(true)
    widgets.add(SlotWidget(getOutput(0), 29 + 46, 26).output(true).recipeContext(this))
    widgets.add(RebornProgressWidget(29 + 21, 30, time * 50, GuiBuilder.ProgressDirection.RIGHT))
    widgets.add(RebornProgressWidget(29 + 71, 30, time * 50, GuiBuilder.ProgressDirection.LEFT))

    widgets.addRightText(TranslatableText("techreborn.jei.recipe.processing.time.3", DecimalFormat("###.##").format(time.toFloat / 20f)).asOrderedText(), getDisplayWidth() - 5, 5, 0xFF404040, false)

}
