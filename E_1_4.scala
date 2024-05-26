import java.util.concurrent.locks.{Lock, ReentrantLock}

object E_1_4 extends App {

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

  val lock: ReentrantLock = new ReentrantLock()

  val t1 = new Thread {
    override def run() = {
      while (true) {
        log("Enter noncritical section")
        dummy(1)
        log("Leaving noncritical section")
        lock.lock()
        try{
            log("Enter critical section")
            dummy(2)
        } 
        finally {
            log("Leaving critical section")
            lock.unlock()
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
        lock.lock()
        try{
            log("Enter critical section")
            dummy(2)
        } 
        finally {
            log("Leaving critical section")
            lock.unlock()
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