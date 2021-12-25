package controllers

import anchor.{ServiceData, ServicesData}
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test._
import play.api.test.Helpers._
import anchor.Drivers._

class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "annual-value" should {

    implicit val serviceDataJson = Json.format[ServiceData]
    implicit val servicesDataJson = Json.format[ServicesData]

    "render annual-value from the application" in {
      val controller = inject[HomeController]
      val body = Json.toJson(ServicesData(Seq(aWeeklyServiceData(price = 100))))
      val request = FakeRequest(method = POST, path = "/annual-value").withJsonBody(body)
      val annualValue = controller.annualValue().apply(request)

      status(annualValue) mustBe OK
      val result = contentAsString(annualValue)
      result must be (s"""{"annualValue":5200}""")
    }

    "render annual-value from the router" in {
      val body = Json.toJson(ServicesData(services = Seq(aWeeklyServiceData(price = 100),
                                                         aMonthlyServiceData(price = 1))))
      val request = FakeRequest(method = POST, path = "/annual-value").withJsonBody(body)
      val annualValue = route(app, request).get

      status(annualValue) mustBe OK
      val result = contentAsString(annualValue)
      result must be (s"""{"annualValue":5212}""") //5200 + 12
    }

    "return 0 if no services" in {
      val body = Json.toJson(ServicesData(services = Seq.empty))
      val request = FakeRequest(method = POST, path = "/annual-value").withJsonBody(body)
      val annualValue = route(app, request).get

      status(annualValue) mustBe OK
      val result = contentAsString(annualValue)
      result must be (s"""{"annualValue":0}""")
    }

    "return Bad Request - invalid data" in {
      val body = Json.toJson(ServicesData(services = Seq(aWeeklyServiceData(price = 100),
                                                         aMonthlyServiceData(price = 5).copy(dayOfWeek = Some("monday")))))
      val request = FakeRequest(method = POST, path = "/annual-value").withJsonBody(body)
      val annualValue = route(app, request).get

      status(annualValue) mustBe BAD_REQUEST
    }

    "return Bad Request - negative price" in {
      val body = Json.toJson(ServicesData(services = Seq(aWeeklyServiceData(price = 100),
                                                         aMonthlyServiceData(price = -5))))
      val request = FakeRequest(method = POST, path = "/annual-value").withJsonBody(body)
      val annualValue = route(app, request).get

      status(annualValue) mustBe BAD_REQUEST
    }

    "return Bad Request - no data" in {
      val request = FakeRequest(method = POST, path = "/annual-value")
      val annualValue = route(app, request).get

      status(annualValue) mustBe BAD_REQUEST
    }
  }
}
