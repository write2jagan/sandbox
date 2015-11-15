package calculator

object Polynomial {
  def computeDelta(a: Signal[Double], b: Signal[Double],
      c: Signal[Double]): Signal[Double] = {
		  //Δ = b² - 4ac
    Signal
    {
      b.apply() * b.apply() - 4d* a.apply() * c.apply() 
    }
    
    
  }

  
  //(-b ± √Δ) / (2a)
  def computeSolutions(a: Signal[Double], b: Signal[Double],
      c: Signal[Double], delta: Signal[Double]): Signal[Set[Double]] = {
    Signal
    {
      if(a.apply() != 0 )
      {
    	  if(delta.apply() > 0)
    	  {
    		  Set(1,-1).map(-b.apply() + _ * math.sqrt(delta.apply())).map(_  / (2*a.apply()))
    	  }else if(delta.apply() == 0)
    	  {
          if(a.apply() != 0 && b.apply() != 0)
          {
        	  val value = - b.apply() / (2 * a.apply())
        			  Set(value,value)
          }else
          {
            Set()
          }
    	  }else
        {
          Set()
        }
      }else
      {
        Set()
      }
    }
  }
}
