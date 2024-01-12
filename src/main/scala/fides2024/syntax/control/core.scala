package fides2024.syntax.control

import fides2024.syntax.identifiers.{Cell, Inp}
import fides2024.syntax.kinds.{Atom, Code, Component, Expr, ValQ, Xctr}
import fides2024.syntax.meta.Quoted
import fides2024.syntax.values.{Bool, Pulse}

sealed trait Order extends Atom, ValQ[Order]
case object Kill extends Order
case object Stop extends Order
case object Start extends Order

final case class Awake(leash: Code[Inp[Order]], body: Code[Component]) extends Component
final case class Asleep(leash: Code[Inp[Order]], body: Code[Component]) extends Component

/**
  * @param running indicates whether the body is currently running
  */
final case class Process(running: Code[Cell[Bool]], body: Code[Component]) extends Component

/**
  * Upon reception of a pulse, the body's execution is started.
  */
final case class OnHold(startSignal: Code[Expr[Pulse]], body: Code[Component]) extends Component

/**
  * Upon reception of a pulse, the body's execution is stopped.
  */
final case class Mortal(killSignal: Code[Expr[Pulse]], body: Code[Component]) extends Component

/**
  * No message can reach and/or exit the sandboxed component directly. The monitor serves as the intermediate.
  * It can send and receive messages to and from the sandboxed component.
  */
final case class Sandboxed(monitor: Code[Component], sandboxed: Code[Component]) extends Component

final case class Catchable
(catchSignal: Code[Expr[Pulse]], body: Code[Component], codeReader: Xctr[Quoted[Component]]) extends Component
// todo catchable that can be restarted?
