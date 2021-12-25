package controllers

import calculators.AnnualPriceCalculator
import converters.ServiceAnnualDataConverter
import domain.{AnnualValueResult, ServiceData, ServicesData}

import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import validators.ServiceDataValidator

@Singleton
class MainController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  implicit val serviceDataJson = Json.format[ServiceData]
  implicit val servicesDataJson = Json.format[ServicesData]
  implicit val annualValueResultJson = Json.format[AnnualValueResult]

  def annualValue() = Action {
    implicit request =>
    val content = request.body
    val jsonObject = content.asJson

    val maybeServicesData: Option[ServicesData] = jsonObject.flatMap(Json.fromJson[ServicesData](_).asOpt)

    maybeServicesData match {
      case Some(servicesData) if ServiceDataValidator.isValid(servicesData.services) =>

        val servicesAnnualData = servicesData.services.map(ServiceAnnualDataConverter.toServiceAnnualData)
        val annualValueResult = AnnualPriceCalculator.calculateAnnualValue(servicesAnnualData)
        Ok(Json.toJson(AnnualValueResult(annualValueResult)))
      case _ =>
        BadRequest
    }
  }

}
