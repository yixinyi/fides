package fides2024.syntax.values

import fides2024.syntax.kinds.{Atom, Code, Expr, ValQ}

/**
  * Boolean values
  */
sealed trait Bool extends Atom, ValQ[Bool]
case object True extends Bool
case object False extends Bool

/**
  * Outputs true iff its two identifiers are the same.
  */
final case class Equal(first: Code[Expr[Atom]], second: Code[Expr[Atom]]) extends Expr[Bool]
// todo corecursively extend? Or delete, since it's currently a special case of Switch?
