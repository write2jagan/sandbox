package it.balyfix.efpi

import org.specs2.{Specification, ScalaCheck,runner}
import Chapter2._
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import org.scalacheck.Properties
import org.scalacheck.Prop.forAll
import org.scalacheck.{Gen, Prop}

@RunWith(classOf[JUnitRunner])
class Chapter2Spec extends Specification with ScalaCheck {
  
  def fib2(n: Int): Int = n match {
    case 1 => 1
    case 2 => 1
    case _ => fib2(n - 1) + fib2(n - 2)
  }
  
  def add(a: Int, b :Int) : Int = a + b 

  def is = "fib" ! check {
    Prop.forAll(Gen.chooseNum(1, 40)) { n: Int => fib(n) == fib2(n) }
  } ^ "isSorted" ! check {
    Prop.forAll { xs: List[Int] => isSorted(xs.toArray, (x: Int, y: Int) => x > y) == (xs.sorted == xs) }
  } ^ "curry" ! check {
    (a: Int, b: Int) => add(a,b)== curry(add)(a)(b)
  }
  
  
}

