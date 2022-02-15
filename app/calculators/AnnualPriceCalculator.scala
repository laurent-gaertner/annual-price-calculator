package calculators

import utils.Constants.{SupportedYear, ZERO}
import exceptions.Exceptions.UnsupportedYearException

import java.time.DayOfWeek


object AnnualPriceCalculator {

  def calculateAnnualValue(servicesAnnualData: Seq[ServiceAnnualData]): BigDecimal = {
    servicesAnnualData match {
      case Nil => BigDecimal(0) //Annual Value is zero if no services
      case _ => servicesAnnualData.map(calculateAnnualValueForService).sum
    }
  }

  def calculateAnnualValueForService(serviceAnnualData: ServiceAnnualData): BigDecimal = {
    serviceAnnualData.year match {
      case SupportedYear => calculateAnnualValue(serviceAnnualData)
      case _ => throw UnsupportedYearException(s"Only year $SupportedYear is supported")
    }
  }

  private def calculateAnnualValue(serviceAnnualData: ServiceAnnualData): BigDecimal = {
    import serviceAnnualData._

    if (price.equals(ZERO))
      ZERO
    else {
      val multiplier = MultiplierCalculator.calculateMultiplier(maybeDayOfWeek, isMonthly, year)
      price * multiplier
    }
  }

}

case class ServiceAnnualData(price:BigDecimal,//in cents
                             maybeDayOfWeek: Option[DayOfWeek],
                             isMonthly: Boolean,
                             year: Int)

