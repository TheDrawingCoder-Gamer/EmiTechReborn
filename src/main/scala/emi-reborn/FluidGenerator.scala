package net.bulbyvr.emireborn.fluidgenerator; 

import techreborn.init.TRContent
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.stack.EmiIngredient
import techreborn.api.generator.FluidGeneratorRecipe
import net.minecraft.util.Identifier
import java.{util => ju}
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.recipe.EmiRecipeCategory

import net.bulbyvr.emireborn.RebornPlugin
import net.bulbyvr.emireborn.FluidWidget
import net.bulbyvr.emireborn.RebornProgressWidget
import net.bulbyvr.emireborn.EntryAnimation
import java.util.ArrayList
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.client.gui.tooltip.TooltipComponent
import net.minecraft.text.OrderedText
import dev.emi.emi.api.stack.FluidEmiStack
import reborncore.client.gui.guibuilder.GuiBuilder
import techreborn.TechReborn
import techreborn.init.TRContent.Machine
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import dev.emi.emi.api.EmiRegistry
import reborncore.common.crafting.RecipeManager
import techreborn.api.generator.EFluidGenerator
import techreborn.api.generator.GeneratorRecipeHelper
import scala.collection.mutable.ListBuffer

import scala.jdk.CollectionConverters._
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.util.Formatting
// i'm sorry for the recipe category val, it's the only way
class EmiFluidGeneratorRecipe(val recipe: FluidGeneratorRecipe, val category: EmiRecipeCategory) extends EmiRecipe {
  val inputs:ListBuffer[EmiIngredient] = ListBuffer()

  val totalEnergy = recipe.getEnergyPerBucket() 
  inputs.addOne(FluidEmiStack(FluidVariant.of(recipe.fluid), 1000))

  override def getInputs(): ju.List[EmiIngredient] = inputs.asJava
  override def getOutputs(): ju.List[EmiStack] = ju.List.of() 
  override def getCategory(): EmiRecipeCategory = category 
  // idk where it is 
  override def getId(): Identifier = null 
  override def getDisplayWidth(): Int = 134 
  override def getDisplayHeight(): Int = 54 
  override def addWidgets(holder: WidgetHolder): Unit = 
    holder.add(RebornPlugin.createEnergyDisplay(108, 8, 14, 50, totalEnergy, EntryAnimation.Upwards(5000), (x, y) => {
      val list : ListBuffer[OrderedText] = ListBuffer()
      list.addOne(Text.of("Energy").asOrderedText) 
      list.addOne(TranslatableText("techreborn.jei.recipe.generator.total", totalEnergy).formatted(Formatting.GRAY).asOrderedText)
      list.addOne(Text.of("").asOrderedText)
      // skip getting mod thing to prevent double 
      (for (txt <- list) yield TooltipComponent.of(txt)).asJava
    }))
    holder.add(RebornPlugin.createFluidDisplay(16, 8, 16, 50, getInputs().get(0).asInstanceOf[FluidEmiStack], EntryAnimation.Downwards(5000)))
    holder.add(RebornProgressWidget(76 - 16, 48 - 19, 5000, GuiBuilder.ProgressDirection.RIGHT))


}
object FluidGeneratorCategories {
  val THERMAL_GENERATOR = EmiRecipeCategory(Identifier(TechReborn.MOD_ID, Machine.THERMAL_GENERATOR.name), EmiStack.of(ItemVariant.of(Machine.THERMAL_GENERATOR)))
  val GAS_TURBINE = EmiRecipeCategory(Identifier(TechReborn.MOD_ID, Machine.GAS_TURBINE.name), EmiStack.of(Machine.GAS_TURBINE))
  val DIESEL_GENERATOR = EmiRecipeCategory(Identifier(TechReborn.MOD_ID, Machine.DIESEL_GENERATOR.name), EmiStack.of(Machine.DIESEL_GENERATOR))
  val SEMI_FLUID_GENERATOR = EmiRecipeCategory(Identifier(TechReborn.MOD_ID, Machine.SEMI_FLUID_GENERATOR.name), EmiStack.of(Machine.SEMI_FLUID_GENERATOR))
  val PLASMA_GENERATOR = EmiRecipeCategory(Identifier(TechReborn.MOD_ID, Machine.PLASMA_GENERATOR.name), EmiStack.of(Machine.PLASMA_GENERATOR))

  def registerFluidGenerator(registry: EmiRegistry) = 
    registry.addCategory(THERMAL_GENERATOR)
    registry.addCategory(GAS_TURBINE)
    registry.addCategory(DIESEL_GENERATOR)
    registry.addCategory(SEMI_FLUID_GENERATOR)
    registry.addCategory(PLASMA_GENERATOR)

    registry.addWorkstation(THERMAL_GENERATOR, EmiStack.of(Machine.THERMAL_GENERATOR))
    registry.addWorkstation(GAS_TURBINE, EmiStack.of(Machine.GAS_TURBINE))
    registry.addWorkstation(DIESEL_GENERATOR, EmiStack.of(Machine.DIESEL_GENERATOR))
    registry.addWorkstation(SEMI_FLUID_GENERATOR, EmiStack.of(Machine.SEMI_FLUID_GENERATOR))
    registry.addWorkstation(PLASMA_GENERATOR, EmiStack.of(Machine.PLASMA_GENERATOR))

    registerFluidRecipes(registry, EFluidGenerator.THERMAL, THERMAL_GENERATOR)
    registerFluidRecipes(registry, EFluidGenerator.GAS, GAS_TURBINE)
    registerFluidRecipes(registry, EFluidGenerator.DIESEL, DIESEL_GENERATOR)
    registerFluidRecipes(registry, EFluidGenerator.SEMIFLUID, SEMI_FLUID_GENERATOR)
    registerFluidRecipes(registry, EFluidGenerator.PLASMA, PLASMA_GENERATOR)
    
  private def registerFluidRecipes(registry: EmiRegistry, generator: EFluidGenerator, category: EmiRecipeCategory): Unit = 
    GeneratorRecipeHelper.getFluidRecipesForGenerator(generator).getRecipes().forEach(recipe => 
        registry.addRecipe(EmiFluidGeneratorRecipe(recipe, category))
    )
}
