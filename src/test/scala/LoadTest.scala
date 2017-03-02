

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

/**
  * @author mciolek
  */
class LoadTest extends Simulation {
  val httpConf = http.baseURL("http://localhost:8080")

  val scn = scenario("JobsSimulation")
    .repeat(1000, "n")(
      exec(http("Execute Job")
        .post("/jobs")
      ).pause(200 millis)
    )

  setUp(
    scn.inject(
      atOnceUsers(10),
      rampUsers(500) over (60 seconds)
    )
  ).protocols(httpConf)
}
