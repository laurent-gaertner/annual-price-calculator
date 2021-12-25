package anchor

import org.specs2.mutable.SpecWithJUnit
import ServiceAnnualDataConverter._
import Drivers._
import Constants.SupportedYear
import java.time.DayOfWeek

class ServiceAnnualDataConverterTest extends SpecWithJUnit {

  "toServiceAnnualData" should {

    "convert a monthly ServiceData to ServiceAnnualData" >> {

      val serviceData = aMonthlyServiceData()
      val result = toServiceAnnualData(serviceData)

      //TODO: Create matcher for ServiceAnnualData
      result.price.toInt must beEqualTo(serviceData.price)
      result.isMonthly must beTrue
      result.maybeDayOfWeek must beNone
      result.year must beEqualTo(SupportedYear)
    }

    "convert a weekly ServiceData to ServiceAnnualData" >> {

      val serviceData = aWeeklyServiceData("Wednesday")
      val result = toServiceAnnualData(serviceData)

      //TODO: Create matcher for ServiceAnnualData
      result.price.toInt must beEqualTo(serviceData.price)
      result.isMonthly must beFalse
      result.maybeDayOfWeek must beSome(DayOfWeek.WEDNESDAY)
      result.year must beEqualTo(SupportedYear)
    }

  }

}
