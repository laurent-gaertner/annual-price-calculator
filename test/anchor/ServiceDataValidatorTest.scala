package anchor

import org.specs2.mutable.SpecWithJUnit
import Drivers._
import Exceptions.InvalidServiceDataException

class ServiceDataValidatorTest extends SpecWithJUnit {

  "General ServiceData validation" should {
    "not have an empty period" >> {
      val emptyPeriod = aWeeklyServiceData().copy(period = null)
      ServiceDataValidator.validate(emptyPeriod) must throwA[InvalidServiceDataException]
    }

    "not have an invalid period" >> {
      val emptyPeriod = aWeeklyServiceData().copy(period = "Yearly")
      ServiceDataValidator.validate(emptyPeriod) must throwA[InvalidServiceDataException]
    }

    "not have a negative price" >> {
      val negativePrice = aWeeklyServiceData().copy(price = -10)
      ServiceDataValidator.validate(negativePrice) must throwA[InvalidServiceDataException]
    }
  }

  "Weekly ServiceData validation" should {
    "not throw InvalidServiceDataException for valid ServiceData" >> {
      val weeklyServiceData = aWeeklyServiceData()
      ServiceDataValidator.validate(weeklyServiceData) must not(throwA[InvalidServiceDataException])
    }

    "not have an invalid DayOfWeek" >> {
      val emptyPeriod = aWeeklyServiceData(dayOfWeek = "Invalid")
      ServiceDataValidator.validate(emptyPeriod) must throwA[InvalidServiceDataException]
    }

    "not have an empty DayOfWeek" >> {
      val emptyPeriod = aWeeklyServiceData().copy(dayOfWeek = None)
      ServiceDataValidator.validate(emptyPeriod) must throwA[InvalidServiceDataException]
    }

    "not have a value for DayOfMonth" >> {
      val emptyPeriod = aWeeklyServiceData().copy(dayOfMonth = Some("1"))
      ServiceDataValidator.validate(emptyPeriod) must throwA[InvalidServiceDataException]
    }

  }

  "Monthly ServiceData validation" should {

    "not throw InvalidServiceDataException for valid ServiceData" >> {
      val monthlyServiceData = aMonthlyServiceData()
      ServiceDataValidator.validate(monthlyServiceData) must not(throwA[InvalidServiceDataException])
    }

    "not have empty dayOfMonth" >> {
      val monthlyServiceData = aMonthlyServiceData().copy(dayOfMonth = None)
      ServiceDataValidator.validate(monthlyServiceData) must throwA[InvalidServiceDataException]
    }

    "not have invalid dayOfMonth" >> {
      val monthlyServiceData = aMonthlyServiceData().copy(dayOfMonth = Some("first"))
      ServiceDataValidator.validate(monthlyServiceData) must throwA[InvalidServiceDataException]
    }

    "not have lower range for dayOfMonth" >> {
      val monthlyServiceData = aMonthlyServiceData().copy(dayOfMonth = Some("0"))
      ServiceDataValidator.validate(monthlyServiceData) must throwA[InvalidServiceDataException]
    }

    "not have higher range for dayOfMonth" >> {
      val monthlyServiceData = aMonthlyServiceData().copy(dayOfMonth = Some("28"))
      ServiceDataValidator.validate(monthlyServiceData) must throwA[InvalidServiceDataException]
    }

    "not have a value for DayOfWeek" >> {
      val monthlyServiceData = aMonthlyServiceData().copy(dayOfWeek = Some("Monday"))
      ServiceDataValidator.validate(monthlyServiceData) must throwA[InvalidServiceDataException]
    }

  }

}
