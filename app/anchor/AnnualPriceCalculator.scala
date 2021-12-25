package anchor

import java.time.DayOfWeek
import Constants.{SupportedYear, ZERO}
import Exceptions.{NegativePriceException, UnsupportedYearException}

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
    price.compare(ZERO) match {
      case 0 => ZERO
      case -1 => throw NegativePriceException("Price can't be negative")
      case _ => {
        val multiplier = MultiplierCalculator.calculateMultiplier(maybeDayOfWeek, isMonthly, year)
        price * multiplier
      }
    }
  }
}

case class ServiceAnnualData(price:BigDecimal,//in cents
                             maybeDayOfWeek: Option[DayOfWeek],
                             isMonthly: Boolean,
                             year: Int)

