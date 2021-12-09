import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.*;
import java.io.PrintWriter;
import java.util.concurrent.Semaphore;
import java.util.Scanner;



public class reader_writer{

    public static int rcnt = 0; 

public static class Writer implements Runnable{
    
    Semaphore writ;
    Semaphore mut;

    Writer(Semaphore writ,Semaphore mut){
        this.writ = writ;
        this.mut = mut;
        new Thread(this,"writer").start();
    }

   public void run(){
       try{
        writ.acquire();

        File file = new File("hello.txt");
        file.createNewFile();
        Scanner sc = new Scanner(System.in);
        String data = sc.nextLine();
PrintWriter pw = new PrintWriter(new FileOutputStream(file));
pw.println(data);
pw.close();

        writ.release();
       }
       catch(InterruptedException e){

       }
       catch(IOException f){

       }
    }

}

public static class Reader implements Runnable{

    Semaphore writ;
    Semaphore mut;

    Reader(Semaphore writ,Semaphore mut){
        this.writ = writ;
        this.mut = mut;
        new Thread(this,"reader").start();     
    }
    
   public void run(){
       try{
        mut.acquire();
        rcnt++;
        if(rcnt==1)
        writ.acquire();                
        mut.release();


File file = new File("hello.txt");
BufferedReader br = new BufferedReader(new FileReader(file));

        String msg;
        while((msg = br.readLine())!=null)
        System.out.println(msg);
br.close();        
        mut.acquire();
        rcnt--;
        if(rcnt==0)
        writ.release();
        mut.release();
       }
       catch(InterruptedException e){

       }
    catch(IOException f){

    }
    }


}


    public static void main(String arg[]){
        Semaphore writ = new Semaphore(1);
        Semaphore mut = new Semaphore(1);
       
        
        File file = new File("hello.txt");
        file.mkdir();
        
        
        new Reader(writ,mut);

        new Writer(writ,mut);

        try{
        Thread.sleep(5000);
        }
        catch(InterruptedException e)
        {

        }
        new Reader(writ,mut);


    }


}