package shared

object SharedSerializationClasses {
  sealed trait Fruit
  case class Apple(isJuicy: Boolean) extends Fruit
  case class Lemon(sourness: Double) extends Fruit
  case class FruitSalad(components: Seq[Fruit]) extends Fruit
  case object TheDurian extends Fruit
}