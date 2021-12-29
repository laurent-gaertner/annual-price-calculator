package controllers

import calculators.AnnualPriceCalculator
import converters.ServiceAnnualDataConverter
import domain.{AnnualValueResult, ServiceData, ServicesData}
import exceptions.Exceptions.InvalidServiceDataException

import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import validators.ServiceDataValidator
import JsonHelper._

@Singleton
class MainController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def annualValue() = Action {
    implicit request =>
    val content = request.body
    val jsonObject = content.asJson

   jsonObject.flatMap(Json.fromJson[ServicesData](_).asOpt) match {
     case None => BadRequest("no data")
     case Some(servicesData) => try {
       calculateAnnualValue(servicesData.services)
       } catch {
         case exc: InvalidServiceDataException => BadRequest(exc.message)
       }
     }
  }

  private def calculateAnnualValue(services: Seq[ServiceData]): Result = {
    services.foreach(ServiceDataValidator.validate)

    val servicesAnnualData = services.map(ServiceAnnualDataConverter.toServiceAnnualData)
    val annualValueResult = AnnualPriceCalculator.calculateAnnualValue(servicesAnnualData)
    Ok(Json.toJson(AnnualValueResult(annualValueResult)))
  }

}
