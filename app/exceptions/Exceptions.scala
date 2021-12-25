package exceptions

object Exceptions {

  case class UnsupportedYearException(message: String) extends RuntimeException(message)

  case class InvalidServiceDataException(message: String) extends RuntimeException(message)

  case class UnsupportedPeriodException(message: String) extends RuntimeException(message)
}
