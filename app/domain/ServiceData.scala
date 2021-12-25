package domain

import utils.Constants.{Monthly, Weekly}

import java.time.DayOfWeek

case class ServiceData(name: String,
                       period: String,
                       dayOfWeek: Option[String],
                       dayOfMonth: Option[String],
                       price: Int) {

  def isWeekly: Boolean = isNotEmpty(period) && period.toLowerCase.equalsIgnoreCase(Weekly)
  def isMonthly: Boolean = isNotEmpty(period) && period.toLowerCase.equalsIgnoreCase(Monthly)

  //Can throw an Exception
  def resolveDayOfWeek: Option[DayOfWeek] = dayOfWeek.map(dayOfWeek => DayOfWeek.valueOf(dayOfWeek.toUpperCase))

  private def isNotEmpty(value: String): Boolean = Option(value).isDefined

}


case class ServicesData(services: Seq[ServiceData])
