package controllers

import anchor.{AnnualPriceCalculator, AnnualValueResult, ServiceAnnualDataConverter, ServiceData, ServiceDataValidator, ServicesData}

import javax.inject._
import play.api.libs.json._
import play.api.mvc._

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  implicit val serviceDataJson = Json.format[ServiceData]
  implicit val servicesDataJson = Json.format[ServicesData]
  implicit val annualValueResultJson = Json.format[AnnualValueResult]

  // curl -v -d '{"services": ...}' -H 'Content-Type: application/json' -X POST localhost:9000/annual-value
  def annualValue() = Action {
    implicit request =>
    val content = request.body
    val jsonObject = content.asJson

    val maybeServicesData: Option[ServicesData] = jsonObject.flatMap(Json.fromJson[ServicesData](_).asOpt)

    maybeServicesData match {
      case Some(servicesData) if ServiceDataValidator.isValid(servicesData.services) =>

        val servicesAnnualData = servicesData.services.map(ServiceAnnualDataConverter.toServiceAnnualData)
        try {
          val annualValueResult = AnnualPriceCalculator.calculateAnnualValue(servicesAnnualData)
          Ok(Json.toJson(AnnualValueResult(annualValueResult)))
        } catch {
          case _:Exception => BadRequest
        }
      case _ =>
        BadRequest
    }
  }

}
