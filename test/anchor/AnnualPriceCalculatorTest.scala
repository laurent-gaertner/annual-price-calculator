package anchor

import Drivers._
import Constants._
import Exceptions.UnsupportedYearException
import org.specs2.mutable.SpecWithJUnit

class AnnualPriceCalculatorTest extends SpecWithJUnit {

  "AnnualPriceCalculator::calculateAnnualValueForService" should {

    "throw Exception if year is not supported" >> {
      val serviceAnnualData = aDayOfWeekServiceAnnualData(year = 2022)

      AnnualPriceCalculator.calculateAnnualValueForService(serviceAnnualData) must throwA[UnsupportedYearException]
    }

    "return zero if price is zero" >> {
      val zeroPrice = aDayOfWeekServiceAnnualData(price = ZERO)

      AnnualPriceCalculator.calculateAnnualValueForService(zeroPrice) must beEqualTo(ZERO)
    }

    "calculate annual price for days of Week in 2021" >> {
      val serviceAnnualData = aDayOfWeekServiceAnnualData()

      val expectedAnnualValue = serviceAnnualData.price * 52 //52 Mondays in 2021
      AnnualPriceCalculator.calculateAnnualValueForService(serviceAnnualData) must beEqualTo(expectedAnnualValue)
    }

    "calculate annual price for days of Month in 2021" >> {
      val serviceAnnualData = aDayOfMonthServiceAnnualData()

      val expectedAnnualValue = serviceAnnualData.price * 12
      AnnualPriceCalculator.calculateAnnualValueForService(serviceAnnualData) must beEqualTo(expectedAnnualValue)
    }
  }

  "AnnualPriceCalculator::calculateAnnualValue" should {

    "throw Exception if one serviceData is invalid" >> {

      val unsupportedYear = aDayOfWeekServiceAnnualData(year = 2022)

      AnnualPriceCalculator.calculateAnnualValue(Seq(aDayOfWeekServiceAnnualData(), unsupportedYear)) must throwA[UnsupportedYearException]
    }

    "sum the prices of all services annual values" >> {
      val dayOfMonthAnnualData = aDayOfMonthServiceAnnualData()
      val dayOfMonthAnnualDataPriceZero = aDayOfMonthServiceAnnualData(price = ZERO)
      val dayOfWeekAnnualData = aDayOfWeekServiceAnnualData()
      val dayOfWeekPriceZero = aDayOfWeekServiceAnnualData(price = ZERO)

      val expectedPrice = dayOfMonthAnnualData.price * 12 + dayOfWeekAnnualData.price * 52
      AnnualPriceCalculator.calculateAnnualValue(servicesAnnualData =
        Seq(dayOfMonthAnnualData, dayOfMonthAnnualDataPriceZero, dayOfWeekAnnualData, dayOfWeekPriceZero)) must beEqualTo(expectedPrice)
    }
  }
}
