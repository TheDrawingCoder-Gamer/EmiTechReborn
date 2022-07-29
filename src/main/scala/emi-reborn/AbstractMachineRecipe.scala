package net.bulbyvr.emireborn.machines

import dev.emi.emi.api.recipe.EmiRecipe
import reborncore.common.crafting.RebornRecipe
import reborncore.common.crafting.RebornRecipeType
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import java.{util => ju}

import scala.jdk.CollectionConverters._
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.util.Identifier
import techreborn.api.recipe.recipes.BlastFurnaceRecipe
import reborncore.common.crafting.RebornFluidRecipe
import dev.emi.emi.api.stack.FluidEmiStack
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import java.{util => ju}
import dev.emi.emi.api.widget.WidgetHolder
import net.bulbyvr.emireborn.RebornPlugin
import net.bulbyvr.emireborn.EntryAnimation
import scala.collection.mutable.ListBuffer
import net.minecraft.text.OrderedText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import net.minecraft.client.gui.tooltip.TooltipComponent

trait AbstractMachineRecipe[R <: RebornRecipe](val rebornRecipe: R, val category: EmiRecipeCategory) extends EmiRecipe {
  val inputs : List[EmiIngredient] = List.from(for (ing <- rebornRecipe.getRebornIngredients().asScala) yield EmiIngredient.of(
    (for (stacks <- ing.getPreviewStacks().asScala) yield EmiStack.of(stacks)).asJava
  )).appendedAll(rebornRecipe match 
    case r : RebornFluidRecipe => List(FluidEmiStack(FluidVariant.of(r.getFluidInstance().getFluid()), r.getFluidInstance().getAmount().getRawValue())) 
    case _ => List())
  val outputs : List[EmiStack] = List.from(for (ing <- rebornRecipe.getOutputs().asScala) yield EmiStack.of(ing))
  val energy = rebornRecipe.getPower()
  val heat = rebornRecipe match 
    case r : BlastFurnaceRecipe => r.getHeat() 
    case _ => 0
  val time = rebornRecipe.getTime() 
  val fluidInstance = rebornRecipe match 
    case r : RebornFluidRecipe => Some(r.getFluidInstance()) 
    case _ => None

  override def getCategory(): EmiRecipeCategory = category 
  override def getId(): Identifier = rebornRecipe.getId() 
  override def getInputs(): ju.List[EmiIngredient] =  inputs.asJava
  override def getOutputs(): ju.List[EmiStack] = outputs.asJava
  def getInput(i: Int): EmiIngredient = 
    if inputs.size > i then 
      inputs(i) 
    else 
      EmiStack.EMPTY
  def getOutput(i: Int): EmiStack = 
    if outputs.size > i then 
      outputs(i) 
    else 
      EmiStack.EMPTY
  override def getDisplayHeight() = 54 
  override def getDisplayWidth() = 134
}

trait AbstractEnergyConsumingMachineRecipe[R <: RebornRecipe] extends AbstractMachineRecipe[R] {
  override def addWidgets(holder: WidgetHolder): Unit = 
    holder.add(RebornPlugin.createEnergyDisplay(8,4,14,50, energy, EntryAnimation.Downwards(5000), (x, y) => {
      val list: ListBuffer[OrderedText] = ListBuffer()
      list.addOne(Text.of("Energy").asOrderedText)
      list.addOne(TranslatableText("techreborn.jei.recipe.running.cost", "E", energy).formatted(Formatting.GRAY).asOrderedText) 
      list.addOne(Text.of("").asOrderedText)
      (for (txt <- list) yield TooltipComponent.of(txt)).asJava
    }))
}
