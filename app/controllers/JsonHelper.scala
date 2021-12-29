package controllers

import domain.{AnnualValueResult, DayOfMonth, DayOfMonthInt, DayOfMonthString, ServiceData, ServicesData}
import play.api.libs.json.{JsNumber, JsResult, JsString, JsSuccess, JsValue, Json, Reads}

object JsonHelper {

  implicit object DataValueReads extends Reads[DayOfMonth] {
    override def reads(json: JsValue): JsResult[DayOfMonth] = {
      json match {
        case JsString(dayOfMonth) => JsSuccess(DayOfMonthString(dayOfMonth))
        case JsNumber(s) => JsSuccess(DayOfMonthInt(s.toInt))
        case _ => throw new Exception("Invalid json value")
      }
    }
  }

  implicit val dayOfMonthIntJson = Json.writes[DayOfMonthInt]
  implicit val dayOfMonthStringJson = Json.writes[DayOfMonthString]
  implicit val dayOfMonthJson = Json.writes[DayOfMonth]

  implicit val serviceDataJson = Json.format[ServiceData]
  implicit val servicesDataJson = Json.format[ServicesData]
  implicit val annualValueResultJson = Json.format[AnnualValueResult]
}
