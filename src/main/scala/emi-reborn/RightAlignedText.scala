package net.bulbyvr.emireborn 

import dev.emi.emi.api.widget.Widget
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.client.MinecraftClient
import net.minecraft.text.OrderedText
import dev.emi.emi.api.widget.Bounds
import net.minecraft.client.util.math.MatrixStack

class RightAlignedText(val text: OrderedText, val x: Int, val y: Int, val color: Int, val shadow: Boolean) extends Widget {
  val goodX = x - CLIENT.textRenderer.getWidth(text)
  private def CLIENT = MinecraftClient.getInstance()

  override def getBounds(): Bounds =
    val width = CLIENT.textRenderer.getWidth(text) 
    Bounds(goodX, y, width, 10)
  override def render(matrices: MatrixStack, mX: Int, mY: Int, delta: Float): Unit = 
    matrices.push()
    matrices.translate(0, 0, 300)

    if shadow then 
      CLIENT.textRenderer.drawWithShadow(matrices, text, goodX.toFloat, y.toFloat, color)
    else 
      CLIENT.textRenderer.draw(matrices, text, goodX.toFloat, y.toFloat, color)
    matrices.pop()
}

extension (c: WidgetHolder)
  def addRightText(text: OrderedText, x: Int, y: Int, color: Int, shadow: Boolean) =
    c.add(RightAlignedText(text, x, y, color, shadow))
