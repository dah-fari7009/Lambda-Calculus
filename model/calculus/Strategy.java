package model.calculus;

import java.util.ArrayList;

abstract class Strategy{

  public LambdaTerm reduce(LambdaTerm term,ArrayList<LambdaTerm> list){
    //System.out.println(term.getClass()+" To be reduced :"+term);
    if(term instanceof Variable) return reduceVar((Variable)term,list);
    if(term instanceof Abstraction) return reduceAbs((Abstraction)term,list);
    return reduceApp((Application)term,list);
  }

  public  Variable reduceVar(Variable var,ArrayList<LambdaTerm> list){
    return var;
  }

  public Abstraction reduceAbs(Abstraction abs,ArrayList<LambdaTerm> list){
      return new Abstraction(abs.binding(),reduce(abs.body(),list));
  }

  public abstract LambdaTerm reduceApp(Application app,ArrayList<LambdaTerm> list);

}

class StrategyAOR extends Strategy{

  public LambdaTerm reduceApp(Application app,ArrayList<LambdaTerm> list){
    if(app.func() instanceof Abstraction && app.arg().isInBnf()){
      Abstraction a=(Abstraction)app.func();
      //here that is the reduced redex,find a way to take it
      list.add(app);
      return a.substitute(a.binding(),app.arg()).body();
    }
    else if(app.func().isInBnf()) return new Application(app.func(),reduce(app.arg(),list));
    else return new Application(reduce(app.func(),list),app.arg());


  }

  public String toString(){
    return ("Call-by-value Reduction (AOR)");
  }

}

class StrategyNOR extends Strategy{

  public LambdaTerm reduceApp(Application app,ArrayList<LambdaTerm> list){
    if(app.func() instanceof Abstraction){
      Abstraction a=(Abstraction)app.func();
      list.add(app);
      //here is the reduced redex, find a way
      return a.substitute(a.binding(),app.arg()).body();
    }
    return new Application(reduce(app.func(),list),reduce(app.arg(),list));
  }

  public String toString(){
    return ("Call-by-name Reduction (NOR)");
  }
}
