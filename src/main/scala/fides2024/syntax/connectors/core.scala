package fides2024.syntax.connectors

import fides2024.syntax.kinds.{Code, Component, Expr, ValType, Xctr}
import fides2024.syntax.values.Pulse

/**
  * A hard-coded connection between one input and one output
  */
final case class Forward[T <: ValType](input: Code[Expr[T]], output: Code[Xctr[T]]) extends Component

/**
  * Kind-of the dual of values.
  *
  * Aka Sink, Forget
  */
final case class Ignore() extends Xctr[ValType]

final case class Duplicate[T <: ValType]
(value: Code[Expr[T]], first: Code[Xctr[T]], second: Code[Xctr[T]]) extends Component

final case class Hold[T <: ValType](signal: Code[Expr[Pulse]], value: Code[Expr[T]]) extends Expr[T]

final case class Signal(trigger: Code[Expr[?]]) extends Expr[Pulse]

final case class Pick[T <: ValType](first: Code[Expr[T]], second: Code[Expr[T]]) extends Expr[T]

final case class UnPick[T <: ValType](first: Code[Xctr[T]], second: Code[Xctr[T]]) extends Xctr[T]
// todo doesn't really make sense in a refutable pattern, right? Maybe Ptrn should not be a supertype of Xctr?
//  But that sounds like it's overcomplicating things... could be a Component.
//  On the other hand, one could argue that it doesn't really make less sense than an Out in a pattern;
//  It only actually gets triggered if the pattern matches.
