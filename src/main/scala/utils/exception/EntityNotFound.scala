package utils.exception

case class EntityNotFound(msg: String) extends RuntimeException(msg)
