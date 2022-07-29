package net.bulbyvr.emireborn.machines 

import reborncore.common.crafting.RebornRecipe
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.widget.WidgetHolder
import dev.emi.emi.api.widget.SlotWidget
import net.bulbyvr.emireborn.RebornPlugin
import net.bulbyvr.emireborn.RebornProgressWidget
import reborncore.client.gui.guibuilder.GuiBuilder
import dev.emi.emi.api.recipe.EmiRecipeCategory
import net.minecraft.text.TranslatableText
import java.text.DecimalFormat
import techreborn.init.ModRecipes
import dev.emi.emi.api.stack.EmiStack
import techreborn.init.TRContent.Machine
import techreborn.init.TRContent
import net.minecraft.recipe.RecipeType

class OneInputOneOutput[R <: RebornRecipe](recipe: R, cat: EmiRecipeCategory) extends AbstractEnergyConsumingMachineRecipe[R], AbstractMachineRecipe[R](recipe, cat) {
  override def addWidgets(holder: WidgetHolder): Unit = 
    super.addWidgets(holder)
    holder.add(SlotWidget(getInput(0), 46, 26).output(false)) 
    holder.add(SlotWidget(getOutput(0), 46 + 46, 26).output(true).recipeContext(this))
    holder.add(RebornProgressWidget(46 + 21, 30, time * 50, GuiBuilder.ProgressDirection.RIGHT))
    holder.addText(TranslatableText("techreborn.jei.recipe.processing.time.3", DecimalFormat("###.##").format(time.toFloat / 20.0)).asOrderedText(), getDisplayWidth - 5, 5, 0xFF404040, false) 
}

object OneInputOneOutput {
  val COMPRESSOR = EmiRecipeCategory(ModRecipes.COMPRESSOR.name, EmiStack.of(Machine.COMPRESSOR))
  val EXTRACTOR = EmiRecipeCategory(ModRecipes.EXTRACTOR.name, EmiStack.of(Machine.EXTRACTOR))
  val GRINDER = EmiRecipeCategory(ModRecipes.GRINDER.name, EmiStack.of(Machine.GRINDER))
  val SCRAPBOX = EmiRecipeCategory(ModRecipes.SCRAPBOX.name, EmiStack.of(TRContent.SCRAP_BOX))
  val VACUUM_FREEZER = EmiRecipeCategory(ModRecipes.VACUUM_FREEZER.name, EmiStack.of(Machine.VACUUM_FREEZER))
  val WIRE_MILL = EmiRecipeCategory(ModRecipes.WIRE_MILL.name, EmiStack.of(Machine.WIRE_MILL))

  private def registerHelper[R <: RebornRecipe](workstations: EmiStack*)(registry: EmiRegistry, kind: RecipeType[R], cat: EmiRecipeCategory) = 
    RebornPlugin.registerHelper(workstations: _*)(registry, kind, cat, OneInputOneOutput(_, cat))
  def register(registry: EmiRegistry) = 
    registerHelper(EmiStack.of(Machine.COMPRESSOR))(registry, ModRecipes.COMPRESSOR, COMPRESSOR)
    registerHelper(EmiStack.of(Machine.EXTRACTOR))(registry, ModRecipes.EXTRACTOR, EXTRACTOR)
    registerHelper(EmiStack.of(Machine.GRINDER))(registry, ModRecipes.GRINDER, GRINDER)
    registerHelper()(registry, ModRecipes.SCRAPBOX, SCRAPBOX)
    registerHelper(EmiStack.of(Machine.VACUUM_FREEZER ))(registry, ModRecipes.VACUUM_FREEZER , VACUUM_FREEZER )
    registerHelper(EmiStack.of(Machine.WIRE_MILL ))(registry, ModRecipes.WIRE_MILL , WIRE_MILL )
}
