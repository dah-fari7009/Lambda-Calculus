package model.calculus;

import java.util.ArrayList;
public class Tree{
  protected LambdaTerm term;
  protected Strategy evaluationStrat;

  public Tree(LambdaTerm term, Strategy ev){
    this.term=term;
    evaluationStrat=ev;
    term.bond(true);
  }


  public LambdaTerm term(){
    return term;
  }

  public void eval() throws Exception{
    LambdaTerm redex=term.clone();
    ArrayList<LambdaTerm> list=new ArrayList<>();
    int fix=1000;
    int a=0;
    System.out.println("Chosen reduction strategy:: "+evaluationStrat.toString()+"\n");
    System.out.println(redex);
   // redex.bond(false);
    while(!redex.isInBnf()){
      redex=evaluationStrat.reduce(redex,list);
      //redex.bond(false);
      System.out.println(redex);
      if(list.size()>=3){
        for(int i=0;i<list.size()-1;i++){
          if(redex.isAlphaEquivalent(list.get(i+1)) && list.get(list.size()-1).isAlphaEquivalent(list.get(i))){
            System.out.println("It seems that this method does not lead to a normal form ...");
            if(evaluationStrat instanceof StrategyAOR){
              System.out.println("Let's try something else ! \n");
              evaluationStrat=new StrategyNOR();
              eval();
            }
            return;
          }
          i+=2;
      }
      /*if(redex.size()>100000){
        System.out.println("It seems that this method does not lead to a normal form ...");
        return;
      }*/
      /*if(i==fix){

      }
      i ++;
  }*/
}
  list.add(redex);
}
    redex.bond(false);
    if(redex.isAlphaEquivalent(OpAndCons.parse(OpAndCons.tru))) System.out.println("true");
    else if(redex.isAlphaEquivalent(OpAndCons.parse(OpAndCons.fals))) System.out.println("0/false");
    else{
      try{
        int number=redex.churchN();
        System.out.println(number);
      }
      catch(Exception e){
          //e.printStackTrace();
      }
    }
  System.out.println("The expression can not be further reduced");

    }


}



/*public class Tree{
  protected LambdaTerm term;
  protected Strategy evaluationStrat;

  public Tree(LambdaTerm term, Strategy ev){
    this.term=term;
    evaluationStrat=ev;
    term.bond(true);
  }

  public class Redexes implements Cloneable{
    protected LambdaTerm chosen,obtained;

    public Redexes(LambdaTerm c, LambdaTerm term){
      chosen=c;
      obtained=term;
    }

    public LambdaTerm chosen(){
      return chosen;
    }

    public LambdaTerm obtained(){
      return obtained;
    }

    public Redexes clone() throws CloneNotSupportedException{
      return new Redexes(chosen.clone(),obtained.clone());
    }

  }

  public LambdaTerm term(){
    return term;
  }

  public void eval() throws Exception{
    Redexes current=null,prev=null;
    LambdaTerm redex=term.clone();
    int fix=1000;
    int i=0;
    System.out.println("Chosen reduction strategy:: "+evaluationStrat.toString()+"\n");
    System.out.println(redex);
    while(!redex.isInBnf()){
      if(current!=null) prev=current.clone();
      current=new Redexes(redex,evaluationStrat.reduce(redex));
      redex=current.obtained();
      redex.bond(false);
      System.out.println(redex);
      if(prev!=null && current.chosen().isAlphaEquivalent(prev.chosen()) && current.obtained().isAlphaEquivalent(prev.obtained())){
        System.out.println("It seems that this method does not lead to a normal form ...");
        if(evaluationStrat instanceof StrategyAOR){
          System.out.println("Let's try something else ! \n");
          evaluationStrat=new StrategyNOR();
          eval();
        }
        return;
      }
      if(i==fix){
        System.out.println("It seems that this method does not lead to a normal form ...");
        return;
      }
      i ++;
  }
    if(redex.isAlphaEquivalent(OpAndCons.parse(OpAndCons.tru))) System.out.println("true");
    else if(redex.isAlphaEquivalent(OpAndCons.parse(OpAndCons.fals))) System.out.println("0/false");
    else{
      try{
        int number=redex.churchN();
        System.out.println(number);
      }
      catch(Exception e){

      }
    }
  System.out.println("The expression can not be further reduced");

    }


}*/
