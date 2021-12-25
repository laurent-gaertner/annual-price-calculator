package anchor

import Constants._
import anchor.Exceptions.InvalidServiceDataException

//TODO: Split to Weekly and Monthly Validator
object ServiceDataValidator {

  def isValid(services: Seq[ServiceData]): Boolean = {
    try {
      services.foreach(ServiceDataValidator.validate)
      true
    } catch {
      case _: InvalidServiceDataException => false
    }
  }

  def validate(serviceData: ServiceData): Unit = {
    validatePeriod(serviceData)
    validateWeekly(serviceData)
    validateMonthly(serviceData)
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
      case _:Exception => //catches exception of resolveDayOfWeek and `get`
        throw InvalidServiceDataException(s"Invalid DayOfWeek Value for service ${serviceData.name}")
    }
  }

  private def validateDayOfMonthIsEmpty(serviceData: ServiceData): Unit = {
    if (serviceData.dayOfMonth.isDefined)
      throw InvalidServiceDataException(s"DayOfMonth should not be provided for service ${serviceData.name}")
  }

  private def validateDayOfWeekIsEmpty(serviceData: ServiceData): Unit = {
    if (serviceData.dayOfWeek.isDefined)
      throw InvalidServiceDataException(s"DayOfWeek should not be provided for service ${serviceData.name}")
  }

  private def validateMonthly(serviceData: ServiceData): Unit = {
    if (serviceData.isMonthly) {
      validateDayOfMonth(serviceData)
      validateDayOfWeekIsEmpty(serviceData)
    }
  }

  private def validateDayOfMonth(serviceData: ServiceData): Unit = {
    serviceData.dayOfMonth match {
      case None => throw InvalidServiceDataException(s"DayOfMonth is not provided for service ${serviceData.name}")
      case Some(dayOfMonth) => {
        if (!dayOfMonth.equalsIgnoreCase(Last))//if dayOfMonth == 'last' -> valid
          dayOfMonthValidationNumber(dayOfMonth, serviceData.name)
      }
    }
  }

  private def dayOfMonthValidationNumber(dayOfMonth: String, serviceName: String): Unit = {
    try {
      val dayOfMonthInt = dayOfMonth.toInt
      if (dayOfMonthInt < 1 || dayOfMonthInt > 27)
        throw InvalidServiceDataException(s"DayOfMonth not in valid range for service $serviceName")
    } catch {
      case _: NumberFormatException => throw InvalidServiceDataException(s"DayOfMonth not a valid number for service $serviceName")
    }
  }

}


