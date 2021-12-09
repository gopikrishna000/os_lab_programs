// Java implementation of a producer and consumer
// that use semaphores to control synchronization.

import java.util.*;
import java.util.concurrent.Semaphore;

//public static int size=5;



// Driver class
public class pc_countingsemaphore {

    public static class Q {
	// an item
	Queue<Integer> que =new LinkedList<Integer>();

	// semCon initialized with 0 permits
	// to ensure put() executes first
	static Semaphore semCon = new Semaphore(0);
    static Semaphore mutex = new Semaphore(1);
	static Semaphore semProd = new Semaphore(5);

	// to get an item from buffer
	void get()
	{
		try {
			// Before consumer can consume an item,
			// it must acquire a permit from semCon
			semCon.acquire();
		}
		catch (InterruptedException e) {
			System.out.println("InterruptedException caught");
		}

try {
			// Before consumer can consume an item,
			// it must acquire a permit from semCon
			mutex.acquire();
		}
		catch (InterruptedException e) {
			System.out.println("InterruptedException caught");
		}



		// consumer consuming an item
		System.out.println("Consumer consumed item : " + que.poll());

   		// After consumer consumes the item,
		// it releases semProd to notify producer
        mutex.release();
		semProd.release();
	}

	// to put an item in buffer
	void put(int item)
	{
		try {
			// Before producer can produce an item,
			// it must acquire a permit from semProd
			semProd.acquire();
		}
		catch (InterruptedException e) {
			System.out.println("InterruptedException caught");
		}

try {
			// Before consumer can consume an item,
			// it must acquire a permit from semCon
			mutex.acquire();
		}
		catch (InterruptedException e) {
			System.out.println("InterruptedException caught");
		}

		// producer producing an item
		que.add(item);

		System.out.println("Producer produced item : " + item);

		// After producer produces the item,
		// it releases semCon to notify consume
        mutex.release();
		semCon.release();
	}
}

// Producer class
public static class Producer implements Runnable {
	Q q;
    Semaphore mutex;
	Producer(Q q)
	{
		this.q = q;
		new Thread(this, "Producer").start();
	}

	public void run()
	{
		for (int i = 0; i < 25; i++){
			// producer
            q.put(i);          
        }
	}
}

// Consumer class
public static class Consumer implements Runnable {
	Q q;
	Consumer(Q q)
	{
		this.q = q;
        new Thread(this, "Consumer").start();
	}

	public void run()
	{
		for (int i = 0; i < 25; i++){
			// consumer get items
			    
            q.get();
        }
	}
}


	public static void main(String args[])
	{
		// creating buffer queue

		Q q = new Q();

		// starting consumer thread
		new Consumer(q);

		// starting producer thread
		new Producer(q);
        
	}
}
