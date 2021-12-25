package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test._
import play.api.test.Helpers._
import drivers.Drivers._
import utils.Constants.ZERO
import domain.{AnnualValueResult, ServiceData, ServicesData}
import play.api.mvc.Result
import scala.concurrent.Future

class MainControllerTest extends PlaySpec with GuiceOneAppPerTest with Injecting {

  implicit val serviceDataJson = Json.format[ServiceData]
  implicit val servicesDataJson = Json.format[ServicesData]
  implicit val annualValueResultJson = Json.format[AnnualValueResult]

  def annualValueCheck(result: Future[Result], expectedAnnualValue: BigDecimal): Unit = {
    val resAsJson = Json.parse(contentAsString(result))
    resAsJson.as[AnnualValueResult].annualValue mustBe expectedAnnualValue
  }

  "annual-value" should {

    "render annual-value from the application" in {
      val controller = inject[MainController]
      val body = Json.toJson(ServicesData(Seq(aWeeklyServiceData(price = 100))))
      val request = FakeRequest(method = GET, path = "/annual-value").withJsonBody(body)
      val annualValue = controller.annualValue().apply(request)

      status(annualValue) mustBe OK
      annualValueCheck(annualValue, BigDecimal(5200))
    }

    "render annual-value from the router" in {
      val body = Json.toJson(ServicesData(services = Seq(aWeeklyServiceData(price = 100),
                                                         aMonthlyServiceData(price = 1))))
      val request = FakeRequest(method = GET, path = "/annual-value").withJsonBody(body)
      val annualValue = route(app, request).get

      status(annualValue) mustBe OK
      annualValueCheck(annualValue, BigDecimal(5212))  //5200 + 12
    }

    "return 0 if no services" in {
      val body = Json.toJson(ServicesData(services = Seq.empty))
      val request = FakeRequest(method = GET, path = "/annual-value").withJsonBody(body)
      val annualValue: Future[Result] = route(app, request).get

      status(annualValue) mustBe OK
      annualValueCheck(annualValue, ZERO)
    }

    "return Bad Request - invalid data" in {
      val body = Json.toJson(ServicesData(services = Seq(aWeeklyServiceData(price = 100),
                                                         aMonthlyServiceData(price = 5).copy(dayOfWeek = Some("monday")))))
      val request = FakeRequest(method = GET, path = "/annual-value").withJsonBody(body)
      val annualValue = route(app, request).get

      status(annualValue) mustBe BAD_REQUEST
    }

    "return Bad Request - no data" in {
      val request = FakeRequest(method = GET, path = "/annual-value")
      val annualValue = route(app, request).get

      status(annualValue) mustBe BAD_REQUEST
    }
  }
}
