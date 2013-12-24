package src.car;

import java.util.Random;
import java.util.Iterator;

import src.CarRace;
import src.exception.StopException;

enum Type{
  SPORTS,LUXURY,LARGE,SMALL;
  
  public static Type getRType(){
    return values()[new Random().nextInt(values().length)];
  }
}
  
public class Car extends Thread{
  //attrs. koji se setuju prilikom instanciranja
  private Type type;
  private String registrationID;
  
  //attrs. koji se setuju tokom simuacije
  private int[] position;
  
  public Car(){
    type=Type.getRType();
    registrationID=new Random().nextInt(2000)+"-"+new Random().nextInt(3000);
  }
  
  @Override
  public void run(){
    Object[][] stage=CarRace.getUCRace().getStage();
    try{  
      while(position[1]<stage[0].length-1 && !CarRace.getUCRace().isEnd()){   //krecu se samo udesno && ako nije kraj!
        if(stage[position[0]][position[1]+1] instanceof String && stage[position[0]][position[1]+1].equals("STOP")){//ukoliko si naisao na STOP
          System.out.println(this+" - I will stop.");
          sleep(5000);
          moveRight();
        }
        else {
          moveRight();
          sleep(2000);
        }
      }
    synchronized(stage){
      if(!CarRace.getUCRace().isEnd())  //ako vec neko nije dosao do kraja
        throw new StopException(this+" - Got to the end first!");
      else
        throw new StopException(this+" - left "+(stage[0].length-position[1])+" meters to finish.");
    }
   }
   catch(StopException e){
     synchronized(stage){
       System.out.println(e.getMessage());
       CarRace.getUCRace().setEnd();
     }
   }
   catch(InterruptedException e){
     e.printStackTrace();
   }
  }

  private void moveRight(){
    Object[][] stage=CarRace.getUCRace().getStage();
    if(!CarRace.getUCRace().isEnd()){ //SITUACIJA: Auto naidje na STOP - ide u SLEEP mode, probudi se - sljedece je moveRight() [iako je neko vec pobjednik]
      synchronized(stage){
        position[1]++;
        stage[position[0]][position[1]-1]=null;  //oslobodi
        stage[position[0]][position[1]]=this;   //zauzmi
        System.out.println(this+" - I've just moved to "+positionToString());   
      }
    }
  }
    
  private String positionToString(){
    return "["+position[0]+","+position[1]+"]";
  }
  
  @Override
  public String toString(){
    return "["+type+"|"+registrationID+"]";
  }
  
  public void setPosition(int arg1,int arg2){
    if(position==null)
      position=new int[2];
    position[0]=arg1;
    position[1]=arg2;
  }
  
}