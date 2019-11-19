
import model.calculus.OpAndCons;
import model.calculus.Sym;
import model.calculus.Tree;


public class Parser2{
  /*protected LookAhead1 reader;

  public Parser(LookAhead1 r){
    reader=r;
  }

  public class Pair{
    protected Boolean key;
    protected LambdaTerm value;

    public Pair(Boolean key, LambdaTerm term){
      this.key=key;
      this.value=term;
    }

    public LambdaTerm getValue(){
      return value;
    }

    public Boolean getKey(){
      return key;
    }
  }

  Grammar
  term:=variable|(application)|(abstraction)|func
  variable:=[a-z]
  func_sym:=[A-Z]
  abstraction:=\0955 variable.term
  application:=term term

  term:=(term)|application|variable|abstraction
  term:=(application)|variable|abstraction
  variable:=[a-z]
  abstraction:=\0955 variable.term
  application:=term application'
  application'=term application'|epsilon
  application:=application term|term(abs or var)


  public Tree progNotTerm(int i) throws Exception{
    Tree tree=new Tree(lambdaterm(),(i==0)?new StrategyNOR():new StrategyAOR());
    if(!reader.isEmpty()) throw new Exception(reader.getLexer().getPosition()+"  Not valid end of file");
    return tree;
  }

  public LambdaTerm lambdaterm() throws Exception{
    return lambdat().getValue();
  }

  public Pair lambdat() throws Exception{
    LambdaTerm term=null;
    boolean condition=false;
    if(reader.check(Sym.VAR)){
      term=new Variable((((WordToken)reader.getCurrent()).getContent()).charAt(0));
      reader.eat(Sym.VAR);
    }
    else if(reader.check(Sym.NUM)) term=church_numeral();
    else if(reader.check(Sym.TRUE) || reader.check(Sym.FALSE)){
      term=trueOrfalse();
      condition=true;
    }
    else if(reader.check(Sym.IF)) term=conditional();
    else {
      reader.eat(Sym.LPAR);
      if(reader.check(Sym.LAMBDA)){
        reader.eat(Sym.LAMBDA);
        term=abstraction();
      }
      else{
        Pair p=application();
        term=p.getValue();
        condition=p.getKey();
      }
      reader.eat(Sym.RPAR);
    }
    return new Pair(condition,term);
  }


  public Application conditional() throws Exception{
    reader.eat(Sym.IF);
    Pair cond=lambdat();
    boolean condition=cond.getKey();
    if(!condition) throw new Exception("Unexpected condition following !");
    reader.eat(Sym.THEN);
    LambdaTerm t1=lambdaterm();
    reader.eat(Sym.ELSE);
    LambdaTerm t2=lambdaterm();
    return new Application(new Application(new Application(OpAndCons.parse(OpAndCons.iF),cond.getValue()),t1),t2);
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

  public Pair application() throws Exception{
    if(reader.check(Sym.NOT)){
      reader.eat(Sym.NOT);
      Pair cond=lambdat();
      boolean condition=cond.getKey();
      if(!condition) throw new Exception("Unexpected condition following !");
      return new Pair(true,new Application(OpAndCons.parse(OpAndCons.not),cond.getValue()));
    }
    else{
      LambdaTerm t1;
      Pair cond=lambdat();
      boolean condition=cond.getKey();
      t1=cond.getValue();
      String s=null;
      if(!condition){
        Token o=reader.getCurrent();
        switch(o.getSym()){
        case PLUS: reader.eat(Sym.PLUS);
          s=OpAndCons.plus;
        //  t0=OpAndCons.plus();
          break;
        case MULT:reader.eat(Sym.MULT);
          s=OpAndCons.mult;
        //  t0=OpAndCons.mult();
          break;
          case EXP:reader.eat(Sym.EXP);
          s=OpAndCons.exp;
          break;
      }
    }
    else{
      if(reader.check(Sym.AND)){
        reader.eat(Sym.AND);
        s=OpAndCons.and;
      }
      else if(reader.check(Sym.OR)){
        reader.eat(Sym.OR);
        s=OpAndCons.or;
      }
    }
    if(s==null) return new Pair(false,new Application(t1,lambdaterm()));
    else{
      LambdaTerm t0=OpAndCons.parse(s);
      Pair cond2=lambdat();
      boolean condition2=cond2.getKey();
      LambdaTerm t2=cond2.getValue();
      if(condition!=condition2) throw new Exception("Unexpected condition following !");
      return new Pair(condition,new Application(new Application(t0,t1),t2));
  }
}
}*/

  /*abstract class Strategy{

  public Redexes reduce(LambdaTerm term){
    if(term instanceof Variable) return reduceVar((Variable)term);
    if(term instanceof Abstraction) return reduceAbs((Abstraction)term);
    return reduceApp((Application)term);
  }

  public  Redexes reduceVar(Variable var){
    return new Redexes(var,var);
  }

  public Redexes reduceAbs(Abstraction abs){
      return new Redexes(abs,new Abstraction(abs.binding(),reduce(abs.body())));
  }

  public abstract LambdaTerm reduceApp(Application app);

}

class StrategyAOR extends Strategy{

  public Redexes reduceApp(Application app){
    if(app.func().isInBnf() && app.arg().isInBnf()){
      Abstraction a=(Abstraction)app.func();
      return new Redexes(app,a.substitute(a.binding(),app.arg()).body());
    }
    else if(app.func().isInBnf()) return new Redexes(app,new Application(app.func(),reduce(app.arg())));
    else return new Redexes(app,new Application(reduce(app.func()),app.arg()));


  }

  public String toString(){
    return ("Call-by-value Reduction (AOR)");
  }

}

class StrategyNOR extends Strategy{

  public Redexes reduceApp(Application app){
    if(app.func() instanceof Abstraction){
      Abstraction a=(Abstraction)app.func();
      return new Redexes(app,a.substitute(a.binding(),app.arg()).body());
    }
    return new Redexes(app,new Application(reduce(app.func()),reduce(app.arg())));
  }

  public String toString(){
    return ("Call-by-name Reduction (NOR)");
  }
}

*/



}
