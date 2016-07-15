package syntax2

object Syntax {
  type Multiset[A] = Map[A, BigInt]

  final case class Program(definitions: Set[Class], main: Process)

  final case class Class(name: ClassName, parameters: Set[Idee], body: Process)

  sealed trait Process

  trait Atom extends Process
  final case class Constant(value: Val, out: Address) extends Atom
  final case class NewIdee(ideeOut: Address) extends Atom

  final case class Send(packetIn: Port, toIn: Port) extends Atom
  final case class Receive(fromIn: Port, onSetupOut: Address, packetOut: Address) extends Atom

  final case class GetPort(ideeIn: Port, portOut: Address) extends Atom
  final case class GetAddress(ideeIn: Port, addressOut: Address) extends Atom

  final case class Sign(messageIn: Port, signatoryIn: Port, signedMessageOut: Address) extends Atom
  final case class UnpackSigned(signedIn: Port, messageOut: Address, signatoryOut: Address) extends Atom

  final case class Wait(in: Port, startIn: Port, out: Address) extends Atom
  final case class Fork(valIn: Port, valOut1: Address, valOut2: Address) extends Atom

  final case class IfEqual(valIn1: Port, valIn2: Port, equalOut: Address, notEqualOut: Address) extends Atom
  //final case class GetType(valIn: Port, )

  final case class Apply(className: ClassName, arguments: Map[Idee, Val]) extends Process

  final case class Par(processes: Multiset[Process]) extends Process
  final case class MayInterrupt(run: Idee, stop: Idee, kill: Idee, process: Process) extends Process

  final class ClassName

  sealed trait Val
  object Unit extends Val
  sealed trait Value extends Val

  final case class Signed private(contents: Address, signatory: Address) extends Value {
    def this(message: Address, signatory: Idee) = this(message, signatory.address)
  }

  sealed trait Port extends Value
  sealed trait Address extends Value

  final class Idee private extends Port with Address {
    def port: Port = this
    def address: Address = this
  }

  object Idee {
    def makeNew: Idee = {
      val result = new Idee
      result
    }
  }
}
