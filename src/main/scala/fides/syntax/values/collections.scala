package fides.syntax.values

import fides.syntax.identifiers.{Inp, Out}
import fides.syntax.code.{Code, Expr, Val, ValQ, ValType, Xctr}

/**
  * A value that is made up of an unordered collection of values.
  */
sealed trait Collected[T <: ValType] extends ValQ[Collected[T]], ValType:
  def elements: Iterable[Val[T]]
end Collected

case object Empty extends Collected[Nothing], ValQ[Empty], ValType:
  def elements: Iterable[Val[Nothing]] = Iterable.empty[Val[Nothing]]
end Empty
type Empty = Empty.type

final case class NonEmpty[T <: ValType](elements: Val[T]*) extends Collected[T], ValQ[NonEmpty[T]], ValType:
  assert(elements.nonEmpty)
end NonEmpty

/**
  * Outputs a Collected with one element added to it.
  */
final case class AddElement[T <: ValType]
(element: Code[Expr[T]], others: Code[Expr[Collected[T]]]) extends Expr[NonEmpty[T]]

/**
  * (Non-deterministically) extracts one element from a Collected.
  */
final case class UnAddElement[T <: ValType]
(element: Code[Xctr[T]], others: Code[Xctr[Collected[T]]]) extends Xctr[NonEmpty[T]]

/**
  * Waits for @size elements from @elementSource, then outputs them as a Collected.
  */
final case class Collect[T <: ValType]
(elementSource: Code[Inp[T]], size: Code[Expr[Integer]]) extends Expr[Collected[T]]
// todo here, Inp[T] means that multiple values can be received, which is not its usual meaning;
//  should a different type be used for this? Or simply the channel directly?

/**
  * Outputs the elements of a Collected to @elementSource, and its size to @size.
  */
final case class UnCollect[T <: ValType]
(elementSource: Code[Out[T]], size: Code[Xctr[Integer]]) extends Xctr[Collected[T]]
