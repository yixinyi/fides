package fides.syntax.meta

import fides.syntax.code.{Code, CodeType, Expr, Ptrn, Val, ValType, Xctr}
import fides.syntax.values.Integer

/**
  * Code as value, used for metaprogramming
  */
trait Quoted[+S <: CodeType] private[syntax]() extends Val[Quoted[S]], ValType:
  def code: Code[S]
object Quoted:
  def apply[S <: CodeType](`$code`: Code[S]): Quoted[S] = new Quoted[S]():
    val code: Code[S] = `$code`
    override def toString: String = s"Quoted($code)"
end Quoted

/**
  * Allows escaping the body of a Quote.
  *
  * (At the top-level (outside of a quote), could represent macro code.)
  */
final case class Escape[S <: CodeType]
(code: Code[Expr[Quoted[S]] | Xctr[Quoted[S]] | Ptrn[Quoted[S], Quoted[S]]]) extends Code[S]
// todo should we keep track of the polarity in the extended type?

/**
  * Allows matching an escape(matcher). See also SignedMatcher.
  */
final case class EscapeMatcher[S <: CodeType]
(level: Code[Val[Integer]], code: Code[Expr[Quoted[S]] | Xctr[Quoted[S]] | Ptrn[Quoted[S], Quoted[S]]]) extends Code[S]
// todo EscapeMatcher could be merged with Escape (and similarly for other matchers)

/**
  * Analoguous to s-Strings in Scala, but for code
  *
  * Once all the Escape inside @code have been evaluated and spliced in, reduces to a Quoted.
  */
final case class Quote[S <: CodeType](code: Code[S]) extends Expr[Quoted[S]]

/**
  * Code extractor.
  */
final case class MatchQuote[S <: CodeType](code: Code[S]) extends Code[Ptrn[Quoted[S], ValType]]
