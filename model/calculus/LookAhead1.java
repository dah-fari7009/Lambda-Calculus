package model.calculus;

class LookAhead1  {
    /* Simulating a reader class for a stream of Token */

    private Token current;
    private Lexer lexer;

    public LookAhead1(Lexer l)
	throws Exception {
	lexer=l;
	current=lexer.yylex();
    }

    public boolean check(Sym s)
	throws Exception {
	/* check whether the first character is of type s*/
          return (current.getSym() == s);
    }

    public Token getCurrent(){
      return current;
    }

    public Lexer getLexer(){
      return lexer;
    }


    public void eat(Sym s)
	throws Exception {
	/* consumes a token of type s from the stream,
	   exception when the contents does not start on s.   */
	if (!check(s)) {
	    throw new Exception("\n" + lexer.getPosition()+": Can't eat "+s+" current being "+current);
	}
		//for debug
	  // System.out.println(current);

        current=lexer.yylex();
   }

  /*  public String getValue()
    throws Exception {
    // it gives the value of the ValuedToken, or it rises an exception if not ValuedToken
    	if (current instanceof ValuedToken) {
    		ValuedToken t = (ValuedToken) current;
    		return t.getValue();
    	} else {
	    throw new Exception("\n"+ lexer.getPosition()+": LookAhead error: get value from a non-valued token");
    	}

    }*/

    public boolean isEmpty(){
      return current==null;
    }

}
