package sg_challenge

import com.twitter.finagle.Http
import com.twitter.util.Await

object Server extends App {
  val service = new AnnotationService(new HashMapStorage, HtmlAnnotator)
  val server = Http.serve(":3001", service)
  Await.result(server)
}
