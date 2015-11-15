package it.balyfix.basic

class miscBaseClass {

  def f(s: String) = "f(" + s + ")"

  def g(s: String) = "g(" + s + ")"

  val one: PartialFunction[Int, String] = { case 1 => "one" }

  val two: PartialFunction[Int, String] = { case 2 => "two" }

  val three: PartialFunction[Int, String] = { case 3 => "three" }

  val wildcard: PartialFunction[Int, String] = { case _ => "something else" }

  val partial = one orElse two orElse three orElse wildcard
  
  val mixList = 2 :: 1 :: "bar" :: "foo" :: Nil

  class sum extends Function[Int, Int] {

    def apply(v: Int): Int = v + 100;

  }

  def sumCollection: Unit =
    {
      val numbers = List(1, 2, 3, 4);

      val sumTest = new sum();

      val mylist = numbers.toStream.map { x => sumTest.apply(x) }.toList;

      mylist.foreach { x => println(x) }

    }

  def filterCollection: Unit =
    {
      val numbers = List(1, 2, 3, 4);

      val sumTest = new sum();

      val mylist = numbers.toStream.map { x => sumTest.apply(x) }.toList;

      mylist.foreach { x => println(x) }

    }

  def convertImmutable: Unit =
    {
      val numbers = List(1, 2, 3, 4);
    }

  def functionComposition: Unit = {

    val fComposeG = f _ compose g _

    val fAndThenG = f _ andThen g _

    println(fComposeG("ciao"))
    println(fAndThenG("ciao"))
    

  }

}