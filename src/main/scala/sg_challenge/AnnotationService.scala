package sg_challenge

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Service, http}
import com.twitter.util.Future

object AnnotationService {

  case class UrlValue(url: String)

  case class Annotation(name: String, url: String)

}

class AnnotationService(storage: Storage, annotator: Annotator)
  extends Service[http.Request, http.Response] {

  import AnnotationService._

  private val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)

  override def apply(request: Request): Future[Response] = {
    import http.Method._
    import http.Status._
    import http.path._

    (request.method, Path(request.path)) match {
      case (Put, Root / "names" / name) =>
        val urlValue = mapper.readValue(request.contentString, classOf[UrlValue])
        storage.put(name, urlValue.url)

      case (Get, Root / "names" / name) =>
        storage.get(name) match {
          case None =>
            request.response.status = NotFound
          case Some(data) =>
            request.response.contentString = mapper.writeValueAsString(Annotation(name, data))
        }

      case (Delete, Root / "names") =>
        storage.clear()

      case (Post, Root / "annotate") =>
        request.response.contentString = annotator.annotate(storage.get(), request.contentString)

      case _ =>
        request.response.status = NotFound
    }

    Future.value(request.response)
  }
}
