import java.util.concurrent.locks.{Lock, ReentrantLock}

object E_2_3 extends App {
  def log(msg: String): Unit = {
    println(s"${Thread.currentThread.getName}: $msg")
  }

  class Hashi {
    val lock = new ReentrantLock()

    def pickUp(): Unit = lock.lock()
    def putDown(): Unit = lock.unlock()
  }

  class SushiTray {
    val lock = new ReentrantLock()

    def pickUp(): Unit = lock.lock()
    def putDown(): Unit = lock.unlock()
  }

  class Person(hashiL: Hashi, hashiR: Hashi, sushiTray: SushiTray, name: String) extends Thread {
    override def run(): Unit = {
      for (i <- 0 to 3) {
        sushiTray.pickUp()
        try {
          log(name + " picked up sushi tray")
          hashiL.pickUp()
          try {
            log(name + " picked up left hashi")
            hashiR.pickUp()
            try {
              log(name + " picked up right hashi")
              sushiTray.putDown()
              log(name + " returned sushi tray")
              log(name + " is eating sushi")
              Thread.sleep(5000)
            } finally {
              hashiR.putDown()
              log(name + " returned right hashi")
            }
          } finally {
            hashiL.putDown()
            log(name + " returned left hashi")
          }
        } finally {
          log(name + " returned sushi tray")
        }
        Thread.sleep(3000)
      }
    }
  }

  val sushiTray = new SushiTray()

  val hashi1 = new Hashi()
  val hashi2 = new Hashi()
  val hashi3 = new Hashi()
  val hashi4 = new Hashi()
  val hashi5 = new Hashi()

  val luffy = new Person(hashi1, hashi2, sushiTray, "Luffy")
  val saitama = new Person(hashi2, hashi3, sushiTray, "Saitama")
  val sasuke = new Person(hashi3, hashi4, sushiTray, "Sasuke")
  val satoru = new Person(hashi4, hashi5, sushiTray, "Satoru")
  val sakura = new Person(hashi5, hashi1, sushiTray, "Sakura")

  luffy.start()
  saitama.start()
  sasuke.start()
  satoru.start()
  sakura.start()

  luffy.join()
  saitama.join()
  sasuke.join()
  satoru.join()
  sakura.join()
}