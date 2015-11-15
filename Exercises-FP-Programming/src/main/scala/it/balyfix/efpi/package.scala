package it.balyfix.efpi

import scala.util.Try

package object efpi {
  def const[A, B](b: B) = (_: A) => b

  object IntegerString {
    def unapply(s: String) = Try {
      s.toInt
    }.toOption
  }
}
