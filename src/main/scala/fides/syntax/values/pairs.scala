package fides.syntax.values

import fides.syntax.code.{Code, Expr, Ptrn, Val, ValQ, ValType, Xctr}

/**
  * A value that is made up of two values.
  */
final case class Paired[+T1 <: ValType, +T2 <: ValType](
  first: Code[Val[T1]],
  second: Code[Val[T2]],
) extends ValQ[Paired[T1, T2]], ValType

/**
  * Pairs two values together.
  */
final case class Pair[T1 <: ValType, T2 <: ValType](
  first: Code[Expr[T1]],
  second: Code[Expr[T2]],
) extends Expr[Paired[T1, T2]]

/**
  * Extracts the elements of a pair.
  */
final case class UnPair[T1 <: ValType, T2 <: ValType](
  first: Code[Xctr[T1]],
  second: Code[Xctr[T2]],
) extends Xctr[Paired[T1, T2]]

// todo delete
def matchPair2[
  P1 <: N1,
  P2 <: N2,
  N1 <: ValType,
  N2 <: ValType,
  U >: Paired[N1, N2] <: ValType,
](): [L <: Paired[P1, P2]] => (
  Paired[P1, P2] <:< (L | Paired[Nothing, Nothing]),
  (U & Paired[ValType, ValType]) <:< Paired[N1, N2],
) ?=> (Code[Ptrn[P1, N1]], Code[Ptrn[P2, N2]]) => Ptrn[L, U] = [L <: Paired[P1, P2]] => (
  lP: Paired[P1, P2] <:< (L | Paired[Nothing, Nothing]),
  uP: (U & Paired[ValType, ValType]) <:< Paired[N1, N2],
) ?=> (t1: Code[Ptrn[P1, N1]], t2: Code[Ptrn[P2, N2]]) => MatchPair[P1, P2, N1, N2, L, U](t1, t2)

// todo delete
//def matchPair[L <: ValType]: [
//  P1 <: N1,
//  P2 <: N2,
//  N1 <: ValType,
//  N2 <: ValType,
//  U >: Paired[N1, N2] | L <: ValType,
//] => (
//  Paired[P1, P2] <:< (L | Paired[Nothing, Nothing]),
//  (U & Paired[ValType, ValType]) <:< Paired[N1, N2],
//  L <:< Paired[P1, P2],
//) ?=> (Code[Ptrn[P1, N1]], Code[Ptrn[P2, N2]]) => Ptrn[L, U] = [
//  P1 <: N1,
//  P2 <: N2,
//  N1 <: ValType,
//  N2 <: ValType,
//  U >: Paired[N1, N2] | L  <: ValType,
//] => (
//  lP: Paired[P1, P2] <:< (L | Paired[Nothing, Nothing]),
//  uP: (U & Paired[ValType, ValType]) <:< Paired[N1, N2],
//  lQ: L <:< Paired[P1, P2],
//) ?=> (t1: Code[Ptrn[P1, N1]], t2: Code[Ptrn[P2, N2]]) => MatchPair[
//  P1,
//  P2,
//  N1,
//  N2,
//  L,
//  U,
//](t1, t2)

/**
  * Pair pattern.
  */
final case class MatchPair[
  P1 <: N1,
  P2 <: N2,
  N1 <: ValType,
  N2 <: ValType,
  L <: Paired[P1, P2],
  U >: Paired[N1, N2] <: ValType,
](
  first: Code[Ptrn[P1, N1]],
  second: Code[Ptrn[P2, N2]],
)(using
  Paired[P1, P2] <:< (L | Paired[Nothing, Nothing]),
  (U & Paired[ValType, ValType]) <:< Paired[N1, N2],
) extends Ptrn[L, U]
// todo we probably need sealed types for the compiler to be able to provide those implicits reliably

// todo Try using type classes to show the four different cases, while having only one class (Pair).
//  (See type-class-syntax branch.)
