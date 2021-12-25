package validators

import utils.Constants.Last
import domain.{DayOfMonth, DayOfMonthInt, DayOfMonthString, ServiceData}
import exceptions.Exceptions.InvalidServiceDataException

import scala.util.{Failure, Success, Try}

//TODO: Split to Weekly and Monthly Validator
object ServiceDataValidator {

  def isValid(services: Seq[ServiceData]): Boolean = {
    Try(services.foreach(ServiceDataValidator.validate)) match {
      case Success(_) => true
      case Failure(_) => false
    }
  }

  def validate(serviceData: ServiceData): Unit = {
    validatePeriod(serviceData)
    validateWeekly(serviceData)
    validateMonthly(serviceData)
    validatePrice(serviceData.price, serviceData.name)
  }

  private def validatePeriod(serviceData: ServiceData): Unit = {
    if (!serviceData.isWeekly && !serviceData.isMonthly)
      throw InvalidServiceDataException(s"Invalid Period value for service ${serviceData.name}")
  }

  private def validateWeekly(serviceData: ServiceData): Unit = {
    if (serviceData.isWeekly) {
      validateDayOfWeek(serviceData)
      validateDayOfMonthIsEmpty(serviceData)
    }
  }

  private def validateDayOfWeek(serviceData: ServiceData): Unit = {
    try {
      serviceData.resolveDayOfWeek.get
    } catch {
      case _: Exception => //catches exception of resolveDayOfWeek and `get`
        throw InvalidServiceDataException(s"Invalid DayOfWeek Value for service ${serviceData.name}")
    }
  }

  private def validateDayOfMonthIsEmpty(serviceData: ServiceData): Unit = {
    if (serviceData.dayOfMonth.isDefined)
      throw InvalidServiceDataException(s"DayOfMonth should not be provided for service ${serviceData.name}")
  }

  private def validateDayOfWeekIsEmpty(dayOfWeek: Option[String], serviceName: String): Unit = {
    if (dayOfWeek.isDefined)
      throw InvalidServiceDataException(s"DayOfWeek should not be provided for service $serviceName")
  }

  private def validateMonthly(serviceData: ServiceData): Unit = {
    if (serviceData.isMonthly) {
      validateDayOfMonth(serviceData.dayOfMonth, serviceData.name)
      validateDayOfWeekIsEmpty(serviceData.dayOfWeek, serviceData.name)
    }
  }

  private def validateDayOfMonth(maybeDayOfMonth: Option[DayOfMonth], serviceName: String): Unit = {
    maybeDayOfMonth match {
      case None => throw InvalidServiceDataException(s"DayOfMonth is not provided for service $serviceName")
      case Some(dayOfMonth) => {
        dayOfMonth match {
          case DayOfMonthString(value) =>
            if (!value.equalsIgnoreCase(Last)) throw InvalidServiceDataException(s"DayOfMonth not valid for service $serviceName")
          case DayOfMonthInt(dayOfMonthInt) => dayOfMonthValidationNumber(dayOfMonthInt, serviceName)
        }
      }
    }
  }

  private def dayOfMonthValidationNumber(dayOfMonth: Int, serviceName: String): Unit = {
      if (dayOfMonth < 1 || dayOfMonth > 27)
        throw InvalidServiceDataException(s"DayOfMonth not in valid range for service $serviceName")
  }

  private def validatePrice(price: Int, serviceName: String): Unit = {
    if (price < 0)
      throw InvalidServiceDataException(s"Price is negative for service $serviceName")
  }

}
