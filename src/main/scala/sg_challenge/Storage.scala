package sg_challenge

trait Storage {
  def get(): Map[String, String]

  def put(key: String, value: String): Unit

  def get(key: String): Option[String]

  def clear(): Unit
}
