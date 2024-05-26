object E_1_2 extends App {

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

  @volatile var b1: Boolean = false
  @volatile var b2: Boolean = false
  @volatile var k: Int = 1

  val t1 = new Thread {
    override def run() = {
      while (true) {
        
        // noncritical actions
        log("Enter noncritical section") 
        dummy(1)
        log("Leaving noncritical section")
        b1 = true
        while (k != 1) {
          while (b2) {
          }
          k = 1
        }
    
        // critical actions
        log("Enter critical section")
        dummy(2)
        log("Leaving critical section")
        b1 = false
      }
    }
  }

  val t2 = new Thread {
    override def run() = {
      while (true) {

        // noncritical actions
        log("Enter noncritical section") 
        dummy(1)
        log("Leaving noncritical section")
        b2 = true
        while (k != 2) {
          while (b1) {
          }
          k = 2
        }
        
        // critical actions
        log("Enter critical section")
        dummy(2)
        log("Leaving critical section")
        b2 = false
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