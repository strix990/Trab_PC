import java.util.concurrent.atomic.AtomicInteger

object E_1_5 extends App {
  
  def log(msg: String): Unit = {
    println(s"${Thread.currentThread.getName}: $msg")
  }

  def dummy(a: Int): Unit = {
    if(a == 1) {
        log("Despite all my rage, I'm just a non critical dummy")
        Thread.sleep(4000)
    }
    if(a == 2) {
        log("Despite all my rage, I'm just a critical dummy")
        Thread.sleep(4000)
    }
  }
  
  val atomicInteger = new AtomicInteger(3)

  val t1 = new Thread {
    override def run() = {
      while (true) {
        log("Enter noncritical section")
        dummy(1)
        log("Leaving noncritical section")
        val value = atomicInteger.get()
        while(!atomicInteger.compareAndSet(3, 1)) {
        }
        try {
          log("Enter critical section")
          dummy(2)
        }
        finally {
          log("Leaving critical section")
          atomicInteger.set(3)
        }
      }
    }
  }

  val t2 = new Thread {
    override def run() = {
      while (true) {
        log("Enter noncritical section")
        dummy(1)
        log("Leaving noncritical section")
        val value = atomicInteger.get()
        while(!atomicInteger.compareAndSet(3, 2)) {
        }
        try {
          log("Enter critical section")
          dummy(2)
        }
        finally {
          log("Leaving critial section")
          atomicInteger.set(3)
        }
      }
    }
  }

  t1.start()
  log("Started thread1")
  t2.start()
  log("Started thread2")
  t1.join()
  log("Finished thread1")
  t2.join()
  log("Finished thread2")
}