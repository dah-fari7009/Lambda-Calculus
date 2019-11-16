package model.calculus;

class Parser{
  protected LookAhead1 reader;

  public Parser(LookAhead1 r){
    reader=r;
  }

  public Tree progNotTerm(int i) throws Exception{
    Tree tree=new Tree(lambdaterm(),(i==0)?new StrategyNOR():new StrategyAOR());
    System.out.println(tree.term());
    if(!reader.isEmpty()) throw new Exception(reader.getLexer().getPosition()+"  Not valid end of file");
    return tree;
  }

  public LambdaTerm lambdaterm() throws Exception{
    LambdaTerm term=null;
    if(reader.check(Sym.VAR)){
      term=new Variable((((WordToken)reader.getCurrent()).getContent()).charAt(0));
      reader.eat(Sym.VAR);
    }
    else if(reader.check(Sym.NUM)) term=church_numeral();
    else if(reader.check(Sym.TRUE) || reader.check(Sym.FALSE)){
      term=trueOrfalse();
    }
    else if(reader.check(Sym.IF)) term=conditional();
    else if(reader.check(Sym.LET)){
      reader.eat(Sym.LET);
      Variable s=(Variable)lambdaterm();
      reader.eat(Sym.EQ);
      LambdaTerm arg=lambdaterm();
      reader.eat(Sym.IN);
      LambdaTerm app=new Abstraction(s,lambdaterm());
      term=new Application(app,arg);
    }
    else {
      reader.eat(Sym.LPAR);
      if(reader.check(Sym.LAMBDA)){
        reader.eat(Sym.LAMBDA);
        term=abstraction();
      }
      else term=application();
      reader.eat(Sym.RPAR);
    }
    return term;
  }


  public Application conditional() throws Exception{
    reader.eat(Sym.IF);
    LambdaTerm cond=lambdaterm();
    reader.eat(Sym.THEN);
    LambdaTerm t1=lambdaterm();
    reader.eat(Sym.ELSE);
    LambdaTerm t2=lambdaterm();
    return new Application(new Application(new Application(OpAndCons.parse(OpAndCons.iF),cond),t1),t2);
  }

  public Abstraction abstraction() throws Exception{
    Variable s=(Variable)lambdaterm();
    if(reader.check(Sym.DOT)){
      reader.eat(Sym.DOT);
      return new Abstraction(s,lambdaterm());
    }
    return new Abstraction(s,abstraction());
  }

  public Abstraction church_numeral() throws Exception{
      int num=((NumberToken)reader.getCurrent()).getValue();
      reader.eat(Sym.NUM);
    return (Abstraction)OpAndCons.parse(OpAndCons.numeral(num));
  }

  public Abstraction trueOrfalse() throws Exception{
  if(reader.check(Sym.TRUE)){
      reader.eat(Sym.TRUE);
      return OpAndCons.tru();
    }
    reader.eat(Sym.FALSE);
    return OpAndCons.fals();
  }



  public Application application() throws Exception{
    if(reader.check(Sym.NOT)){
      reader.eat(Sym.NOT);
      LambdaTerm cond=lambdaterm();
      return new Application(OpAndCons.parse(OpAndCons.not),cond);
    }
    else if(reader.check(Sym.FAC)){
      reader.eat(Sym.FAC);
      LambdaTerm t=lambdaterm();
      return new Application(OpAndCons.fac(),t);
    }
    else if(reader.check(Sym.FIB)){
      reader.eat(Sym.FIB);
      return new Application(OpAndCons.fib(),lambdaterm());
    }
    else if(reader.check(Sym.PRED)){
      reader.eat(Sym.PRED);
      LambdaTerm t=lambdaterm();
      return new Application(OpAndCons.parse(OpAndCons.pred),t);
    }
    else{
      LambdaTerm t1=lambdaterm();
      String s=null;
        Token o=reader.getCurrent();
        switch(o.getSym()){
        case PLUS: reader.eat(Sym.PLUS);
          s=OpAndCons.plus;
          break;

          case MINUS:reader.eat(Sym.MINUS);
          s=OpAndCons.minus;
          break;

        case MULT:reader.eat(Sym.MULT);
          s=OpAndCons.mult;
          break;

          case EXP:reader.eat(Sym.EXP);
          s=OpAndCons.exp;
          break;

          case AND:reader.eat(Sym.AND);
          s=OpAndCons.and;
          break;

          case OR:reader.eat(Sym.OR);
          s=OpAndCons.or;
          break;

          case EQ:
          reader.eat(Sym.EQ);
          s=OpAndCons.eq;
          break;

          case LEQ:
          reader.eat(Sym.LEQ);
          s=OpAndCons.leq;
          break;

          case NEQ: reader.eat(Sym.NEQ);
           s=OpAndCons.neq;
           break;

          case LT: reader.eat(Sym.LT);
           s=OpAndCons.lt;
           break;

           case GT: reader.eat(Sym.GT);
            s=OpAndCons.gt;
            break;

           case GEQ:
           reader.eat(Sym.GEQ);
           s=OpAndCons.geq;
           break;


          case DIV:
          reader.eat(Sym.DIV);
          s=OpAndCons.div();
          break;

          case DIVI:
          reader.eat(Sym.DIVI);
          s=OpAndCons.intDiv();
          break;

          case MOD:
          reader.eat(Sym.MOD);
          s=OpAndCons.mod();
          break;
      }
    if(s==null){
      /*if(reader.check(Sym.ISZERO)){
        reader.eat(Sym.ISZERO);
        return new Application(OpAndCons.parse(OpAndCons.iszero),t1);
      }*/
      return new Application(t1,lambdaterm());
    }
    else{
      LambdaTerm t0=OpAndCons.parse(s);
      LambdaTerm t2=lambdaterm();
      return new Application(new Application(t0,t1),t2);
  }
}
}

}
