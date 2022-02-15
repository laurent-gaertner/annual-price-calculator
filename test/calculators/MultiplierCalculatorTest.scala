package calculators

import utils.Constants.SupportedYear
import exceptions.Exceptions.{UnsupportedPeriodException, UnsupportedYearException}
import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.core.Fragments
import java.time.DayOfWeek


class MultiplierCalculatorTest extends SpecWithJUnit {

  "calculateMultiplier for dayOfWeek" should {

    "throw Exception if year is not supported" >> {
      MultiplierCalculator.calculateMultiplier(Some(DayOfWeek.MONDAY), isMonthly = false, 2022) must throwA[UnsupportedYearException]
    }

    Fragments.foreach(Seq(DayOfWeek.MONDAY,DayOfWeek.TUESDAY,DayOfWeek.WEDNESDAY,DayOfWeek.THURSDAY,DayOfWeek.SATURDAY,DayOfWeek.SUNDAY)) { dayOfWeek =>
      Fragments {
        "return 52 for non-Friday in 2021" >> {
          MultiplierCalculator.calculateMultiplier(Some(dayOfWeek), isMonthly = false, SupportedYear) must beEqualTo(52)
        }
      }
    }

    "return 53 for Friday in 2021" >> {
      MultiplierCalculator.calculateMultiplier(maybeDayOfWeek = Some(DayOfWeek.FRIDAY), isMonthly = false, SupportedYear) must beEqualTo(53)
    }
  }

  "calculateMultiplier for dayOfMonth" should {

    "return 12 if it's monthly" >> {
      MultiplierCalculator.calculateMultiplier(maybeDayOfWeek = None, isMonthly = true, SupportedYear) must beEqualTo(12)
    }
  }

  "calculateMultiplier for neither monthly nor weekly" should {

    "throw UnsupportedPeriodException" >> {
      MultiplierCalculator.calculateMultiplier(maybeDayOfWeek = None, isMonthly = false, 2021) must throwA[UnsupportedPeriodException]
    }
  }
}