package io.codeheroes.metrics

import akka.actor.ActorSystem
import akka.contrib.metrics.CircuitBreakerMetrics
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.ws.{TextMessage, UpgradeToWebSocket}
import akka.http.scaladsl.server.Directives._
import akka.pattern.CircuitBreaker
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{BroadcastHub, Sink}
import org.json4s.DefaultFormats
import org.json4s.native.Serialization

import scala.concurrent.duration._

/**
  * @author mciolek
  */
object Application extends App {
  private implicit val system = ActorSystem()
  private implicit val mat = ActorMaterializer()
  private implicit val scheduler = system.scheduler
  private implicit val ec = system.dispatcher
  private implicit val formats = DefaultFormats

  val service = new ChaoticService
  val breaker = CircuitBreaker(scheduler, 5, 3 seconds, 5 seconds)
  val metrics = CircuitBreakerMetrics.timeBuckets(breaker, 1 second).runWith(BroadcastHub.sink)

  val statsSource = metrics.map(Serialization.write(_)).map(TextMessage(_))

  val routes = (path("jobs") & post) {
    onSuccess(breaker.withCircuitBreaker(service.doTheJob()))(_ => complete(StatusCodes.OK))
  } ~ path("stats") {
    extractRequest { req =>
      req.header[UpgradeToWebSocket] match {
        case Some(upgrade) => complete(upgrade.handleMessagesWithSinkSource(Sink.ignore, statsSource))
        case None => complete(StatusCodes.BadRequest)
      }
    }

  }

  Http.get(system).bindAndHandle(routes, "localhost", 8080)

}
