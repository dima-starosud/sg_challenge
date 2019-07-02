package sg_challenge

import java.util.concurrent.ConcurrentHashMap
import scala.collection.JavaConverters._

class HashMapStorage extends Storage {
  private val map = new ConcurrentHashMap[String, String]()

  override def get(): Map[String, String] = map.asScala.toMap

  override def get(key: String): Option[String] = Option(map.get(key))

  override def put(key: String, value: String): Unit = map.put(key, value)

  override def clear(): Unit = map.clear()
}
