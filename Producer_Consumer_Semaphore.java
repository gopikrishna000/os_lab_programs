import java.util.concurrent.Semaphore;

class Que {
	int item;

	static Semaphore semCon = new Semaphore(0);
	static Semaphore semProd = new Semaphore(1);

	void get()
	{
		try {
			semCon.acquire();
		}
		catch (InterruptedException e) {
			System.out.println("InterruptedException caught");
		}

		System.out.println("Consumer consumed item : " + item);

		semProd.release();
	}

	void put(int item)
	{
		try {
			semProd.acquire();
		}
		catch (InterruptedException e) {
			System.out.println("InterruptedException caught");
		}

		this.item = item;

		System.out.println("Producer produced item : " + item);

		semCon.release();
	}
}

class Producer implements Runnable {
	Que q;
	Producer(Que q)
	{
		this.q = q;
		new Thread(this, "Producer").start();
	}

	public void run()
	{
		for (int i = 0; i < 5; i++)
			// producer put items
			q.put(i);
	}
}

class Consumer implements Runnable {
	Que q;
	Consumer(Que q)
	{
		this.q = q;
		new Thread(this, "Consumer").start();
	}

	public void run()
	{
		for (int i = 0; i < 5; i++)
			q.get();
	}
}

class Producer_Consumer_Semaphore {
	public static void main(String args[])
	{
	Que q = new Que();
	new Consumer(q);
	new Producer(q);
	}
}
