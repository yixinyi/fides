package doodle

import scala.Ordering.Implicits._
import scala.collection.mutable

object MaxNumberExercise extends App {
  type Number = Seq[Int]

  var callCount = 0

  val example: Set[Number] = Set(
    Seq(2),
    Seq(3),
    Seq(2, 4),
    Seq(2, 4, 3, 9),
    Seq(2, 3),
    Seq(7),
    Seq(7, 7),
    Seq(5)
  )
  println(SimpleSolution(example))
  println("Call count: " + callCount)
  callCount = 0
  println(SimpleSolution.mem(example))
  println("Call count: " + callCount)
  println(solution(example))

  def solution(numbers: Set[Number]): Number = numbers.toSeq.sortWith(greaterThan).flatten

  def greaterThan(x: Number, y: Number): Boolean = x match {
    case Nil => false
    case xHead :: xTail => y match {
      case Nil => true
      case yHead :: yTail => (xHead > yHead) || (xHead == yHead) && (compareTails(xTail, yTail)(xHead) > 0)
    }
  }

  //noinspection RedundantBlock
  @scala.annotation.tailrec
  def compareTails(x: Number, y: Number)(implicit current: Int): Int = {
    x match {
      case Nil => -compareToNil(y)
      case xHead :: xTail => {
        y match {
          case Nil => compareToNil(x)
          case yHead :: yTail => {
            if (xHead < yHead) -1
            else if (xHead > yHead) 1
            else compareTails(xTail, yTail)
          }
        }
      }
    }
  }

  //noinspection RedundantBlock
  @scala.annotation.tailrec
  def compareToNil(x: Number)(implicit current: Int): Int = x match {
    case Nil => 0
    case head :: tail => {
      if (head < current) -1
      else if (head > current) 1
      else compareToNil(tail)
    }
  }

  object SimpleSolution extends RecFun[Set[Number], Number] {
    override def rec(self: selfT)(numbers: Set[Number]): Number = {
      callCount += 1
      if (numbers.isEmpty) Nil
      else (for (number <- numbers) yield number ++ self(numbers - number)).max
    }
  }

  trait RecFun[A, B] {
    type selfT = A => B

    def rec(self: selfT)(a: A): B

    final def apply(a: A): B = rec(apply)(a)

    def mem: A => B = {
      val map = mutable.Map.empty[A, B]

      def memF(a: A): B = {
        map.getOrElseUpdate(a, rec(memF)(a))
      }

      memF
    }
  }
}
