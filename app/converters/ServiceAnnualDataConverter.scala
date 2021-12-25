package converters

import utils.Constants.{Monthly, SupportedYear}
import calculators.ServiceAnnualData
import domain.ServiceData

object ServiceAnnualDataConverter {

  def toServiceAnnualData(serviceData: ServiceData): ServiceAnnualData = {
    import serviceData._
    ServiceAnnualData(price = BigDecimal(price),
                      maybeDayOfWeek = serviceData.resolveDayOfWeek,
                      isMonthly = period.equalsIgnoreCase(Monthly),
                      year = SupportedYear)
  }

}
