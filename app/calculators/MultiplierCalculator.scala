package calculators

import utils.Constants.SupportedYear
import exceptions.Exceptions.UnsupportedYearException

import java.time.DayOfWeek

object MultiplierCalculator {

  def calculateMultiplier(maybeDayOfWeek: Option[DayOfWeek], isMonthly: Boolean, year: Int): Int = {
    year match {
      case SupportedYear => calculate2021Multiplier(maybeDayOfWeek, isMonthly)
      case _ => throw UnsupportedYearException(s"Only year $SupportedYear is supported")
    }
  }

  private def calculate2021Multiplier(maybeDayOfWeek: Option[DayOfWeek], isMonthly: Boolean): Int = {
    maybeDayOfWeek match {
      case Some(dayOfWeek) => getDayOfWeek2021Multiplier(dayOfWeek)
      case _ if isMonthly => 12 //Because input is 1 to 27 Or last => response will always be 12
    }
  }

  /**
   * In 2021 there were 53 Fridays, all other days of the week were present 52 times
   * https://calendarmaniacs.com/days-of-year/how-many-fridays-in-2021.html
   * No need to calculate that on every call as this is a constant value
   *
   * @param dayOfWeek
   * @return
   */
  private def getDayOfWeek2021Multiplier(dayOfWeek: DayOfWeek): Int = {
    dayOfWeek match {
      case DayOfWeek.FRIDAY => 53
      case _ => 52
    }
  }

}
