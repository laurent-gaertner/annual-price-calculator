package drivers

import calculators.ServiceAnnualData
import domain.{DayOfMonth, DayOfMonthInt, ServiceData}
import utils.Constants.{Monthly, SupportedYear, Weekly}

import java.time.DayOfWeek


object Drivers {

  def aDayOfWeekServiceAnnualData(dayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
                                  year: Int = SupportedYear,
                                  price: BigDecimal = BigDecimal(Math.random)): ServiceAnnualData = {
    ServiceAnnualData(price = price,
      Some(dayOfWeek),
      isMonthly = false,
      year = year)
  }

  def aDayOfMonthServiceAnnualData(year: Int = SupportedYear,
                                   price: BigDecimal = BigDecimal(Math.random)): ServiceAnnualData = {
    ServiceAnnualData(price = price,
      maybeDayOfWeek = None,
      isMonthly = true,
      year = year)
  }

  def aMonthlyServiceData(price: Int = 1234,
                          dayOfMonth: Option[DayOfMonth] = Some(DayOfMonthInt(27))): ServiceData = {
    ServiceData(name = "abcde",
      period = Monthly,
      dayOfWeek = None,
      dayOfMonth = dayOfMonth,
      price = price)
  }

  def aWeeklyServiceData(dayOfWeek: String = "Monday", price: Int = 1234): ServiceData = {
    ServiceData(name = "abcde",
      period = Weekly,
      dayOfWeek = Some(dayOfWeek),
      dayOfMonth = None,
      price = price)
  }

}
