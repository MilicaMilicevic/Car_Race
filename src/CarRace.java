package src;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import src.car.Car;

public class CarRace{
  private Object[][] stage;
  private List<Car> cars;
  private volatile boolean isEnd;
  
  private static CarRace uniqueCRace=new CarRace();//singleton!
  
  private CarRace(){
    stage=new Object[3][15];
    cars=new ArrayList<Car>();
  }
  
  public static void main(String[] args){
    CarRace uniqueRace=getUCRace();
    for(int i=0;i<4;i++)
      uniqueRace.dropStop(); //popuni stazu STOP znakovima!
    uniqueRace.cars.addAll(Arrays.asList(new Car[]{new Car(),new Car(),new Car()}));//kreiraj automobile
    uniqueRace.setCars();
    System.out.println(uniqueRace.stageToString());
    System.out.println("Simulation begins.");
    Iterator iteratorCar1=uniqueRace.cars.iterator();
    while(iteratorCar1.hasNext())
      ((Car)iteratorCar1.next()).start();
    Iterator iteratorCar2=uniqueRace.cars.iterator();
    try {
      while(iteratorCar2.hasNext())
        ((Car)iteratorCar2.next()).join();
    }
    catch(InterruptedException e){
      e.printStackTrace();
    }
    System.out.println("Simulation ends.");
  }

  private void dropStop(){
    Random generator=new Random();
    boolean isSetted=false;//pomocna
    while(!isSetted){
      int r=generator.nextInt(stage.length);
      int c=generator.nextInt(stage[0].length-1)+1;  //da ne postavi u prvu kolonu!
      if(!(stage[r][c] instanceof String)){
        stage[r][c]="STOP";
        isSetted=true;
      }
    }
  }
  
  public static CarRace getUCRace(){
    return uniqueCRace;
  }
 
  private void setCars(){
    int i=0;//pomocna
    Iterator iteratorCar=cars.iterator();
    while(iteratorCar.hasNext()){
      Car car=(Car)iteratorCar.next();
      stage[i][0]=car;
      car.setPosition(i++,0);
    }
  }
  
  public String stageToString(){
    String result="";
    for(int i=0;i<stage.length;i++)
      for(int j=0;j<stage[i].length;j++)
      if(stage[i][j]!=null)
      result+=stage[i][j]+" at ["+i+"]["+j+"]\n";
    return result;
  }
  
  public Object[][] getStage(){
    return stage;
  }
      
  public void setEnd(){
    isEnd=true;
  }
  
  public boolean isEnd(){
    return isEnd;
  }
    
}
    