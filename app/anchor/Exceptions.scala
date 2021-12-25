package anchor

object Exceptions {

  case class UnsupportedYearException(message: String) extends RuntimeException(message)
  case class InvalidServiceDataException(message: String) extends RuntimeException(message)
}