package calculators

import utils.Constants.SupportedYear
import exceptions.Exceptions.UnsupportedYearException
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
      MultiplierCalculator.calculateMultiplier(Some(DayOfWeek.FRIDAY), isMonthly = false, SupportedYear) must beEqualTo(53)
    }

    "return 12 if it's monthly" >> {
      MultiplierCalculator.calculateMultiplier(None, isMonthly = true, SupportedYear) must beEqualTo(12)
    }
  }
}