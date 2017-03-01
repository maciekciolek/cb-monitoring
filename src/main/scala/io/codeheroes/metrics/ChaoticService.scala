package io.codeheroes.metrics

import akka.Done
import akka.actor.ActorSystem

import scala.concurrent.duration._
import scala.concurrent.{Future, Promise}
import scala.util.Random

/**
  * @author mciolek
  */
class ChaoticService(implicit system: ActorSystem) {
  private implicit val ec = system.dispatchers.lookup("service-dispatcher")

  def doTheJob(): Future[Done] = Random.nextInt(1000) match {
    case i if i > 0 && i < 900 => Future.successful(Done)
    case i if i >= 900 && i < 950 => Future.failed(new IllegalStateException("BOOM!"))
    case _ =>
      val promise = Promise[Done]
      system.scheduler.scheduleOnce(5 seconds, new Runnable {
        override def run() = promise.success(Done)
      })
      promise.future
  }

}
