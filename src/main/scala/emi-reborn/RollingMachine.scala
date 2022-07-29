package net.bulbyvr.emireborn.rollingmachine 

import techreborn.api.recipe.recipes.RollingMachineRecipe
import dev.emi.emi.api.recipe.EmiCraftingRecipe
import dev.emi.emi.recipe.EmiShapedRecipe
import net.minecraft.recipe.ShapedRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import techreborn.init.ModRecipes
import techreborn.init.TRContent.Machine
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.EmiRegistry

// I LOVE USING IMPL CLASSES
class EmiRollingMachineRecipe(recipe: ShapedRecipe) extends EmiShapedRecipe(recipe) {
    override def getCategory() = EmiRollingMachineRecipe.CATEGORY 
}
object EmiRollingMachineRecipe {
  val CATEGORY = EmiRecipeCategory(ModRecipes.ROLLING_MACHINE.name, EmiStack.of(Machine.ROLLING_MACHINE))
  def register(registry: EmiRegistry) =
    registry.addCategory(CATEGORY)
    registry.addWorkstation(CATEGORY, EmiStack.of(Machine.ROLLING_MACHINE))
    registry.getRecipeManager().listAllOfType(ModRecipes.ROLLING_MACHINE).forEach(it => 
      registry.addRecipe(EmiRollingMachineRecipe(it.getShapedRecipe()))
    )
}
